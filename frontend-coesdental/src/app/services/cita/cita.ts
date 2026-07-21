import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CitaService {
  private apiUrl = 'http://localhost:8080/api/citas';

  constructor(private http: HttpClient) { }

  listarTodas(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  registrar(cita: any): Observable<any> {
    return this.http.post(this.apiUrl, cita);
  }

  cancelar(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/cancelar`, {}, {responseType: 'text'});
  }

  listarPorOdontologo(odontologoId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/odontologo/${odontologoId}`);
  }

  listarPorPaciente(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/paciente/${id}`);
  }

  iniciarAtencion(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/iniciar-atencion`, {});
  }

  finalizar(id: number, detalleAtencion: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/finalizar?detalleAtencion=${encodeURIComponent(detalleAtencion)}`, {});
  }

  obtenerHistorialPaciente(pacienteId: number): Observable<any> {
    return this.http.get(`http://localhost:8080/api/historial/paciente/${pacienteId}`);
  }
}