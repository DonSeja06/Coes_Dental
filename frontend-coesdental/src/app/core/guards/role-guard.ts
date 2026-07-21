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

  const expectedRoles = route.data?.['expectedRoles'] as Array<string>;

  if (expectedRoles && !expectedRoles.includes(rol || '')) {
    if (rol === 'ROLE_Odontologo') {
      router.navigate(['/layout/mis-citas']);
    } else if (rol === 'ROLE_Recepcionista') {
      router.navigate(['/layout/calendario']);
    } else if (rol === 'ROLE_Paciente') {
      router.navigate(['/layout/mis-citas-paciente']);
    } else {
      router.navigate(['/layout/dashboard']);
    }
    return false;
  }

  return true;
};