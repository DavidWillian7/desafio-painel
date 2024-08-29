import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { DashboardService } from '../../service/dashboard.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [FormsModule, CommonModule],
  providers: [DashboardService],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent {
  newAdminEmail: string = '';
  selectedFile: File | null = null;
  fileError: string = '';
  uploadSuccess: boolean = false;
  uploadError: string = '';
  adminError: string = '';
  adminSuccess: boolean = false;

  constructor(
    private dashboardService: DashboardService,
    private router: Router
  ) {}

  addAdmin() {
    if (this.newAdminEmail) {
      this.adminError = '';
      this.adminSuccess = false;

      this.dashboardService.atualizarRoleAdmin(this.newAdminEmail).subscribe({
        next: (response) => {
          if (response === "Role do usuário atualizada para ADMIN") {
            this.adminSuccess = true;
            this.newAdminEmail = '';
          } else {
            this.adminError = 'Resposta inesperada do servidor. Por favor, tente novamente.';
          }
        },
        error: (error) => {
          this.adminError = 'Erro ao adicionar admin. Por favor, tente novamente.';
          this.adminSuccess = false;
        }
      });
    } else {
      this.adminError = 'Por favor, insira um email válido.';
      this.adminSuccess = false;
    }
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file && file.name.toLowerCase().endsWith('.csv')) {
      this.selectedFile = file;
      this.fileError = '';
      this.uploadError = '';
    } else {
      this.selectedFile = null;
      this.fileError = 'Por favor, selecione um arquivo CSV válido.';
    }
  }

  updateDataset() {
    if (this.selectedFile) {
      this.uploadSuccess = false;
      this.uploadError = '';
      this.dashboardService.uploadCovidData(this.selectedFile).subscribe({
        next: (response) => {
          if (response.status === 202) {
            this.uploadSuccess = true;
            this.selectedFile = null;
            setTimeout(() => {
              alert('Upload iniciado com sucesso. O processamento do dataset está em andamento.');
              this.router.navigate(['/painel']);
            }, 100);
          } else {
            this.uploadError = 'Resposta inesperada do servidor. Por favor, verifique os logs.';
          }
        },
        error: (error) => {
          this.uploadError = 'Erro ao fazer upload do dataset. Por favor, tente novamente.';
        }
      });
    } else {
      this.fileError = 'Por favor, selecione um arquivo CSV válido.';
    }
  }
}
