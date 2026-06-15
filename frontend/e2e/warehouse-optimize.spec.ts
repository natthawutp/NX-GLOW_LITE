import { expect, test, type Locator, type Page } from '@playwright/test';

async function sampleCanvasPixel(canvas: Locator, x: number, y: number): Promise<number[]> {
  return canvas.evaluate((element, point) => {
    const context = (element as HTMLCanvasElement).getContext('2d');
    if (!context) {
      throw new Error('Canvas 2D context is not available.');
    }
    return Array.from(context.getImageData(point.x, point.y, 1, 1).data);
  }, { x, y });
}

async function loginAsAdminAndEnterPreferredTenant(page: Page): Promise<void> {
  await page.goto('/login');
  await expect(page.getByRole('button', { name: /sign in/i })).toBeVisible();

  await page.locator('input[name="email"]').fill('admin@wms.com');
  await page.locator('p-password input').fill('admin123');
  await page.getByRole('button', { name: /sign in/i }).click();

  await expect(page).toHaveURL(/select-tenant/, { timeout: 30000 });
  const preferredTenant = page.locator('.warehouse-group', { hasText: 'BKK' }).locator('.tenant-card', { hasText: 'TESA' });
  if (await preferredTenant.count()) {
    await preferredTenant.first().click();
  } else {
    await expect(page.locator('.warehouse-group').first()).toBeVisible({ timeout: 30000 });
    await page.locator('.warehouse-group').first().locator('.tenant-card').first().click();
  }
  await page.getByRole('button', { name: /enter workspace/i }).click();

  await expect(page).toHaveURL(/dashboard/, { timeout: 30000 });
}

test('admin can enter BKK/TESA and use Warehouse Optimize screens', async ({ page }) => {
  test.setTimeout(180000);

  await loginAsAdminAndEnterPreferredTenant(page);

  await page.goto('/warehouse-optimize/design');
  await expect(page.getByRole('heading', { name: /warehouse optimize/i })).toBeVisible();
  const designerCanvas = page.locator('#designerCanvas');
  await designerCanvas.scrollIntoViewIfNeeded();
  await expect(designerCanvas).toBeVisible({ timeout: 30000 });
  await expect(page.getByRole('button', { name: /draw/i })).toBeVisible();

  page.once('dialog', dialog => void dialog.accept());
  await page.getByRole('button', { name: /new blank/i }).click();
  await page.waitForTimeout(500);
  await designerCanvas.scrollIntoViewIfNeeded();

  const canvasBox = await designerCanvas.boundingBox();
  expect(canvasBox).not.toBeNull();
  if (!canvasBox) {
    throw new Error('Designer canvas is not available for drawing.');
  }

  const pixelBefore = await sampleCanvasPixel(designerCanvas, 200, 180);
  await designerCanvas.hover();
  await page.mouse.move(canvasBox.x + 140, canvasBox.y + 120);
  await page.mouse.down();
  await page.mouse.move(canvasBox.x + 280, canvasBox.y + 260, { steps: 10 });
  await page.mouse.up();
  await page.waitForTimeout(1000);
  const pixelAfter = await sampleCanvasPixel(designerCanvas, 200, 180);

  expect(pixelAfter).not.toEqual(pixelBefore);

  await page.goto('/warehouse-optimize/view-2d');
  await expect(page.getByRole('heading', { name: /2d result/i })).toBeVisible();
  await expect(page.getByText(/no warehouse layout loaded/i)).not.toBeVisible({ timeout: 30000 });
  await expect(page.locator('svg.layout-svg')).toBeVisible({ timeout: 30000 });

  await page.goto('/warehouse-optimize/design');
  await page.getByRole('button', { name: /save layout/i }).click();
  await page.waitForTimeout(1500);

  await page.goto('/warehouse-optimize/slotting');
  await expect(page.getByRole('heading', { name: /slotting/i })).toBeVisible();
  await expect(page.locator('table').first()).toBeVisible();

  await page.goto('/warehouse-optimize/routes');
  await expect(page.getByRole('heading', { name: /routes/i })).toBeVisible();
  await expect(page.locator('.map-panel')).toBeVisible();

  await page.goto('/warehouse-optimize/analytics');
  await expect(page.getByRole('heading', { name: /analytics/i })).toBeVisible();
  await expect(page.locator('.summary-card').first()).toBeVisible({ timeout: 30000 });

  await page.getByRole('link', { name: /^3D Live$/i }).click();
  await expect(page).toHaveURL(/warehouse-optimize\/view-3d/, { timeout: 30000 });
  await expect(page.getByRole('heading', { name: /3d live/i })).toBeVisible();
  const syncButton = page.getByRole('button', { name: /sync stock data/i });
  await expect(syncButton).toBeEnabled({ timeout: 45000 });
  await syncButton.click();

  const liveWarehouse = page.locator('wms-warehouse-live-scene canvas');
  await expect(liveWarehouse).toBeVisible({ timeout: 45000 });
  await expect(page.locator('.status-card').filter({ hasText: 'Last sync' })).not.toContainText('Not synced', { timeout: 45000 });
});

