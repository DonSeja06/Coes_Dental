import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PacienteService {
  private apiUrl = 'http://localhost:8080/api/pacientes';

  constructor(private http: HttpClient) { }

  listarActivos(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  registrar(paciente: any): Observable<any> {
    return this.http.post(this.apiUrl, paciente);
  }

  desactivar(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {responseType: 'text'});
  }

  editar(id: number, paciente: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, paciente);
  }

  buscarPorDni(dni: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/buscar?dni=${dni}`);
  }

  listarInactivos(): Observable<any> {
    return this.http.get(`${this.apiUrl}/admin/inactivos`);
  }

  reactivar(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/admin/${id}/reactivar`, {});
  }
}