import { Component, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { MessageModule } from 'primeng/message';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { AuthService } from '@core/services/auth.service';
import { LoginRequest } from '@core/models/auth.model';
import { environment } from '@env/environment';

@Component({
  selector: 'wms-login',
  standalone: true,
  imports: [
    CommonModule, FormsModule, TranslateModule,
    InputTextModule, PasswordModule, ButtonModule,
    CheckboxModule, MessageModule, ProgressSpinnerModule
  ],
  template: `
    <div class="login-page">
      <!-- Left: Branding Panel -->
      <div class="login-branding">
        <div class="branding-content">
          <div class="brand-logo">
            <svg width="56" height="56" viewBox="0 0 56 56" fill="none">
              <rect width="56" height="56" rx="14" fill="white" fill-opacity="0.15"/>
              <path d="M14 21L28 14L42 21V35L28 42L14 35V21Z" stroke="#8EC400" stroke-width="2.5" fill="none"/>
              <path d="M28 14V42M14 21L42 35M42 21L14 35" stroke="#8EC400" stroke-width="1.5" stroke-opacity="0.5"/>
              <circle cx="28" cy="28" r="5" fill="#8EC400"/>
            </svg>
          </div>
          <div class="brand-name-row">
            <h1 class="brand-title">{{ appNamePrimary }}</h1>
            <h3 class="brand-title brand-title-accent" *ngIf="appNameAccent">{{ appNameAccent }}</h3>
          </div>
          <p class="brand-subtitle">{{ appSubtitle }}</p>

          <div class="brand-features">
            <div class="feature-item">
              <div class="feature-icon">
                <i class="pi pi-check-circle"></i>
              </div>
              <div class="feature-text">
                <strong>Real-time Inventory</strong>
                <span>Track stock levels across all warehouses instantly</span>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <i class="pi pi-check-circle"></i>
              </div>
              <div class="feature-text">
                <strong>Smart Operations</strong>
                <span>Streamlined inbound, outbound & picking workflows</span>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <i class="pi pi-check-circle"></i>
              </div>
              <div class="feature-text">
                <strong>Analytics & Reports</strong>
                <span>Comprehensive dashboards and business insights</span>
              </div>
            </div>
          </div>
        </div>

        <div class="branding-footer">
          <span>&copy; {{ currentYear }} {{ companyName }}</span>
        </div>

        <!-- Background decorations -->
        <div class="bg-circle bg-circle-1"></div>
        <div class="bg-circle bg-circle-2"></div>
        <div class="bg-grid"></div>
      </div>

      <!-- Right: Login Form -->
      <div class="login-form-panel">
        <div class="form-container">
          <!-- Language selector -->
          <div class="lang-row">
            @for (lang of languages; track lang.code) {
              <button class="lang-btn" [class.active]="selectedLocale === lang.code"
                      (click)="switchLang(lang.code)">
                <span class="lang-flag">{{ lang.flag }}</span>
                <span>{{ lang.code | uppercase }}</span>
              </button>
            }
          </div>

          <div class="form-header">
            <h2>{{ 'Welcome' | translate }}</h2>
            <p>{{ 'login.sign_in_subtitle' | translate }}</p>
          </div>

          <!-- Error message -->
          <p-message *ngIf="errorMessage()"
                     severity="error"
                     [text]="errorMessage()"
                     [style]="{ width: '100%', 'margin-bottom': '16px' }">
          </p-message>

          <form (ngSubmit)="onLogin()" class="login-form">
            <!-- Email -->
            <div class="form-field">
              <label>{{ 'login.email' | translate }}</label>
              <span class="p-input-icon-left email-input-shell">
                <i class="pi pi-envelope"></i>
                <input pInputText
                       type="email"
                       [(ngModel)]="loginData.email"
                       name="email"
                       [placeholder]="'login.email_placeholder' | translate"
                       class="w-full email-input"
                       autocomplete="email"
                       required>
              </span>
            </div>

            <!-- Password -->
            <div class="form-field">
              <label>{{ 'login.password' | translate }}</label>
              <p-password [(ngModel)]="loginData.password"
                          name="password"
                          [placeholder]="'login.password_placeholder' | translate"
                          [toggleMask]="true"
                          [feedback]="false"
                          [style]="{ width: '100%' }"
                          [inputStyle]="{ width: '100%' }"
                          autocomplete="current-password"
                          required>
              </p-password>
            </div>

            <!-- Remember me -->
            <div class="form-actions-row">
              <div class="remember-me">
                <p-checkbox [(ngModel)]="rememberMe"
                            name="rememberMe"
                            [binary]="true"
                            label="{{ 'login.remember_me' | translate }}">
                </p-checkbox>
              </div>
            </div>

            <!-- Submit -->
            <button pButton
                    type="submit"
                    [label]="'login.sign_in' | translate"
                    icon="pi pi-sign-in"
                    class="login-btn"
                    [loading]="isLoading()"
                    [disabled]="isLoading()">
            </button>
          </form>

          <div class="form-footer">
            <span class="powered-by">Powered by {{ appName }} v{{ appVersion }}</span>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .login-page {
      display: flex;
      min-height: 100vh;
    }

    /* === Branding Panel === */
    .login-branding {
      flex: 0 0 480px;
      background: linear-gradient(135deg, #0f0035 0%, #1A005D 50%, #2d0080 100%);
      color: white;
      display: flex;
      flex-direction: column;
      justify-content: center;
      padding: 48px;
      position: relative;
      overflow: hidden;
    }

    .branding-content {
      position: relative;
      z-index: 2;
    }

    .brand-name-row {
      display: flex;
      flex-wrap: wrap;
      align-items: baseline;
      gap: 10px;
      margin-bottom: 8px;
    }

    .brand-logo {
      margin-bottom: 24px;
    }

    .brand-title {
      font-size: 32px;
      font-weight: 800;
      letter-spacing: -0.5px;
      margin: 0;
      line-height: 1.1;
    }

    .brand-title-accent {
      color: #8EC400;
    }

    .brand-subtitle {
      font-size: 15px;
      color: rgba(255, 255, 255, 0.6);
      margin: 0 0 48px;
    }

    .brand-features {
      display: flex;
      flex-direction: column;
      gap: 24px;
    }

    .feature-item {
      display: flex;
      gap: 14px;
      align-items: flex-start;
    }

    .feature-icon {
      width: 36px;
      height: 36px;
      border-radius: 10px;
      background: rgba(142, 196, 0, 0.18);
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
    }

    .feature-icon i {
      color: #8EC400;
      font-size: 17px;
    }

    .feature-text {
      display: flex;
      flex-direction: column;
      gap: 2px;
    }

    .feature-text strong {
      font-size: 14px;
      font-weight: 600;
    }

    .feature-text span {
      font-size: 12.5px;
      color: rgba(255, 255, 255, 0.5);
      line-height: 1.4;
    }

    .branding-footer {
      position: absolute;
      bottom: 24px;
      left: 48px;
      font-size: 12px;
      color: rgba(255, 255, 255, 0.3);
      z-index: 2;
    }

    .bg-circle {
      position: absolute;
      border-radius: 50%;
      border: 1px solid rgba(255, 255, 255, 0.05);
    }

    .bg-circle-1 {
      width: 400px;
      height: 400px;
      top: -100px;
      right: -120px;
    }

    .bg-circle-2 {
      width: 300px;
      height: 300px;
      bottom: -60px;
      left: -80px;
    }

    .bg-grid {
      position: absolute;
      inset: 0;
      background-image:
        linear-gradient(rgba(255,255,255,0.03) 1px, transparent 1px),
        linear-gradient(90deg, rgba(255,255,255,0.03) 1px, transparent 1px);
      background-size: 40px 40px;
      z-index: 1;
    }

    /* === Form Panel === */
    .login-form-panel {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f8f9fc;
      padding: 32px;
    }

    .form-container {
      width: 100%;
      max-width: 440px;
    }

    .lang-row {
      display: flex;
      justify-content: flex-end;
      gap: 4px;
      margin-bottom: 32px;
    }

    .lang-btn {
      display: flex;
      align-items: center;
      gap: 5px;
      padding: 6px 10px;
      border: 1px solid transparent;
      background: transparent;
      border-radius: 6px;
      cursor: pointer;
      font-size: 12px;
      font-weight: 500;
      color: #6b7280;
      transition: all 0.15s;
    }

    .lang-btn:hover {
      background: white;
      border-color: #e5e7eb;
    }

    .lang-btn.active {
      background: white;
      border-color: #1A005D;
      color: #1A005D;
      font-weight: 600;
      box-shadow: 0 1px 3px rgba(26, 0, 93, 0.1);
    }

    .lang-flag {
      font-size: 16px;
    }

    .form-header {
      margin-bottom: 28px;
    }

    .form-header h2 {
      font-size: 26px;
      font-weight: 800;
      color: #111827;
      margin: 0 0 6px;
      letter-spacing: -0.3px;
    }

    .form-header p {
      font-size: 14px;
      color: #6b7280;
      margin: 0;
    }

    .login-form {
      display: flex;
      flex-direction: column;
      gap: 18px;
    }

    .form-field {
      display: flex;
      flex-direction: column;
      gap: 6px;
    }

    .form-field label {
      font-size: 13px;
      font-weight: 600;
      color: #374151;
    }

    .email-input-shell {
      display: block;
      width: 100%;
      position: relative;
    }

    .email-input {
      padding-left: 2.75rem !important;
    }

    :host ::ng-deep .email-input-shell > i {
      left: 0.95rem;
      color: #9ca3af;
      z-index: 1;
      pointer-events: none;
    }

    .form-actions-row {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .remember-me {
      font-size: 13px;
    }

    .login-btn {
      width: 100%;
      height: 46px;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      font-size: 15px;
      font-weight: 600;
      padding: 0 1.35rem !important;
      border-radius: 10px !important;
      background: linear-gradient(135deg, #1A005D, #2d0080) !important;
      border: none !important;
      color: #ffffff !important;
      margin-top: 4px;
      transition: transform 0.15s, box-shadow 0.15s !important;
    }

    :host ::ng-deep .login-btn .p-button-icon {
      margin-right: 0.55rem;
    }

    .login-btn:hover:not(:disabled) {
      transform: translateY(-1px);
      box-shadow: 0 6px 20px rgba(26, 0, 93, 0.35) !important;
    }

    .login-btn:active:not(:disabled) {
      transform: translateY(0);
    }

    .form-footer {
      text-align: center;
      margin-top: 32px;
    }

    .powered-by {
      font-size: 12px;
      color: #9ca3af;
    }

    .w-full {
      width: 100%;
    }

    /* Responsive */
    @media (max-width: 1024px) {
      .login-branding {
        display: none;
      }
    }

    @media (max-width: 480px) {
      .login-form-panel {
        padding: 20px;
      }

      .form-header h2 {
        font-size: 22px;
      }
    }
  `]
})
export class LoginComponent implements OnInit {
  readonly appName = environment.appName;
  readonly appNamePrimary = environment.appNamePrimary;
  readonly appNameAccent = environment.appNameAccent;
  readonly appSubtitle = environment.appSubtitle;
  readonly companyName = environment.companyName;
  readonly appVersion = environment.appVersion;
  isLoading = signal(false);
  errorMessage = signal('');

