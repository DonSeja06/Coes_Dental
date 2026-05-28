import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OdontologoService {
  private apiUrl = 'http://localhost:8080/api/admin/odontologos';

  constructor(private http: HttpClient) { }

  listarActivos(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  registrar(odontologo: any): Observable<any> {
    return this.http.post(this.apiUrl, odontologo);
  }

  editar(id: number, odontologo: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, odontologo);
  }

  desactivar(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {responseType: 'text'});
  }

  buscarPorDni(dni: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/buscar?dni=${dni}`);
  }

  listarInactivos(): Observable<any> {
    return this.http.get(`${this.apiUrl}/inactivos`);
  }

  reactivar(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/reactivar`, {});
  }
}