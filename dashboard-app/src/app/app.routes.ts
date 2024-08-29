import { Routes } from '@angular/router';
import { PainelComponent } from './components/painel/painel.component';
import { AdminComponent } from './components/admin/admin.component';
import { LoginComponent } from './components/login/login.component';
import { CadastroComponent } from './components/cadastro/cadastro.component';
import { AuthGuard } from './guard/auth.guard';

export const routes: Routes = [
    { path: 'painel', component: PainelComponent },
    { path: 'login', component: LoginComponent },
    { path: 'cadastro', component: CadastroComponent },
    { path: 'admin', component: AdminComponent, canActivate: [AuthGuard] },
    { path: '', redirectTo: '/painel', pathMatch: 'full' },
    { path: '**', redirectTo: '/painel' }
];
