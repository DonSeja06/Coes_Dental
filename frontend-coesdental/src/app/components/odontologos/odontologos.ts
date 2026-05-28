import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { OdontologoService } from '../../services/odontologo/odontologo';
import { EspecialidadService } from '../../services/especialidad/especialidad';

@Component({
  selector: 'app-odontologos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './odontologos.html',
  styleUrls: ['./odontologos.css'],
})
export class Odontologos implements OnInit {
  listaOdontologos: any[] = [];
  listaEspecialidades: any[] = [];
  cargando: boolean = true;
  odontologoForm: FormGroup;
  odontologoEnEdicion: number | null = null;
  especialidadForm: FormGroup;
  especialidadEnEdicion: number | null = null;
  modoInactivos: boolean = false;

  constructor(
    private odontologoService: OdontologoService,
    private especialidadService: EspecialidadService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder
  ) {
    this.odontologoForm = this.fb.group({
      DNI: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(8)]],
      nombre: ['', Validators.required],
      correo: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      telefono: ['', [Validators.minLength(9), Validators.maxLength(9)]],
      colegiatura: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(5)]],
      especialidadId: ['', Validators.required] 
    });
    this.especialidadForm = this.fb.group({
      nombre: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.cargarEspecialidades();
    this.cargarOdontologos();
  }

  cambiarVista(verInactivos: boolean) {
    this.modoInactivos = verInactivos;
    if (this.modoInactivos) {
      this.cargarInactivos();
    } else {
      this.cargarOdontologos();
    }
  }

  cargarInactivos() {
    this.cargando = true;
    this.odontologoService.listarInactivos().subscribe({
      next: (datos) => {
        this.listaOdontologos = datos;
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
    this.odontologoService.buscarPorDni(dni.trim()).subscribe({
      next: (datos) => {
        this.listaOdontologos = datos ? [datos] : [];
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('No se encontró el odontólogo', err);
        this.listaOdontologos = []; 
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  reactivarOdontologo(id: number) {
    if (confirm('¿Estás seguro de reactivar a este odontólogo?')) {
      this.odontologoService.reactivar(id).subscribe({
        next: () => {
          this.cargarInactivos(); 
        },
        error: (err) => alert('Error al reactivar odontólogo.')
      });
    }
  }

  cargarEspecialidades() {
    this.especialidadService.listarTodas().subscribe({
      next: (datos) => { this.listaEspecialidades = datos; }
    });
  }

  cargarOdontologos() {
    this.cargando = true;
    this.odontologoService.listarActivos().subscribe({
      next: (datos) => {
        this.listaOdontologos = datos;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al traer los odontólogos', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModalNuevo() {
    this.odontologoEnEdicion = null;
    this.odontologoForm.reset();
  }

  abrirModalEditar(odontologo: any) {
    this.odontologoEnEdicion = odontologo.id;
    this.odontologoForm.patchValue({
      DNI: odontologo.DNI,
      nombre: odontologo.nombre,
      correo: odontologo.correo,
      colegiatura: odontologo.colegiatura,
      telefono: odontologo.telefono || '',
      password: '' 
    });
  }

  guardarOdontologo() {
    if (this.odontologoForm.valid) {
      const payload = this.odontologoForm.value;

      if (this.odontologoEnEdicion) {
        this.odontologoService.editar(this.odontologoEnEdicion, payload).subscribe({
          next: () => {
            document.getElementById('btnCerrarModalOdontologo')?.click();
            this.cargarOdontologos();
          },
          error: (err) => alert('Error al actualizar: ' + err.error)
        });
      } else {
        this.odontologoService.registrar(payload).subscribe({
          next: () => {
            document.getElementById('btnCerrarModalOdontologo')?.click();
            this.odontologoForm.reset();
            this.cargarOdontologos();
          },
          error: (err) => alert('Error al crear: ' + err.error)
        });
      }
    } else {
      this.odontologoForm.markAllAsTouched();
    }
  }

  desactivarOdontologo(id: number) {
    if (confirm('¿Estás seguro de que deseas desactivar a este odontólogo?')) {
      this.odontologoService.desactivar(id).subscribe({
        next: () => { this.cargarOdontologos(); }
      });
    }
  }

  prepararNuevaEspecialidad() {
    this.especialidadEnEdicion = null;
    this.especialidadForm.reset();
  }

  seleccionarEspecialidadParaEditar(esp: any) {
    this.especialidadEnEdicion = esp.id;
    this.especialidadForm.patchValue({ nombre: esp.nombre });
  }

  guardarEspecialidad() {
    if (this.especialidadForm.valid) {
      const payload = this.especialidadForm.value;
      
      if (this.especialidadEnEdicion) {
        this.especialidadService.actualizar(this.especialidadEnEdicion, payload).subscribe({
          next: () => {
            this.cargarEspecialidades();
            this.prepararNuevaEspecialidad();
          },
          error: (err) => alert('Error al actualizar especialidad')
        });
      } else {
        this.especialidadService.registrar(payload).subscribe({
          next: () => {
            this.cargarEspecialidades();
            this.prepararNuevaEspecialidad();
          },
          error: (err) => alert('Error al crear especialidad')
        });
      }
    }
  }
}