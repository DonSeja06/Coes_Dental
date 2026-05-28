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

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody CitaRequestDTO dto) {
        try {
            Cita nuevaCita = citaService.registrarCita(dto);
            return ResponseEntity.ok(mapearDTO(nuevaCita));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<CitaResponseDTO>> listarTodas() {
        List<CitaResponseDTO> lista = citaService.listarTodas()
                .stream()
                .map(this::mapearDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            citaService.cancelarCita(id);
            return ResponseEntity.ok("Cita cancelada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/posponer")
    public ResponseEntity<?> posponer(
            @PathVariable Long id, 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime nuevaFecha) {
        try {
            Cita citaPospuesta = citaService.posponerCita(id, nuevaFecha);
            return ResponseEntity.ok(mapearDTO(citaPospuesta));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/iniciar-atencion")
    public ResponseEntity<?> iniciarAtencion(@PathVariable Long id) {
        try {
            Cita citaAtendida = citaService.iniciarAtencion(id);
            return ResponseEntity.ok(mapearDTO(citaAtendida));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizar(
            @PathVariable Long id, 
            @RequestParam String detalleAtencion) {
        try {
            RegistroClinico registro = citaService.finalizarCita(id, detalleAtencion);
            
            RegistroClinicoResponseDTO dto = new RegistroClinicoResponseDTO();
            dto.setId(registro.getId());
            dto.setDetalle(registro.getDetalle());
            dto.setFechaAtencion(registro.getCita().getFechaCita());
            dto.setNombrePaciente(registro.getCita().getPaciente().getNombre());
            dto.setNombreOdontologo(registro.getCita().getOdontologo().getNombre());

            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/odontologo/{odontologoId}")
    public ResponseEntity<List<CitaResponseDTO>> listarPorOdontologo(@PathVariable Long odontologoId) {
        List<CitaResponseDTO> lista = citaService.listarCitasPorOdontologo(odontologoId)
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