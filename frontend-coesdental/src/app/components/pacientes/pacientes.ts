import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PacienteService } from '../../services/paciente/paciente';
import { HistorialService } from '../../services/historial/historial';
import Swal from 'sweetalert2';

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

  listaHistorial: any[] = [];
  cargandoHistorial: boolean = false;
  pacienteSeleccionadoNombre: string = '';

  constructor(
    private pacienteService: PacienteService,
    private historialService: HistorialService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder
  ) {
    this.pacienteForm = this.fb.group({
      DNI: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(8)]],
      nombre: ['', Validators.required],
      fechaNacimiento: ['', Validators.required],
      correo: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      telefono: ['', [Validators.maxLength(9)]]
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
    this.listaPacientes = []; // Limpiar la lista para evitar solapamientos
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
        error: (err) => Swal.fire('Error', 'Error al reactivar paciente.', 'error')
      });
    }
  }

  cargarPacientes() {
    this.cargando = true;
    this.listaPacientes = [];
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
      fechaNacimiento: fechaLimpia,
      correo: paciente.correo,
      telefono: paciente.telefono,
      password: '' // No mostrar la password al editar
    });
  }

  guardarPaciente() {
    if (this.pacienteForm.valid) {
      const datos = this.pacienteForm.value;
      const payload = {
        DNI: datos.DNI,
        nombre: datos.nombre,
        fechaNacimiento: datos.fechaNacimiento + 'T00:00:00',
        correo: datos.correo,
        password: datos.password,
        telefono: datos.telefono
      };

      if (this.pacienteEnEdicion) {
        this.pacienteService.editar(this.pacienteEnEdicion, payload).subscribe({
          next: () => {
            document.getElementById('btnCerrarModal')?.click();
            this.cargarPacientes();
          },
          error: (error) => {
            console.error('Error al editar paciente', error);
            Swal.fire('Error', 'Hubo un error al actualizar al paciente.', 'error');
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
            Swal.fire('Error', 'Hubo un error al crear el paciente.', 'error');
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
          Swal.fire('Error', 'Hubo un error al desactivar al paciente.', 'error');
        }
      });
    }
  }

  abrirHistorial(idPaciente: number, nombre: string) {
    this.pacienteSeleccionadoNombre = nombre;
    this.cargandoHistorial = true;
    this.listaHistorial = [];
    this.historialService.obtenerPorPaciente(idPaciente).subscribe({
      next: (datos) => {
        this.listaHistorial = datos;
        this.cargandoHistorial = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al traer historial', err);
        this.cargandoHistorial = false;
        this.cdr.detectChanges();
      }
    });
  }
}