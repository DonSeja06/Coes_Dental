import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PacienteService } from '../../services/paciente/paciente';

@Component({
  selector: 'app-pacientes',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './pacientes.html',
  styleUrls: ['./pacientes.css'],
})
export class Pacientes implements OnInit {
  listaPacientes: any[] = [];
  cargando: boolean = true;
  pacienteForm: FormGroup;
  pacienteEnEdicion: number | null = null;
  modoInactivos: boolean = false;

  constructor(
    private pacienteService: PacienteService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder
  ) {
    this.pacienteForm = this.fb.group({
      DNI: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(8)]],
      nombre: ['', Validators.required],
      fechaNacimiento: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.cargarPacientes();
  }

  cambiarVista(verInactivos: boolean) {
    this.modoInactivos = verInactivos;
    if (this.modoInactivos) {
      this.cargarInactivos();
    } else {
      this.cargarPacientes();
    }
  }

  cargarInactivos() {
    this.cargando = true;
    this.pacienteService.listarInactivos().subscribe({
      next: (datos) => {
        this.listaPacientes = datos;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al traer inactivos', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  buscarDni(dni: string) {
    if (!dni || dni.trim() === '') {
      this.cambiarVista(this.modoInactivos);
      return;
    }
    
    this.cargando = true;
    this.pacienteService.buscarPorDni(dni.trim()).subscribe({
      next: (datos) => {
        this.listaPacientes = datos ? [datos] : [];
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('No se encontró el paciente', err);
        this.listaPacientes = [];
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  reactivarPaciente(id: number) {
    if (confirm('¿Estás seguro de reactivar a este paciente?')) {
      this.pacienteService.reactivar(id).subscribe({
        next: () => {
          this.cargarInactivos();
        },
        error: (err) => alert('Error al reactivar paciente.')
      });
    }
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
        console.error('Error al traer los pacientes', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModalNuevo() {
    this.pacienteEnEdicion = null;
    this.pacienteForm.reset();
  }

  abrirModalEditar(paciente: any) {
    this.pacienteEnEdicion = paciente.id;
    
    let fechaLimpia = paciente.fechaNacimiento;
    if (fechaLimpia && fechaLimpia.includes('T')) {
      fechaLimpia = fechaLimpia.split('T')[0];
    }

    this.pacienteForm.patchValue({
      DNI: paciente.DNI,
      nombre: paciente.nombre,
      fechaNacimiento: fechaLimpia
    });
  }

  guardarPaciente() {
    if (this.pacienteForm.valid) {
      const datos = this.pacienteForm.value;
      const payload = {
        DNI: datos.DNI,
        nombre: datos.nombre,
        fechaNacimiento: datos.fechaNacimiento + 'T00:00:00' 
      };

      if (this.pacienteEnEdicion) {
        this.pacienteService.editar(this.pacienteEnEdicion, payload).subscribe({
          next: () => {
            document.getElementById('btnCerrarModal')?.click();
            this.cargarPacientes();
          },
          error: (error) => {
            console.error('Error al editar paciente', error);
            alert('Hubo un error al actualizar al paciente.');
          }
        });
      } 
      else {
        this.pacienteService.registrar(payload).subscribe({
          next: () => {
            document.getElementById('btnCerrarModal')?.click();
            this.pacienteForm.reset();
            this.cargarPacientes();
          },
          error: (error) => {
            console.error('Error al crear paciente', error);
            alert('Hubo un error al crear el paciente.');
          }
        });
      }
    } else {
      Object.values(this.pacienteForm.controls).forEach(control => control.markAsTouched());
    }
  }

  desactivarPaciente(id: number) {
    const confirmar = confirm('¿Estás seguro de que deseas desactivar a este paciente?');
    
    if (confirmar) {
      this.pacienteService.desactivar(id).subscribe({
        next: (respuesta) => {
          console.log('Paciente desactivado');
          this.cargarPacientes();
        },
        error: (error) => {
          console.error('Error al desactivar', error);
          alert('Hubo un error al desactivar al paciente.');
        }
      });
    }
  }
}