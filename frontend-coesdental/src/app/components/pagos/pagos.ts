import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';

import { PagoService } from '../../services/pago';
import { CitaService } from '../../services/cita/cita';

@Component({
  selector: 'app-pagos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './pagos.html',
  styleUrls: ['./pagos.css']
})
export class Pagos implements OnInit {
  listaPagos: any[] = [];
  listaCitasFinalizadas: any[] = [];
  cargando: boolean = true;
  pagoForm: FormGroup;

  constructor(
    private pagoService: PagoService,
    private citaService: CitaService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder
  ) {
    this.pagoForm = this.fb.group({
      citaId: ['', Validators.required],
      monto: ['', [Validators.required, Validators.min(0.1)]],
      metodoPago: ['', Validators.required],
      estadoPago: ['PAGADO', Validators.required]
    });
  }

  ngOnInit() {
    this.cargarPagos();
    this.cargarCitasFinalizadas();
  }

  cargarPagos() {
    this.cargando = true;
    this.pagoService.listarTodos().subscribe({
      next: (datos) => {
        this.listaPagos = datos;
        this.filtrarCitasNoPagadas();
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar pagos', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  cargarCitasFinalizadas() {
    this.citaService.listarTodas().subscribe({
      next: (datos) => {
        // Filtrar citas que estén FINALIZADA y no tengan pago
        this.listaCitasFinalizadas = datos.filter((c: any) => c.estadoCita === 'FINALIZADA');
        this.filtrarCitasNoPagadas();
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar citas', err);
      }
    });
  }

  filtrarCitasNoPagadas() {
    if (this.listaCitasFinalizadas.length > 0 && this.listaPagos.length > 0) {
      const citasPagadasIds = this.listaPagos.map(p => p.citaId);
      this.listaCitasFinalizadas = this.listaCitasFinalizadas.filter(c => !citasPagadasIds.includes(c.id));
    }
  }

  abrirModalNuevo() {
    this.pagoForm.reset({ estadoPago: 'PAGADO' });
  }

  guardarPago() {
    if (this.pagoForm.valid) {
      const payload = this.pagoForm.value;
      this.pagoService.registrar(payload).subscribe({
        next: () => {
          document.getElementById('btnCerrarModalPago')?.click();
          Swal.fire('Éxito', 'Pago registrado correctamente', 'success');
          this.pagoForm.reset();
          this.cargarPagos();
        },
        error: (err) => {
          console.error(err);
          Swal.fire('Error', 'Ocurrió un error al registrar el pago.', 'error');
        }
      });
    } else {
      this.pagoForm.markAllAsTouched();
    }
  }
}
