import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';

import { CitaService } from '../../services/cita/cita';
import { PacienteService } from '../../services/paciente/paciente';
import { OdontologoService } from '../../services/odontologo/odontologo';
import { ConsultorioService } from '../../services/consultorio/consultorio';

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
        alert('Fecha inválida: No puedes programar una cita en el pasado.');
        return;
      }

      if (fechaSeleccionada.getMinutes() !== 0) {
        alert('Por favor, programa las citas en horas exactas (Ej. 10:00, 11:00).');
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
          alert( mensajeError);
        }
      });
    } else {
      this.citaForm.markAllAsTouched();
    }
  }

  cancelarCita(id: number) {
    if (confirm('¿Estás seguro de cancelar esta cita?')) {
      this.citaService.cancelar(id).subscribe({
        next: () => this.cargarCitas(),
        error: (err) => alert('Error al cancelar la cita.')
      });
    }
  }
}