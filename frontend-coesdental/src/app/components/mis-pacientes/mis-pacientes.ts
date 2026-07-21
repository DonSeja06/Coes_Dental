import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PacienteService } from '../../services/paciente/paciente';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-mis-pacientes',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule], 
  templateUrl: './mis-pacientes.html',
  styleUrls: ['./mis-pacientes.css']
})
export class MisPacientes implements OnInit {
  listaPacientes: any[] = [];
  cargando: boolean = true;
  pacienteForm: FormGroup;
  dniBusqueda: string = '';

  constructor(
    private pacienteService: PacienteService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    this.pacienteForm = this.fb.group({
      dni: ['', [Validators.required, Validators.pattern('^[0-9]{8}$')]],
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      fechaNacimiento: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.cargarPacientes();
  }

  cargarPacientes() {
    this.cargando = true;
    this.pacienteService.listarActivos().subscribe({
      next: (datos) => {
        this.listaPacientes = datos;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al traer pacientes', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  buscarPaciente() {
    if (this.dniBusqueda.trim() === '') {
      this.cargarPacientes();
      return;
    }
    this.cargando = true;
    this.pacienteService.buscarPorDni(this.dniBusqueda).subscribe({
      next: (paciente) => {
        this.listaPacientes = paciente ? [paciente] : [];
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.listaPacientes = [];
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModalNuevo() {
    this.pacienteForm.reset();
  }

  guardarPaciente() {
    if (this.pacienteForm.valid) {
      
      const payload = {
        DNI: this.pacienteForm.value.dni,
        nombre: this.pacienteForm.value.nombre,
        fechaNacimiento: this.pacienteForm.value.fechaNacimiento
      };

      this.pacienteService.registrar(payload).subscribe({
        next: () => {
          document.getElementById('btnCerrarModalPacienteDoctor')?.click();
          this.pacienteForm.reset();
          this.cargarPacientes();
          Swal.fire('Éxito', 'Paciente registrado correctamente.', 'success');
        },
        error: (err) => {
          let mensaje = 'Error al registrar el paciente.';
          if (typeof err.error === 'string') mensaje = err.error;
          Swal.fire('Atención', mensaje, 'info');
        }
      });
    } else {
      this.pacienteForm.markAllAsTouched();
    }
  }
}