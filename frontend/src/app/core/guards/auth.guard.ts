import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '@core/services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    router.navigate(['/login'], {
      queryParams: { returnUrl: state.url }
    });
    return false;
  }

  // Authenticated but hasn't selected tenant yet → redirect to tenant selection
  if (!authService.hasTenantSelected()) {
    router.navigate(['/select-tenant']);
    return false;
  }

  return true;
};

/**
 * Guard for /select-tenant route: user must be authenticated but NOT yet have a tenant.
 */
export const tenantGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  // Already selected tenant → go to dashboard
  if (authService.hasTenantSelected()) {
    router.navigate(['/dashboard']);
    return false;
  }

  return true;
};

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  const requiredRoles = route.data?.['roles'] as string[];
  if (requiredRoles && !authService.hasAnyRole(requiredRoles)) {
    router.navigate(['/dashboard']);
    return false;
  }

  return true;
};
