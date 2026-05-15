import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError, switchMap } from 'rxjs';
import { AuthService } from '@core/services/auth.service';
import { MessageService } from 'primeng/api';

let isRefreshing = false;

export const errorInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn) => {
  const authService = inject(AuthService);
  const messageService = inject(MessageService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      // Handle 401 - try token refresh
      if (error.status === 401 && !req.url.includes('/auth/')) {
        if (!isRefreshing) {
          isRefreshing = true;
          return authService.refreshToken().pipe(
            switchMap(() => {
              isRefreshing = false;
              const token = authService.getAccessToken();
              const retryReq = req.clone({
                setHeaders: { Authorization: `Bearer ${token}` }
              });
              return next(retryReq);
            }),
            catchError(refreshErr => {
              isRefreshing = false;
              authService.logout();
              return throwError(() => refreshErr);
            })
          );
        }
      }

      // Handle 403
      if (error.status === 403) {
        messageService.add({
          severity: 'error',
          summary: 'Access Denied',
          detail: 'You do not have permission to perform this action.',
          life: 5000
        });
      }

      // Handle 409 - optimistic lock
      if (error.status === 409) {
        messageService.add({
          severity: 'warn',
          summary: 'Data Changed',
          detail: 'This record was modified by another user. Please refresh and try again.',
          life: 6000
        });
      }

      // Handle 422 - business logic error
      if (error.status === 422) {
        const messages = error.error?.messages;
        if (messages && messages.length > 0) {
          messages.forEach((msg: any) => {
            messageService.add({
              severity: 'warn',
              summary: 'Validation',
              detail: msg.message || msg.code,
              life: 5000
            });
          });
        }
      }

      // Handle 500
      if (error.status >= 500) {
        messageService.add({
          severity: 'error',
          summary: 'System Error',
          detail: 'An unexpected error occurred. Please try again later.',
          life: 5000
        });
      }

      // Handle network errors
      if (error.status === 0) {
        messageService.add({
          severity: 'error',
          summary: 'Connection Error',
          detail: 'Unable to connect to the server. Please check your network.',
          life: 5000
        });
      }

      return throwError(() => error);
    })
  );
};
