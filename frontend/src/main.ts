import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';

bootstrapApplication(AppComponent, appConfig)
  .then(() => {
    // Remove loading screen
    const loader = document.getElementById('app-loading');
    if (loader) {
      loader.style.opacity = '0';
      loader.style.transition = 'opacity 0.3s ease';
      setTimeout(() => loader.remove(), 300);
    }
  })
  .catch(err => console.error('Application bootstrap failed:', err));