test('admin can generate warehouse optimize draft from DB and view it before saving', async ({ page }) => {
  test.setTimeout(180000);

  await loginAsAdminAndEnterPreferredTenant(page);

  await page.goto('/warehouse-optimize/design');
  await expect(page.getByRole('heading', { name: /warehouse optimize/i })).toBeVisible();

  await page.route('**/api/v1/warehouse-optimize/layout/auto-generate', async route => {
    await new Promise(resolve => setTimeout(resolve, 750));
    await route.continue();
  });

  const quickGeneratePanel = page.locator('.panel').filter({ hasText: 'Quick Generate' });
  const generateFromDbButton = quickGeneratePanel.locator('.property-actions button').nth(1);
  await expect(generateFromDbButton).toBeVisible();

  page.once('dialog', dialog => void dialog.accept());
  const generateResponse = page.waitForResponse(response =>
    response.url().includes('/api/v1/warehouse-optimize/layout/auto-generate')
    && response.request().method() === 'POST'
    && response.ok()
  );

  await generateFromDbButton.click();
  await expect(page.locator('.save-state')).toContainText(/generating layout from db/i);
  await expect(generateFromDbButton).toBeDisabled();
  await expect(page.locator('.workspace-loading-overlay')).toContainText(/generating layout from db/i);
  await expect(page.locator('.workspace-loading-overlay')).toContainText(/designer is locked/i);
  await generateResponse;

  await expect(page.locator('.save-state')).toContainText(/unsaved changes/i, { timeout: 30000 });
  await expect(page.locator('.aisle-card').first()).toBeVisible({ timeout: 30000 });

  await page.goto('/warehouse-optimize/view-2d');
  await expect(page.getByRole('heading', { name: /2d result/i })).toBeVisible();
  await expect(page.locator('svg.layout-svg')).toBeVisible({ timeout: 30000 });

  await page.goto('/warehouse-optimize/view-3d');
  await expect(page.getByRole('heading', { name: /3d live/i })).toBeVisible();
  await expect(page.locator('wms-warehouse-live-scene canvas')).toBeVisible({ timeout: 45000 });
});

test('design and 2d views expose loading progress and collapsible side panels', async ({ page }) => {
  test.setTimeout(180000);

  await loginAsAdminAndEnterPreferredTenant(page);

  await page.route('**/api/v1/warehouse-optimize/profiles/*', async route => {
    await new Promise(resolve => setTimeout(resolve, 1200));
    await route.continue();
  });

  await page.goto('/warehouse-optimize/design');
  await expect(page.getByRole('heading', { name: /warehouse optimize/i })).toBeVisible();
  await expect(page.locator('.load-state')).toContainText(/loading profile/i);
  await expect(page.locator('.workspace-loading-overlay')).toContainText(/loading saved layout/i);
  await expect(page.locator('.workspace-loading-overlay')).not.toBeVisible({ timeout: 30000 });

  const designWorkspace = page.locator('.workspace-grid');
  const collapseDesignPanelsButton = page.getByRole('button', { name: /collapse design panels/i });
  await expect(collapseDesignPanelsButton).toBeVisible();
  await collapseDesignPanelsButton.click();
  await expect(designWorkspace).toHaveClass(/side-collapsed/);
  const collapsedDesignRail = page.locator('.collapsed-sidebar-rail');
  await expect(collapsedDesignRail).toBeVisible();
  await collapsedDesignRail.getByRole('button', { name: /expand design panels/i }).click();
  await expect(designWorkspace).not.toHaveClass(/side-collapsed/);

  await page.goto('/warehouse-optimize/view-2d');
  await expect(page.getByRole('heading', { name: /2d result/i })).toBeVisible();
  const view2dGrid = page.locator('.content-grid');
  await page.getByRole('button', { name: /collapse assignments panel/i }).click();
  await expect(view2dGrid).toHaveClass(/side-collapsed/);
  const collapsedAssignmentsRail = page.locator('.collapsed-rail').filter({ hasText: 'Assignments' });
  await expect(collapsedAssignmentsRail).toBeVisible();
  await collapsedAssignmentsRail.getByRole('button', { name: /expand assignments panel/i }).click();
  await expect(view2dGrid).not.toHaveClass(/side-collapsed/);
});

