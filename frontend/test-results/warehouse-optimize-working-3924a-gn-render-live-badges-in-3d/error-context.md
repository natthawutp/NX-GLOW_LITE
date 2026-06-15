# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: warehouse-optimize.spec.ts >> working-status zones drawn in design render live badges in 3d
- Location: e2e\warehouse-optimize.spec.ts:251:5

# Error details

```
Error: expect(locator).toContainText(expected) failed

Locator: locator('.scene-state')
Expected pattern: /workflow/i
Received string:  " No working-status zones "
Timeout: 30000ms

Call log:
  - Expect "toContainText" with timeout 30000ms
  - waiting for locator('.scene-state')
    63 × locator resolved to <span _ngcontent-ng-c2000214895="" class="scene-state ng-star-inserted">…</span>
       - unexpected value " No working-status zones "

```

```yaml
- text: No working-status zones
```

# Test source

```ts
  235 |   await expect(firstSearchResult).toBeVisible({ timeout: 15000 });
  236 |   const highlightedLocation = (await firstSearchResult.locator('.search-result-location').textContent())?.trim();
  237 |   expect(highlightedLocation).toBeTruthy();
  238 |   await firstSearchResult.click();
  239 | 
  240 |   const tooltip = page.locator('.scene-tooltip');
  241 |   await expect(tooltip).toBeVisible({ timeout: 15000 });
  242 |   await expect(tooltip).toContainText(/status/i);
  243 |   await expect(tooltip).toContainText(/physical qty/i);
  244 |   await expect(tooltip).toContainText(/available qty/i);
  245 | 
  246 |   const hoveredLocation = (await tooltip.locator('.scene-tooltip-title').textContent())?.trim();
  247 |   expect(hoveredLocation).toBe(highlightedLocation);
  248 |   await expect(page.locator('.status-card').filter({ hasText: 'Search Results' })).not.toContainText(/no filter/i);
  249 | });
  250 | 
  251 | test('working-status zones drawn in design render live badges in 3d', async ({ page }) => {
  252 |   test.setTimeout(180000);
  253 | 
  254 |   await loginAsAdminAndEnterPreferredTenant(page);
  255 | 
  256 |   await page.goto('/warehouse-optimize/design');
  257 |   await expect(page.getByRole('heading', { name: /warehouse optimize/i })).toBeVisible();
  258 | 
  259 |   const designerCanvas = page.locator('#designerCanvas');
  260 |   page.once('dialog', dialog => void dialog.accept());
  261 |   await page.getByRole('button', { name: /new blank/i }).click();
  262 |   await page.getByRole('button', { name: /draw/i }).click();
  263 |   await designerCanvas.scrollIntoViewIfNeeded();
  264 |   await expect(designerCanvas).toBeVisible({ timeout: 30000 });
  265 | 
  266 |   const canvasBox = await designerCanvas.boundingBox();
  267 |   expect(canvasBox).not.toBeNull();
  268 |   if (!canvasBox) {
  269 |     throw new Error('Designer canvas is not available for working-status drawing.');
  270 |   }
  271 | 
  272 |   await page.mouse.move(canvasBox.x + 190, canvasBox.y + 130);
  273 |   await page.mouse.down();
  274 |   await page.mouse.move(canvasBox.x + 360, canvasBox.y + 250, { steps: 12 });
  275 |   await page.mouse.up();
  276 |   await page.waitForTimeout(1000);
  277 | 
  278 |   const firstAisleCard = page.locator('.aisle-card').first();
  279 |   await expect(firstAisleCard).toBeVisible({ timeout: 30000 });
  280 |   await firstAisleCard.click();
  281 | 
  282 |   const propertiesPanel = page.locator('.panel').filter({ hasText: 'Properties' });
  283 |   await expect(propertiesPanel).toBeVisible();
  284 | 
  285 |   await propertiesPanel.locator('label.field').filter({ hasText: 'Zone name' }).locator('input').fill('Picking Zone');
  286 |   await propertiesPanel.locator('label.field').filter({ hasText: 'Type' }).locator('select').selectOption({ label: 'Working Status' });
  287 |   await propertiesPanel.locator('label.field').filter({ hasText: 'Operation flow' }).locator('select').selectOption({ label: 'Outbound' });
  288 |   await propertiesPanel.locator('label.field').filter({ hasText: 'System status' }).locator('select').selectOption({ label: 'Picking' });
  289 |   await propertiesPanel.getByRole('button', { name: /apply/i }).click();
  290 | 
  291 |   await expect(page.locator('.save-state')).toContainText(/unsaved changes/i, { timeout: 30000 });
  292 |   await expect(propertiesPanel.locator('.status-preview-card')).toContainText(/picking/i);
  293 | 
  294 |   await page.route('**/api/v1/warehouse-optimize/viewer/working-status', async route => {
  295 |     await route.fulfill({
  296 |       status: 200,
  297 |       contentType: 'application/json',
  298 |       body: JSON.stringify({
  299 |         status: 'SUCCESS',
  300 |         data: {
  301 |           generatedAt: '2026-06-12T12:00:00',
  302 |           statuses: [
  303 |             {
  304 |               flow: 'OUTBOUND',
  305 |               statusCode: '500',
  306 |               statusLabel: 'Picking',
  307 |               color: '#FF9E1B',
  308 |               orderCount: 23,
  309 |               totalOrders: 231,
  310 |               csQty: 45,
  311 |               pcsQty: 678
  312 |             }
  313 |           ]
  314 |         },
  315 |         totalRecords: 1,
  316 |         page: 0,
  317 |         size: 1,
  318 |         messages: []
  319 |       })
  320 |     });
  321 |   });
  322 | 
  323 |   await page.goto('/warehouse-optimize/view-3d');
  324 |   await expect(page.getByRole('heading', { name: /3d live/i })).toBeVisible();
  325 | 
  326 |   const customerDropdown = page.locator('.field').filter({ hasText: 'Customer filter' });
  327 |   await customerDropdown.locator('.p-dropdown').click();
  328 |   const preferredCustomer = page.getByRole('option', { name: /TESA/i });
  329 |   if (await preferredCustomer.count()) {
  330 |     await preferredCustomer.first().click();
  331 |   } else {
  332 |     await page.getByRole('option').first().click();
  333 |   }
  334 | 
> 335 |   await expect(page.locator('.scene-state')).toContainText(/workflow/i, { timeout: 30000 });
      |                                              ^ Error: expect(locator).toContainText(expected) failed
  336 | 
  337 |   const statusBadge = page.locator('wms-warehouse-live-scene .working-status-badge').filter({ hasText: 'Picking Zone' });
  338 |   await expect(statusBadge).toBeVisible({ timeout: 45000 });
  339 |   await expect(statusBadge).toContainText('OUTBOUND Picking');
  340 |   await expect(statusBadge).toContainText('23/231');
  341 |   await expect(statusBadge).toContainText('CS 45 | PCS 678');
  342 | });
  343 | 
```