import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const roleGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  const token = localStorage.getItem('token');
  const rol = localStorage.getItem('rol');

  if (!token) {
    router.navigate(['/login']);
    return false;
  }

  const rolEsperado = route.data?.['expectedRole'];

  if (rolEsperado && rolEsperado !== rol) {
    if (rol === 'ROLE_Odontologo') {
      router.navigate(['/layout/mis-citas']);
    } else {
      router.navigate(['/layout/dashboard']);
    }
    return false;
  }

  return true;
};