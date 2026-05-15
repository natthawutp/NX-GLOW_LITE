export const environment = {
  production: true,
  apiUrl: '/gwh-modern/api/v1',
  defaultLocale: 'ja',
  supportedLocales: ['en', 'ja', 'th'],
  jwtTokenKey: 'wms_access_token',
  jwtRefreshKey: 'wms_refresh_token',
  tokenExpiryKey: 'wms_token_expiry',
  appName: 'GWH Modern WMS',
  appVersion: '1.0.0',
  dateFormat: 'yyyy-MM-dd',
  dateTimeFormat: 'yyyy-MM-dd HH:mm:ss',
  defaultPageSize: 20,
  maxPageSize: 200,
  toastLife: 4000,
  sessionTimeoutMinutes: 30,
  enableDebugLog: false
};
