import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EspecialidadService {
  private apiUrl = 'http://localhost:8080/api/admin/especialidades';

  constructor(private http: HttpClient) { }

  listarTodas(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  registrar(especialidad: any): Observable<any> {
    return this.http.post(this.apiUrl, especialidad);
  }

  actualizar(id: number, especialidad: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, especialidad);
  }
}