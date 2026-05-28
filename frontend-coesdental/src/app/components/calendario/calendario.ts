import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import esLocale from '@fullcalendar/core/locales/es';

import { CitaService } from '../../services/cita/cita';

@Component({
  selector: 'app-calendario',
  standalone: true,
  imports: [CommonModule, FullCalendarModule],
  templateUrl: './calendario.html',
  styleUrls: ['./calendario.css']
})
export class Calendario implements OnInit {
  
  cargando: boolean = true;
  eventoSeleccionado: any = null;

  calendarOptions: CalendarOptions = {
    plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
    initialView: 'timeGridWeek',
    locales: [esLocale],
    locale: 'es',
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay'
    },
    slotMinTime: '08:00:00',
    slotMaxTime: '20:00:00',
    allDaySlot: false,
    events: [],
    eventClick: this.abrirDetalleCita.bind(this)
  };

  constructor(
    private citaService: CitaService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.cargarEventos();
  }

  cargarEventos() {
    this.cargando = true;
    this.citaService.listarTodas().subscribe({
      next: (res) => {
        const eventosConvertidos = res.map((c: any) => {
          
          let fechaISO = '';
          if (Array.isArray(c.fechaCita)) {
            const year = c.fechaCita[0];
            const month = String(c.fechaCita[1]).padStart(2, '0');
            const day = String(c.fechaCita[2]).padStart(2, '0');
            const hour = String(c.fechaCita[3]).padStart(2, '0');
            const min = String(c.fechaCita[4] || 0).padStart(2, '0');
            fechaISO = `${year}-${month}-${day}T${hour}:${min}:00`;
          } else {
            fechaISO = c.fechaCita;
          }

          const fechaInicio = new Date(fechaISO);
          const fechaFin = new Date(fechaInicio.getTime() + (60 * 60 * 1000));

          let colorBloque = '#0d6efd'; 
          if(c.estadoCita === 'FINALIZADA') colorBloque = '#198754'; 
          if(c.estadoCita === 'CANCELADA') colorBloque = '#dc3545'; 
          if(c.estadoCita === 'POSPUESTA') colorBloque = '#ffc107'; 
          if(c.estadoCita === 'ATENCION') colorBloque = '#0dcaf0'; 

          return {
            title: `Cita: ${c.nombrePaciente}`,
            start: fechaInicio.toISOString(),
            end: fechaFin.toISOString(),
            color: colorBloque,
            extendedProps: {
              paciente: c.nombrePaciente,
              odontologo: c.nombreOdontologo,
              consultorio: c.nombreConsultorio,
              estado: c.estadoCita
            }
          };
        });

        this.calendarOptions.events = eventosConvertidos;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error cargando el calendario', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirDetalleCita(info: any) {
    this.eventoSeleccionado = info.event;
    this.cdr.detectChanges();
    document.getElementById('btnAbrirModalDetalle')?.click();
  }
}