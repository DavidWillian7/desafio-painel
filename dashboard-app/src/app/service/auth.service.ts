import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../models/Usuario';
import { jwtDecode } from 'jwt-decode'; 

interface DecodedToken {
  role: string;
  sub: string;
  iat: number;
  exp: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  login(email: string, senha: string): Observable<string> {
    return this.http.post(`${this.baseUrl}/login`, { email, senha }, { responseType: 'text' });
  }

  register(usuario: Usuario): Observable<string> {
    return this.http.post(`${this.baseUrl}/register`, usuario, { responseType: 'text' });
  }

  saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem('token');
  }

  isAdmin(): boolean {
    const token = this.getToken();
    if (token) {
      try {
        const decodedToken = jwtDecode<DecodedToken>(token); 
        return decodedToken.role === 'ADMIN';
      } catch (error) {
        console.error('Erro ao decodificar o token:', error);
        return false;
      }
    }
    return false;
  }
}