  loginData: LoginRequest = {
    email: '',
    password: '',
    locale: 'en'
  };

  rememberMe = false;
  selectedLocale = environment.defaultLocale;
  currentYear = new Date().getFullYear();

  languages = [
    { code: 'en', flag: '🇺🇸' },
    { code: 'ja', flag: '🇯🇵' },
    { code: 'th', flag: '🇹🇭' }
  ];

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    // If already fully authenticated with tenant, redirect to dashboard
    if (this.authService.isAuthenticated() && this.authService.hasTenantSelected()) {
      this.router.navigate(['/dashboard']);
      return;
    }
    this.selectedLocale = localStorage.getItem('wms_locale') || environment.defaultLocale;

    // Load remembered email
    const savedEmail = localStorage.getItem('wms_remembered_email');
    if (savedEmail) {
      this.loginData.email = savedEmail;
      this.rememberMe = true;
    }
  }

  switchLang(lang: string): void {
    this.selectedLocale = lang;
    this.loginData.locale = lang;
    this.translate.use(lang);
    localStorage.setItem('wms_locale', lang);
  }

  onLogin(): void {
    if (!this.loginData.email || !this.loginData.password) {
      this.errorMessage.set('Please enter email and password');
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set('');
    this.loginData.locale = this.selectedLocale;

    this.authService.login(this.loginData).subscribe({
      next: (response) => {
        this.isLoading.set(false);
        if (response.status === 'SUCCESS') {
          if (this.rememberMe) {
            localStorage.setItem('wms_remembered_email', this.loginData.email);
          } else {
            localStorage.removeItem('wms_remembered_email');
          }
          // Navigate to tenant selection page (step 2)
          this.router.navigate(['/select-tenant']);
        } else {
          const msg = response.messages?.[0]?.message || 'Login failed';
          this.errorMessage.set(msg);
        }
      },
      error: (err) => {
        this.isLoading.set(false);
        if (err.status === 401) {
          this.errorMessage.set('Invalid email or password');
        } else if (err.status === 0) {
          this.errorMessage.set('Unable to connect to server');
        } else {
          this.errorMessage.set(err.error?.messages?.[0]?.message || 'An error occurred');
        }
      }
    });
  }
}
