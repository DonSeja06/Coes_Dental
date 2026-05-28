import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PacienteService } from '../../services/paciente/paciente';
import { OdontologoService } from '../../services/odontologo/odontologo';
import { CitaService } from '../../services/cita/cita';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class DashboardComponent implements OnInit {
  totalPacientes: number = 0;
  totalOdontologos: number = 0;
  citasProgramadas: number = 0;
  citasHoy: number = 0;
  
  listaCitasHoy: any[] = [];
  cargando: boolean = true;

  constructor(
    private pacienteService: PacienteService,
    private odontologoService: OdontologoService,
    private citaService: CitaService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.cargarEstadisticas();
  }

  cargarEstadisticas() {
    this.cargando = true;

    this.pacienteService.listarActivos().subscribe({
      next: (res) => { 
        this.totalPacientes = res.length; 
        this.cdr.detectChanges(); 
      },
      error: (err) => console.error('Error pacientes', err)
    });

    this.odontologoService.listarActivos().subscribe({
      next: (res) => { 
        this.totalOdontologos = res.length; 
        this.cdr.detectChanges(); 
      },
      error: (err) => console.error('Error odontólogos', err)
    });

    this.citaService.listarTodas().subscribe({
      next: (res) => {
        const hoy = new Date();
        
        const pendientes = res.filter((c: any) => c.estadoCita === 'CREADA' || c.estadoCita === 'POSPUESTA');
        this.citasProgramadas = pendientes.length;

        this.listaCitasHoy = res.filter((c: any) => {
          if (!c.fechaCita) return false;

          let fechaCita;
          if (Array.isArray(c.fechaCita)) {
            fechaCita = new Date(c.fechaCita[0], c.fechaCita[1] - 1, c.fechaCita[2]);
          } else {
            fechaCita = new Date(c.fechaCita);
          }

          const esHoy = fechaCita.getDate() === hoy.getDate() && 
                        fechaCita.getMonth() === hoy.getMonth() && 
                        fechaCita.getFullYear() === hoy.getFullYear();
                        
          return esHoy && c.estadoCita !== 'CANCELADA';
        });

        this.listaCitasHoy.sort((a, b) => {
          const timeA = Array.isArray(a.fechaCita) ? new Date(a.fechaCita[0], a.fechaCita[1]-1, a.fechaCita[2], a.fechaCita[3], a.fechaCita[4]).getTime() : new Date(a.fechaCita).getTime();
          const timeB = Array.isArray(b.fechaCita) ? new Date(b.fechaCita[0], b.fechaCita[1]-1, b.fechaCita[2], b.fechaCita[3], b.fechaCita[4]).getTime() : new Date(b.fechaCita).getTime();
          return timeA - timeB;
        });

        this.citasHoy = this.listaCitasHoy.length;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar la agenda:', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }
}