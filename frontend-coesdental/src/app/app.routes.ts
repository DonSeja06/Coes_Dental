import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { LayoutComponent } from './components/layout/layout';
import { DashboardComponent } from './components/dashboard/dashboard';
import { Pacientes } from './components/pacientes/pacientes';
import { Odontologos } from './components/odontologos/odontologos';
import { Consultorios } from './components/consultorios/consultorios';
import { Citas } from './components/citas/citas';
import { Calendario } from './components/calendario/calendario';
import { MisCitas } from './components/mis-citas/mis-citas';
import { MisPacientes } from './components/mis-pacientes/mis-pacientes';
import { Recepcionistas } from './components/recepcionistas/recepcionistas';
import { MisCitasPaciente } from './components/mis-citas-paciente/mis-citas-paciente';
import { Pagos } from './components/pagos/pagos';

import { roleGuard } from './core/guards/role-guard';
export const routes: Routes = [
    { path: 'login', component: LoginComponent },

    {
        path: 'layout',
        component: LayoutComponent,
        canActivate: [roleGuard],
        children: [
            {
                path: 'dashboard',
                component: DashboardComponent,
                canActivate: [roleGuard],
                data: { expectedRoles: ['ROLE_Admin'] }
            },
            {
                path: 'pacientes',
                component: Pacientes,
                canActivate: [roleGuard],
                data: { expectedRoles: ['ROLE_Admin', 'ROLE_Recepcionista'] }
            },
            {
                path: 'odontologos',
                component: Odontologos,
                canActivate: [roleGuard],
                data: { expectedRoles: ['ROLE_Admin', 'ROLE_Recepcionista'] }
            },
            {
                path: 'consultorios',
                component: Consultorios,
                canActivate: [roleGuard],
                data: { expectedRoles: ['ROLE_Admin'] }
            },
            {
                path: 'citas',
                component: Citas,
                canActivate: [roleGuard],
                data: { expectedRoles: ['ROLE_Admin', 'ROLE_Recepcionista'] }
            },
            {
                path: 'recepcionistas',
                component: Recepcionistas,
                canActivate: [roleGuard],
                data: { expectedRoles: ['ROLE_Admin'] }
            },
            {
                path: 'mis-citas-paciente',
                component: MisCitasPaciente,
                canActivate: [roleGuard],
                data: { expectedRoles: ['ROLE_Paciente'] }
            },
            {
                path: 'mis-citas',
                component: MisCitas,
                canActivate: [roleGuard],
                data: { expectedRoles: ['ROLE_Odontologo'] }
            },
            {
                path: 'mis-pacientes',
                component: MisPacientes,
                canActivate: [roleGuard],
                data: { expectedRoles: ['ROLE_Odontologo'] }
            },
            {
                path: 'calendario',
                component: Calendario,
                canActivate: [roleGuard],
                data: { expectedRoles: ['ROLE_Admin', 'ROLE_Recepcionista', 'ROLE_Odontologo', 'ROLE_Paciente'] }
            },
            {
                path: 'pagos',
                component: Pagos,
                canActivate: [roleGuard],
                data: { expectedRoles: ['ROLE_Admin', 'ROLE_Recepcionista'] }
            },
            { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
            { path: '**', redirectTo: 'dashboard' }
        ]
    },

    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: '**', redirectTo: 'login' }
];