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
  pagoEnEdicionId: number | null = null;

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
    this.pagoForm.get('citaId')?.valueChanges.subscribe(citaId => {
      if (citaId) {
        const pagoAsociado = this.listaPagos.find(p => p.citaId == citaId && p.estadoPago === 'PENDIENTE');
        if (pagoAsociado) {
          this.pagoForm.patchValue({ monto: pagoAsociado.monto }, { emitEvent: false });
        }
      }
    });
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
      const citasPagadasIds = this.listaPagos
        .filter(p => p.estadoPago === 'PAGADO' || p.estadoPago === 'ANULADO')
        .map(p => p.citaId);
      this.listaCitasFinalizadas = this.listaCitasFinalizadas.filter(c => !citasPagadasIds.includes(c.id));
    }
  }

  abrirModalNuevo() {
    this.pagoEnEdicionId = null;
    this.pagoForm.enable();
    this.pagoForm.reset({ estadoPago: 'PAGADO' });
  }

  abrirModalPagarPendiente(pago: any) {
    this.pagoEnEdicionId = pago.id;
    this.pagoForm.enable();
    
    // Add the selected cita to listaCitasFinalizadas if not present
    const citaExiste = this.listaCitasFinalizadas.find(c => c.id === pago.citaId);
    if (!citaExiste) {
      this.listaCitasFinalizadas.push({
        id: pago.citaId,
        fechaCita: pago.fechaCita,
        nombrePaciente: pago.nombrePaciente
      });
    }

    this.pagoForm.patchValue({
      citaId: pago.citaId,
      monto: pago.monto,
      metodoPago: '',
      estadoPago: 'PAGADO'
    });
    this.pagoForm.get('citaId')?.disable();
    this.pagoForm.get('monto')?.disable();
  }

  guardarPago() {
    if (this.pagoForm.valid || (this.pagoForm.get('metodoPago')?.valid && this.pagoForm.get('estadoPago')?.valid)) {
      const payload = this.pagoForm.getRawValue();

      const request$ = this.pagoEnEdicionId 
          ? this.pagoService.actualizar(this.pagoEnEdicionId, payload)
          : this.pagoService.registrar(payload);

      request$.subscribe({
        next: () => {
          document.getElementById('btnCerrarModalPago')?.click();
          Swal.fire('Éxito', 'Pago procesado correctamente', 'success');
          this.pagoForm.reset();
          this.pagoEnEdicionId = null;
          this.cargarPagos();
        },
        error: (err: any) => {
          console.error(err);
          Swal.fire('Error', 'Ocurrió un error al procesar el pago.', 'error');
        }
      });
    } else {
      this.pagoForm.markAllAsTouched();
    }
  }
}
