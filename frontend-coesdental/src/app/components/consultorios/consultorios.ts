import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConsultorioService } from '../../services/consultorio/consultorio';

@Component({
  selector: 'app-consultorios',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './consultorios.html',
  styleUrls: ['./consultorios.css'],
})
export class Consultorios implements OnInit {
  listaConsultorios: any[] = [];
  cargando: boolean = true;
  consultorioForm: FormGroup;
  consultorioEnEdicion: number | null = null;

  constructor(
    private consultorioService: ConsultorioService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder
  ) {
    this.consultorioForm = this.fb.group({
      nombreConsultorio: ['', Validators.required],
      piso: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.cargarConsultorios();
  }

  cargarConsultorios() {
    this.cargando = true;
    this.consultorioService.listarActivos().subscribe({
      next: (datos) => {
        this.listaConsultorios = datos;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al traer los consultorios', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModalNuevo() {
    this.consultorioEnEdicion = null;
    this.consultorioForm.reset();
  }

  abrirModalEditar(consultorio: any) {
    this.consultorioEnEdicion = consultorio.id;
    this.consultorioForm.patchValue({
      nombreConsultorio: consultorio.nombreConsultorio,
      piso: consultorio.piso
    });
  }

  guardarConsultorio() {
    if (this.consultorioForm.valid) {
      const payload = this.consultorioForm.value;

      if (this.consultorioEnEdicion) {
        this.consultorioService.editar(this.consultorioEnEdicion, payload).subscribe({
          next: () => {
            document.getElementById('btnCerrarModalConsultorio')?.click();
            this.cargarConsultorios();
          },
          error: (err) => this.mostrarError(err)
        });
      } else {
        this.consultorioService.registrar(payload).subscribe({
          next: () => {
            document.getElementById('btnCerrarModalConsultorio')?.click();
            this.consultorioForm.reset();
            this.cargarConsultorios();
          },
          error: (err) => this.mostrarError(err)
        });
      }
    } else {
      this.consultorioForm.markAllAsTouched();
    }
  }

  private mostrarError(err: any) {
    let mensaje = 'Ocurrió un error al guardar el consultorio.';
    
    if (typeof err.error === 'string') {
      mensaje = err.error;
    } else if (err.error && err.error.message) {
      mensaje = err.error.message;
    } else if (err.status === 400) {
      mensaje = 'Por favor, verifica los datos. El nombre debe tener al menos 2 caracteres y el piso debe ser un número válido.';
    }

    alert(mensaje);
  }

  desactivarConsultorio(id: number) {
    if (confirm('¿Estás seguro de que deseas desactivar este consultorio?')) {
      this.consultorioService.desactivar(id).subscribe({
        next: () => { this.cargarConsultorios(); },
        error: (err) => alert('Error al desactivar: ' + err.error)
      });
    }
  }
}