test('3d live shows occupied-empty colors and searchable stock details', async ({ page }) => {
  test.setTimeout(180000);

  await loginAsAdminAndEnterPreferredTenant(page);

  await page.goto('/warehouse-optimize/design');
  await expect(page.getByRole('heading', { name: /warehouse optimize/i })).toBeVisible();
  const quickGeneratePanel = page.locator('.panel').filter({ hasText: 'Quick Generate' });
  const generateFromDbButton = quickGeneratePanel.locator('.property-actions button').nth(1);
  page.once('dialog', dialog => void dialog.accept());
  await generateFromDbButton.click();
  await expect(page.locator('.save-state')).toContainText(/unsaved changes/i, { timeout: 30000 });
  await page.getByRole('button', { name: /save layout/i }).click();
  await expect(page.locator('.save-state')).toContainText(/saved/i, { timeout: 30000 });

  await page.goto('/warehouse-optimize/view-3d');
  await expect(page.getByRole('heading', { name: /3d live/i })).toBeVisible();

  const customerDropdown = page.locator('.field').filter({ hasText: 'Customer filter' });
  await customerDropdown.locator('.p-dropdown').click();
  const preferredCustomer = page.getByRole('option', { name: /TESA/i });
  if (await preferredCustomer.count()) {
    await preferredCustomer.first().click();
  } else {
    await page.getByRole('option').first().click();
  }

  await page.route('**/api/v1/warehouse-optimize/viewer/*/sync-stock', async route => {
    await new Promise(resolve => setTimeout(resolve, 2000));
    await route.continue();
  });

  const syncButton = page.locator('wms-page-header button.p-button-sm').first();
  await syncButton.click();
  await expect(syncButton).toBeDisabled();
  await expect(page.locator('.sync-state')).toContainText(/syncing stock data/i);
  await expect(page.locator('.scene-loading-overlay')).toContainText(/syncing stock data/i);

  const liveCanvas = page.locator('wms-warehouse-live-scene canvas');
  await expect(liveCanvas).toBeVisible({ timeout: 45000 });
  const sceneLegend = page.locator('wms-warehouse-live-scene .scene-legend');
  await expect(sceneLegend.getByText(/^Occupied$/)).toBeVisible();
  await expect(sceneLegend.getByText(/^Empty$/)).toBeVisible();
  await expect(sceneLegend.getByText(/^Search result$/)).toBeVisible();
  await expect(page.locator('.scene-panel .panel-head-actions')).not.toContainText(/^0 mapped locations$/, { timeout: 45000 });
  await expect(page.locator('.sync-state')).toContainText(/synced .* occupied locations/i, { timeout: 45000 });

  const searchInput = page.getByPlaceholder(/location or product code/i);
  await expect(searchInput).toBeVisible();
  await searchInput.fill('1');

  const searchPanel = page.locator('.panel').filter({ hasText: 'Search Matches' });
  await expect(searchPanel).toBeVisible();
  const firstSearchResult = searchPanel.locator('.search-result-button').first();
  await expect(firstSearchResult).toBeVisible({ timeout: 15000 });
  const highlightedLocation = (await firstSearchResult.locator('.search-result-location').textContent())?.trim();
  expect(highlightedLocation).toBeTruthy();
  await firstSearchResult.click();

  const tooltip = page.locator('.scene-tooltip');
  await expect(tooltip).toBeVisible({ timeout: 15000 });
  await expect(tooltip).toContainText(/status/i);
  await expect(tooltip).toContainText(/physical qty/i);
  await expect(tooltip).toContainText(/available qty/i);

  const hoveredLocation = (await tooltip.locator('.scene-tooltip-title').textContent())?.trim();
  expect(hoveredLocation).toBe(highlightedLocation);
  await expect(page.locator('.status-card').filter({ hasText: 'Search Results' })).not.toContainText(/no filter/i);
});

