import { defineConfig } from '@playwright/test';

export default defineConfig({
  testDir: './e2e',
  fullyParallel: false,
  retries: 0,
  use: {
    baseURL: 'http://localhost:4200',
    trace: 'retain-on-failure',
    screenshot: 'only-on-failure',
    video: 'retain-on-failure'
  },
  webServer: {
    command: 'npm start -- --host localhost --port 4200',
    url: 'http://localhost:4200/login',
    reuseExistingServer: true,
    timeout: 180000
  },
  reporter: [['list']]
});
