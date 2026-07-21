package coes.com.example.CoesDental.controller;

import coes.com.example.CoesDental.dto.CitaRequestDTO;
import coes.com.example.CoesDental.dto.CitaResponseDTO;
import coes.com.example.CoesDental.dto.RegistroClinicoResponseDTO;
import coes.com.example.CoesDental.model.Cita;
import coes.com.example.CoesDental.model.RegistroClinico;
import coes.com.example.CoesDental.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista', 'Odontologo')")
    public ResponseEntity<?> registrar(@Valid @RequestBody CitaRequestDTO dto) {
        Cita nuevaCita = citaService.registrarCita(dto);
        return ResponseEntity.ok(mapearDTO(nuevaCita));
    }

    @PostMapping("/solicitar")
    @PreAuthorize("hasRole('Paciente')")
    public ResponseEntity<?> solicitar(@Valid @RequestBody CitaRequestDTO dto) {
        Cita nuevaCita = citaService.solicitarCita(dto);
        return ResponseEntity.ok(mapearDTO(nuevaCita));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista', 'Odontologo', 'Paciente')")
    public ResponseEntity<List<CitaResponseDTO>> listarTodas() {
        List<CitaResponseDTO> lista = citaService.listarTodas()
                .stream()
                .map(this::mapearDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista', 'Odontologo')")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        citaService.cancelarCita(id);
        return ResponseEntity.ok("Cita cancelada correctamente");
    }

    @PutMapping("/{id}/aprobar")
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista')")
    public ResponseEntity<?> aprobar(@PathVariable Long id, @RequestParam Long consultorioId) {
        try {
            Cita citaAprobada = citaService.aprobarSolicitud(id, consultorioId);
            return ResponseEntity.ok(mapearDTO(citaAprobada));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/rechazar")
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista')")
    public ResponseEntity<?> rechazar(@PathVariable Long id) {
        citaService.rechazarSolicitud(id);
        return ResponseEntity.ok("Solicitud rechazada correctamente");
    }

    @PutMapping("/{id}/posponer")
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista', 'Odontologo')")
    public ResponseEntity<?> posponer(
            @PathVariable Long id, 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime nuevaFecha) {
        Cita citaPospuesta = citaService.posponerCita(id, nuevaFecha);
        return ResponseEntity.ok(mapearDTO(citaPospuesta));
    }

    @PutMapping("/{id}/iniciar-atencion")
    @PreAuthorize("hasAnyRole('Admin', 'Odontologo')")
    public ResponseEntity<?> iniciarAtencion(@PathVariable Long id) {
        Cita citaAtendida = citaService.iniciarAtencion(id);
        return ResponseEntity.ok(mapearDTO(citaAtendida));
    }

    @PutMapping("/{id}/finalizar")
    @PreAuthorize("hasAnyRole('Admin', 'Odontologo')")
    public ResponseEntity<?> finalizar(
            @PathVariable Long id, 
            @RequestParam String detalleAtencion) {
        RegistroClinico registro = citaService.finalizarCita(id, detalleAtencion);
        
        RegistroClinicoResponseDTO dto = new RegistroClinicoResponseDTO();
        dto.setId(registro.getId());
        dto.setDetalle(registro.getDetalle());
        dto.setFechaAtencion(registro.getCita().getFechaCita());
        dto.setNombrePaciente(registro.getCita().getPaciente().getNombre());
        dto.setNombreOdontologo(registro.getCita().getOdontologo().getNombre());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/odontologo/{odontologoId}")
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista', 'Odontologo')")
    public ResponseEntity<List<CitaResponseDTO>> listarPorOdontologo(@PathVariable Long odontologoId) {
        List<CitaResponseDTO> lista = citaService.listarCitasPorOdontologo(odontologoId)
                .stream()
                .map(this::mapearDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista', 'Paciente')")
    public ResponseEntity<List<CitaResponseDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<CitaResponseDTO> lista = citaService.listarCitasPorPaciente(pacienteId)
                .stream()
                .map(this::mapearDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    private CitaResponseDTO mapearDTO(Cita cita) {
        CitaResponseDTO dto = new CitaResponseDTO();
        dto.setId(cita.getId());
        dto.setPacienteId(cita.getPaciente().getId());
        dto.setFechaCita(cita.getFechaCita());
        dto.setEstadoCita(cita.getEstado().name());
        dto.setNombrePaciente(cita.getPaciente().getNombre());
        dto.setNombreOdontologo(cita.getOdontologo().getNombre());
        dto.setNombreConsultorio(cita.getConsultorio().getNombreConsultorio());
        return dto;
    }
}