test('working-status zones drawn in design render live badges in 3d', async ({ page }) => {
  test.setTimeout(180000);

  await loginAsAdminAndEnterPreferredTenant(page);

  await page.goto('/warehouse-optimize/design');
  await expect(page.getByRole('heading', { name: /warehouse optimize/i })).toBeVisible();

  const designerCanvas = page.locator('#designerCanvas');
  page.once('dialog', dialog => void dialog.accept());
  await page.getByRole('button', { name: /new blank/i }).click();
  await page.getByRole('button', { name: /draw/i }).click();
  await designerCanvas.scrollIntoViewIfNeeded();
  await expect(designerCanvas).toBeVisible({ timeout: 30000 });

  const canvasBox = await designerCanvas.boundingBox();
  expect(canvasBox).not.toBeNull();
  if (!canvasBox) {
    throw new Error('Designer canvas is not available for working-status drawing.');
  }

  await page.mouse.move(canvasBox.x + 190, canvasBox.y + 130);
  await page.mouse.down();
  await page.mouse.move(canvasBox.x + 360, canvasBox.y + 250, { steps: 12 });
  await page.mouse.up();
  await page.waitForTimeout(1000);

  const firstAisleCard = page.locator('.aisle-card').first();
  await expect(firstAisleCard).toBeVisible({ timeout: 30000 });
  await firstAisleCard.click();

  const propertiesPanel = page.locator('.panel').filter({ hasText: 'Properties' });
  await expect(propertiesPanel).toBeVisible();

  await propertiesPanel.locator('label.field').filter({ hasText: 'Zone name' }).locator('input').fill('Picking Zone');
  await propertiesPanel.locator('label.field').filter({ hasText: 'Type' }).locator('select').selectOption({ label: 'Working Status' });
  await propertiesPanel.locator('label.field').filter({ hasText: 'Operation flow' }).locator('select').selectOption({ label: 'Outbound' });
  await propertiesPanel.locator('label.field').filter({ hasText: 'System status' }).locator('select').selectOption({ label: 'Picking' });
  await propertiesPanel.getByRole('button', { name: /apply/i }).click();

  await expect(page.locator('.save-state')).toContainText(/unsaved changes/i, { timeout: 30000 });
  await expect(propertiesPanel.locator('.status-preview-card')).toContainText(/picking/i);

  await page.route('**/api/v1/warehouse-optimize/viewer/working-status', async route => {
    await route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify({
        status: 'SUCCESS',
        data: {
          generatedAt: '2026-06-12T12:00:00',
          statuses: [
            {
              flow: 'OUTBOUND',
              statusCode: '500',
              statusLabel: 'Picking',
              color: '#FF9E1B',
              orderCount: 23,
              totalOrders: 231,
              csQty: 45,
              pcsQty: 678
            }
          ]
        },
        totalRecords: 1,
        page: 0,
        size: 1,
        messages: []
      })
    });
  });

  await page.goto('/warehouse-optimize/view-3d');
  await expect(page.getByRole('heading', { name: /3d live/i })).toBeVisible();

  const customerDropdown = page.locator('.field').filter({ hasText: 'Customer filter' });
  await customerDropdown.locator('.p-dropdown').click();
  const preferredCustomer = page.getByRole('option', { name: /TESA/i });
  if (await preferredCustomer.count()) {
    await preferredCustomer.first().click();
  } else {
    await page.getByRole('option').first().click();
  }

  await expect(page.locator('.scene-state')).toContainText(/sync stock to start workflow status/i, { timeout: 30000 });

  await page.route('**/api/v1/warehouse-optimize/viewer/*/sync-stock', async route => {
    await route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify({
        status: 'SUCCESS',
        data: {
          profileId: 1,
          customerCode: 'TESA',
          syncedAt: '2026-06-12T12:00:00',
          cursor: '2026-06-12T12:00:00',
          locations: [],
          diagnostics: {
            unmatchedLayoutLocations: [],
            unmatchedStockLocations: []
          }
        },
        totalRecords: 1,
        page: 0,
        size: 1,
        messages: []
      })
    });
  });

  await page.getByRole('button', { name: /sync stock data/i }).click();
  await expect(page.locator('.scene-state')).toContainText(/workflow/i, { timeout: 30000 });

  const statusBadge = page.locator('wms-warehouse-live-scene .working-status-badge').filter({ hasText: 'Picking Zone' });
  await expect(statusBadge).toBeVisible({ timeout: 45000 });
  await expect(statusBadge).toContainText('OUTBOUND Picking');
  await expect(statusBadge).toContainText('23/231');
  await expect(statusBadge).toContainText('CS 45 | PCS 678');
});
