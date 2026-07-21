import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';

import { CitaService } from '../../services/cita/cita';
import { PacienteService } from '../../services/paciente/paciente';
import { OdontologoService } from '../../services/odontologo/odontologo';
import { ConsultorioService } from '../../services/consultorio/consultorio';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-citas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './citas.html',
  styleUrls: ['./citas.css'],
})
export class Citas implements OnInit {
  listaCitas: any[] = [];
  
  listaPacientes: any[] = [];
  listaOdontologos: any[] = [];
  listaConsultorios: any[] = [];
  
  cargando: boolean = true;
  citaForm: FormGroup;

  constructor(
    private citaService: CitaService,
    private pacienteService: PacienteService,
    private odontologoService: OdontologoService,
    private consultorioService: ConsultorioService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder
  ) {
    this.citaForm = this.fb.group({
      pacienteId: ['', Validators.required],
      odontologoId: ['', Validators.required],
      consultorioId: ['', Validators.required],
      fechaCita: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.cargarDatosBase();
    this.cargarCitas();
  }
  cargarDatosBase() {
    this.pacienteService.listarActivos().subscribe(res => this.listaPacientes = res);
    this.odontologoService.listarActivos().subscribe(res => this.listaOdontologos = res);
    this.consultorioService.listarActivos().subscribe(res => this.listaConsultorios = res);
  }

  cargarCitas() {
    this.cargando = true;
    this.citaService.listarTodas().subscribe({
      next: (datos) => {
        this.listaCitas = datos;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al traer citas', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModalNuevo() {
    this.citaForm.reset();
  }

  guardarCita() {
    if (this.citaForm.valid) {
      const payload = this.citaForm.value;
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

      this.citaService.registrar(payload).subscribe({
        next: () => {
          document.getElementById('btnCerrarModalCita')?.click();
          this.citaForm.reset();
          this.cargarCitas();
        },
        error: (err) => {
          let mensajeError = 'Error al registrar la cita. Revisa los cruces de horario.';
          if (typeof err.error === 'string') {
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

  posponerCita(id: number) {
    Swal.fire({
      title: 'Posponer Cita',
      html: '<input type="datetime-local" id="swal-input-fecha" class="swal2-input">',
      focusConfirm: false,
      showCancelButton: true,
      confirmButtonText: 'Posponer',
      cancelButtonText: 'Cerrar',
      preConfirm: () => {
        const fecha = (document.getElementById('swal-input-fecha') as HTMLInputElement).value;
        if (!fecha) {
          Swal.showValidationMessage('Debe seleccionar una nueva fecha');
          return false;
        }
        return fecha;
      }
    }).then((result) => {
      if (result.isConfirmed) {
        this.citaService.posponer(id, result.value as string).subscribe({
          next: () => {
            Swal.fire('Éxito', 'La cita ha sido pospuesta.', 'success');
            this.cargarCitas();
          },
          error: (err) => {
            let msg = 'Error al posponer la cita.';
            if (err.error && err.error.message) msg = err.error.message;
            else if (typeof err.error === 'string') msg = err.error;
            Swal.fire('Error', msg, 'error');
          }
        });
      }
    });
  }

  cancelarCita(id: number) {
    Swal.fire({
      title: '¿Cancelar Cita?',
      text: '¿Estás seguro de cancelar esta cita? Esta acción no se puede deshacer.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, cancelar',
      cancelButtonText: 'Volver'
    }).then((result) => {
      if (result.isConfirmed) {
        this.citaService.cancelar(id).subscribe({
          next: () => {
            Swal.fire('Cancelada', 'La cita ha sido cancelada.', 'success');
            this.cargarCitas();
          },
          error: (err) => Swal.fire('Error', 'No se pudo cancelar la cita.', 'error')
        });
      }
    });
  }

  aprobarSolicitud(id: number) {
    if (!this.listaConsultorios || this.listaConsultorios.length === 0) {
      Swal.fire('Error', 'No hay consultorios disponibles para asignar.', 'error');
      return;
    }
    
    let optionsHtml = '';
    this.listaConsultorios.forEach(c => {
      optionsHtml += `<option value="${c.id}">${c.nombreConsultorio}</option>`;
    });

    Swal.fire({
      title: 'Aprobar Cita',
      html: `
        <div class="mb-3">
           <label class="form-label text-start d-block">Seleccionar Consultorio Definitivo:</label>
           <select id="swal-consultorio-id" class="form-select">
             ${optionsHtml}
           </select>
        </div>
      `,
      showCancelButton: true,
      confirmButtonText: 'Aprobar',
      cancelButtonText: 'Cancelar',
      preConfirm: () => {
        const select = document.getElementById('swal-consultorio-id') as HTMLSelectElement;
        return select.value;
      }
    }).then((result) => {
      if (result.isConfirmed) {
         this.citaService.aprobar(id, Number(result.value)).subscribe({
           next: () => {
             Swal.fire('Aprobada', 'La solicitud ha sido aprobada y asignada al consultorio.', 'success');
             this.cargarCitas();
           },
           error: (err) => {
             let msg = 'No se pudo aprobar la solicitud.';
             if (err.error && err.error.message) msg = err.error.message;
             else if (typeof err.error === 'string') msg = err.error;
             Swal.fire('Error', msg, 'error');
           }
         });
      }
    });
  }

  rechazarSolicitud(id: number) {
    Swal.fire({
      title: '¿Rechazar Solicitud?',
      text: '¿Estás seguro de rechazar esta solicitud de cita?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, rechazar',
      cancelButtonText: 'Volver'
    }).then((result) => {
      if (result.isConfirmed) {
        this.citaService.rechazar(id).subscribe({
          next: () => {
            Swal.fire('Rechazada', 'La solicitud ha sido rechazada.', 'success');
            this.cargarCitas();
          },
          error: (err) => Swal.fire('Error', 'No se pudo rechazar la solicitud.', 'error')
        });
      }
    });
  }
}