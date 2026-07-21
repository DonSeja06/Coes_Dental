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
}