import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RecepcionistaService } from '../../services/recepcionista/recepcionista';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-recepcionistas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './recepcionistas.html',
  styleUrls: ['./recepcionistas.css'],
})
export class Recepcionistas implements OnInit {
  listaRecepcionistas: any[] = [];
  cargando: boolean = true;
  recepcionistaForm: FormGroup;

  constructor(
    private recepcionistaService: RecepcionistaService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder
  ) {
    this.recepcionistaForm = this.fb.group({
      DNI: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(8)]],
      nombre: ['', Validators.required],
      correo: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      telefono: ['', [Validators.maxLength(9)]]
    });
  }

  ngOnInit() {
    this.cargarRecepcionistas();
  }

  cargarRecepcionistas() {
    this.cargando = true;
    this.recepcionistaService.listarTodos().subscribe({
      next: (datos) => {
        this.listaRecepcionistas = datos;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al traer los recepcionistas', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModalNuevo() {
    this.recepcionistaForm.reset();
  }

  guardarRecepcionista() {
    if (this.recepcionistaForm.valid) {
      const payload = this.recepcionistaForm.value;
      this.recepcionistaService.registrar(payload).subscribe({
        next: () => {
          document.getElementById('btnCerrarModal')?.click();
          this.recepcionistaForm.reset();
          Swal.fire('Éxito', 'Recepcionista registrado exitosamente.', 'success');
          this.cargarRecepcionistas();
        },
        error: (error) => {
          console.error('Error al crear recepcionista', error);
          Swal.fire('Error', 'Hubo un error al crear el recepcionista: ' + (error.error || error.message), 'error');
        }
      });
    } else {
      Object.values(this.recepcionistaForm.controls).forEach(control => control.markAsTouched());
    }
  }
}
