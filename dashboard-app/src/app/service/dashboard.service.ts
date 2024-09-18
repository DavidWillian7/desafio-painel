import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VaccinationData } from '../models/VaccinationData';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  getCovidData(): Observable<VaccinationData[]> {
    return this.http.get<VaccinationData[]>(`${this.apiUrl}/covid-data`);
  }

  uploadCovidData(file: File): Observable<HttpResponse<string>> {
    const formData = new FormData();
    formData.append('file', file, file.name);

    const token = localStorage.getItem('token');

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.put(`${this.apiUrl}/covid-data/upload`, formData, {
      headers: headers,
      observe: 'response',
      responseType: 'text'
    });
  }

  atualizarRoleAdmin(email: string): Observable<string> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.post(`${this.apiUrl}/auth/atualizar-role`, { email }, {
      headers: headers,
      responseType: 'text'
    });
  }
}
