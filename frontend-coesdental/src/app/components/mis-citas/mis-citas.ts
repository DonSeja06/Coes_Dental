import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CitaService } from '../../services/cita/cita';

@Component({
  selector: 'app-mis-citas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './mis-citas.html',
  styleUrls: ['./mis-citas.css']
})
export class MisCitas implements OnInit {
  listaMisCitas: any[] = [];
  cargando: boolean = true;
  
  finalizarForm: FormGroup;
  citaEnAtencion: any = null;
  pacienteSeleccionado: any = null;

  historialClinico: any[] = [];
  cargandoHistorial: boolean = false;

  constructor(
    private citaService: CitaService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    this.finalizarForm = this.fb.group({
      detalleAtencion: ['', [Validators.required, Validators.minLength(5)]]
    });
  }

  ngOnInit() {
    this.cargarMisCitas();
  }

  cargarMisCitas() {
    this.cargando = true;
    const miId = Number(localStorage.getItem('userId')); 

    if (!miId) {
      alert('Error de sesión: No se encontró tu ID de usuario. Vuelve a iniciar sesión.');
      return;
    }

    this.citaService.listarPorOdontologo(miId).subscribe({
      next: (res) => {
        this.listaMisCitas = res.sort((a: any, b: any) => new Date(a.fechaCita).getTime() - new Date(b.fechaCita).getTime());
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar mis citas', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  iniciarAtencion(cita: any) {
    if (confirm(`¿Iniciar la atención del paciente ${cita.nombrePaciente}?`)) {
      this.citaService.iniciarAtencion(cita.id).subscribe({
        next: () => this.cargarMisCitas(),
        error: (err) => alert(' Error al iniciar atención: ' + (err.error || err.message))
      });
    }
  }

  abrirModalFinalizar(cita: any) {
    this.citaEnAtencion = cita;
    this.finalizarForm.reset();
  }

  guardarDiagnostico() {
    if (this.finalizarForm.valid && this.citaEnAtencion) {
      const detalle = this.finalizarForm.value.detalleAtencion;
      
      this.citaService.finalizar(this.citaEnAtencion.id, detalle).subscribe({
        next: () => {
          document.getElementById('btnCerrarModalFinalizar')?.click();
          alert('Cita finalizada y guardada en el historial clínico.');
          this.cargarMisCitas();
        },
        error: (err) => alert('Error al finalizar: ' + (err.error || err.message))
      });
    } else {
      this.finalizarForm.markAllAsTouched();
    }
  }

  abrirHistorial(cita: any) {
    this.pacienteSeleccionado = cita;
    this.cargandoHistorial = true;
    this.historialClinico = [];
    const idDelPaciente = cita.pacienteId; 

    this.citaService.obtenerHistorialPaciente(idDelPaciente).subscribe({
      next: (res) => {
        this.historialClinico = res;
        this.cargandoHistorial = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar historial', err);
        this.cargandoHistorial = false;
        this.cdr.detectChanges();
      }
    });
  }
}