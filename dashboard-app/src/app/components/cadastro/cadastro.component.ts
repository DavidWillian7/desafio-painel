import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { Usuario } from '../../models/Usuario';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './cadastro.component.html',
  styleUrl: './cadastro.component.css'
})
export class CadastroComponent {
  usuario: Usuario = {
    nome: '',
    dataNascimento: new Date(),
    email: '',
    senha: ''
  };

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.authService.register(this.usuario).subscribe({
      next: (token) => {
        this.authService.saveToken(token);
        this.router.navigate(['/']);
      },
      error: (error) => {
        console.error('Erro no cadastro:', error);
      }
    });
  }
}
