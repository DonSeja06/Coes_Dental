import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConsultorioService {
  private apiUrl = 'http://localhost:8080/api/admin/consultorios';

  constructor(private http: HttpClient) { }

  listarActivos(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  registrar(consultorio: any): Observable<any> {
    return this.http.post(this.apiUrl, consultorio);
  }

  editar(id: number, consultorio: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, consultorio);
  }

  desactivar(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {responseType: 'text'});
  }
}