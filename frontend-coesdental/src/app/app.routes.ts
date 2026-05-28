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
                data: { expectedRole: 'ROLE_Admin' }
            },
            {
                path: 'pacientes',
                component: Pacientes,
                canActivate: [roleGuard],
                data: { expectedRole: 'ROLE_Admin' }
            },
            {
                path: 'odontologos',
                component: Odontologos,
                canActivate: [roleGuard],
                data: { expectedRole: 'ROLE_Admin' }
            },
            {
                path: 'consultorios',
                component: Consultorios,
                canActivate: [roleGuard],
                data: { expectedRole: 'ROLE_Admin' }
            },
            {
                path: 'citas',
                component: Citas,
                canActivate: [roleGuard],
                data: { expectedRole: 'ROLE_Admin' }
            },
            {
                path: 'mis-citas',
                component: MisCitas,
                canActivate: [roleGuard],
                data: { expectedRole: 'ROLE_Odontologo' }
            },
            {
                path: 'mis-pacientes',
                component: MisPacientes,
                canActivate: [roleGuard],
                data: { expectedRole: 'ROLE_Odontologo' }
            },
            {
                path: 'calendario',
                component: Calendario
            },
            { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
            { path: '**', redirectTo: 'dashboard' }
        ]
    },

    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: '**', redirectTo: 'login' }
];