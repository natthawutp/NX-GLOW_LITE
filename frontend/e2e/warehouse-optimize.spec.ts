import { expect, test, type Locator } from '@playwright/test';

async function sampleCanvasPixel(canvas: Locator, x: number, y: number): Promise<number[]> {
  return canvas.evaluate((element, point) => {
    const context = (element as HTMLCanvasElement).getContext('2d');
    if (!context) {
      throw new Error('Canvas 2D context is not available.');
    }
    return Array.from(context.getImageData(point.x, point.y, 1, 1).data);
  }, { x, y });
}

test('admin can enter BKK/TESA and use Warehouse Optimize screens', async ({ page }) => {
  test.setTimeout(180000);

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
  await page.getByRole('button', { name: /save layout/i }).click();
  await page.waitForTimeout(1500);

  await page.goto('/warehouse-optimize/view-2d');
  await expect(page.getByRole('heading', { name: /2d result/i })).toBeVisible();
  await expect(page.getByText(/no warehouse layout loaded/i)).not.toBeVisible({ timeout: 30000 });
  await expect(page.locator('svg.layout-svg')).toBeVisible({ timeout: 30000 });

  await page.goto('/warehouse-optimize/slotting');
  await expect(page.getByRole('heading', { name: /slotting/i })).toBeVisible();
  await expect(page.locator('table').first()).toBeVisible();

  await page.goto('/warehouse-optimize/routes');
  await expect(page.getByRole('heading', { name: /routes/i })).toBeVisible();
  await expect(page.locator('.map-panel')).toBeVisible();

  await page.goto('/warehouse-optimize/analytics');
  await expect(page.getByRole('heading', { name: /analytics/i })).toBeVisible();
  await expect(page.locator('.summary-card').first()).toBeVisible({ timeout: 30000 });

  await page.goto('/warehouse-optimize/view-3d');
  await expect(page.getByRole('heading', { name: /3d live/i })).toBeVisible();
  const syncButton = page.getByRole('button', { name: /sync stock data/i });
  await expect(syncButton).toBeEnabled({ timeout: 45000 });
  await syncButton.click();

  const liveWarehouse = page.locator('wms-warehouse-live-scene canvas');
  await expect(liveWarehouse).toBeVisible({ timeout: 45000 });
  await expect(page.locator('.status-card').filter({ hasText: 'Last sync' })).not.toContainText('Not synced', { timeout: 45000 });
});
