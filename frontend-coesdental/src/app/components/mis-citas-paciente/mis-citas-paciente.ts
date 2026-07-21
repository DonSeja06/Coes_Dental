import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CitaService } from '../../services/cita/cita';
import { HistorialService } from '../../services/historial/historial';
import { OdontologoService } from '../../services/odontologo/odontologo';
import { ConsultorioService } from '../../services/consultorio/consultorio';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-mis-citas-paciente',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './mis-citas-paciente.html',
  styleUrls: ['./mis-citas-paciente.css']
})
export class MisCitasPaciente implements OnInit {
  listaCitas: any[] = [];
  listaHistorial: any[] = [];
  cargando: boolean = true;
  cargandoHistorial: boolean = true;
  mostrarHistorial: boolean = false;
  
  citaForm: FormGroup;
  listaOdontologos: any[] = [];
  listaConsultorios: any[] = [];
  filtroOdontologo: string = '';

  get odontologosFiltrados() {
    if (!this.filtroOdontologo) return this.listaOdontologos;
    const f = this.filtroOdontologo.toLowerCase();
    return this.listaOdontologos.filter(o => 
       o.nombre.toLowerCase().includes(f) || 
       o.apellido?.toLowerCase().includes(f) ||
       (o.especialidad && o.especialidad.toLowerCase().includes(f))
    );
  }

  constructor(
    private citaService: CitaService, 
    private historialService: HistorialService,
    private odontologoService: OdontologoService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder
  ) {
    this.citaForm = this.fb.group({
      odontologoId: ['', Validators.required],
      fechaCita: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.cargarMisCitas();
    this.cargarMiHistorial();
    this.cargarDatosBase();
  }

  cargarDatosBase() {
    this.odontologoService.listarActivos().subscribe(res => this.listaOdontologos = res);
  }

  abrirModalNuevo() {
    this.citaForm.reset();
  }

  solicitarCita() {
    if (this.citaForm.valid) {
      const payload = this.citaForm.value;
      const miId = Number(localStorage.getItem('userId'));
      payload.pacienteId = miId;

      const fechaSeleccionada = new Date(payload.fechaCita);
      const ahora = new Date();

      if (fechaSeleccionada < ahora) {
        Swal.fire('Error', 'Fecha inválida: No puedes programar una cita en el pasado.', 'error');
        return;
      }

      if (fechaSeleccionada.getMinutes() !== 0) {
        Swal.fire('Aviso', 'Por favor, programa las citas en horas exactas (Ej. 10:00, 11:00).', 'warning');
        return;
      }

      this.citaService.solicitar(payload).subscribe({
        next: () => {
          document.getElementById('btnCerrarModalSolicitar')?.click();
          this.citaForm.reset();
          Swal.fire('Éxito', 'Su solicitud de cita ha sido enviada. Espere confirmación.', 'success');
          this.cargarMisCitas();
        },
        error: (err) => {
          let mensajeError = 'Error al solicitar la cita.';
          if (err.error && err.error.message) {
            mensajeError = err.error.message;
          } else if (typeof err.error === 'string') {
            mensajeError = err.error; 
          } else if (err.message) {
            mensajeError = err.message;
          }
          Swal.fire('Error', mensajeError, 'error');
        }
      });
    } else {
      this.citaForm.markAllAsTouched();
    }
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
