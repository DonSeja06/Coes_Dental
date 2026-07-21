import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CitaService } from '../../services/cita/cita';
import { HistorialService } from '../../services/historial/historial';

@Component({
  selector: 'app-mis-citas-paciente',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mis-citas-paciente.html',
  styleUrls: ['./mis-citas-paciente.css']
})
export class MisCitasPaciente implements OnInit {
  listaCitas: any[] = [];
  listaHistorial: any[] = [];
  cargando: boolean = true;
  cargandoHistorial: boolean = true;
  mostrarHistorial: boolean = false;

  constructor(
    private citaService: CitaService, 
    private historialService: HistorialService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarMisCitas();
    this.cargarMiHistorial();
  }

  cargarMiHistorial() {
    const miId = Number(localStorage.getItem('userId')); 
    if (!miId) return;

    this.historialService.obtenerPorPaciente(miId).subscribe({
      next: (datos) => {
        this.listaHistorial = datos;
        this.cargandoHistorial = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al traer mi historial', err);
        this.cargandoHistorial = false;
        this.cdr.detectChanges();
      }
    });
  }

  cargarMisCitas() {
    const miId = Number(localStorage.getItem('userId')); 

    if (!miId) {
      return;
    }

    this.cargando = true;
    this.citaService.listarPorPaciente(miId).subscribe({
      next: (datos) => {
        this.listaCitas = datos;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al traer mis citas', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }
}
