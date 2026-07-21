import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RecepcionistaService {
  private apiUrl = 'http://localhost:8080/api/recepcionistas';

  constructor(private http: HttpClient) { }

  listarTodos(): Observable<any> {
    return this.http.get(`${this.apiUrl}`);
  }

  registrar(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}`, data);
  }
}
