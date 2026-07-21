import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PagoService {
  private apiUrl = 'http://localhost:8080/api/pagos';

  constructor(private http: HttpClient) { }

  listarTodos(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  registrar(pago: any): Observable<any> {
    return this.http.post(this.apiUrl, pago);
  }

  actualizar(id: number, pago: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, pago);
  }

  eliminar(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
