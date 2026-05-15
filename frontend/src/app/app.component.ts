import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { TranslateService } from '@ngx-translate/core';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { environment } from '@env/environment';

@Component({
  selector: 'wms-root',
  standalone: true,
  imports: [RouterOutlet, ToastModule, ConfirmDialogModule],
  template: `
    <p-toast position="top-right" [life]="toastLife"></p-toast>
    <p-confirmDialog
      [style]="{ width: '420px' }"
      header="Confirmation"
      icon="pi pi-exclamation-triangle"
      acceptButtonStyleClass="p-button-sm"
      rejectButtonStyleClass="p-button-sm p-button-outlined">
    </p-confirmDialog>
    <router-outlet></router-outlet>
  `,
  styles: [`
    :host {
      display: block;
      min-height: 100vh;
    }
  `]
})
export class AppComponent implements OnInit {
  toastLife = environment.toastLife;

  constructor(
    private translate: TranslateService,
    private title: Title
  ) {}

  ngOnInit(): void {
    this.title.setTitle(environment.appName);
    const savedLocale = localStorage.getItem('wms_locale') || environment.defaultLocale;
    this.translate.addLangs(environment.supportedLocales);
    this.translate.setDefaultLang(environment.defaultLocale);
    this.translate.use(savedLocale);
  }
}
