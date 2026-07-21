package coes.com.example.CoesDental.controller;

import coes.com.example.CoesDental.dto.PacienteRequestDTO;
import coes.com.example.CoesDental.dto.PacienteResponseDTO;
import coes.com.example.CoesDental.model.Paciente;
import coes.com.example.CoesDental.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/pacientes")
@PreAuthorize("hasAnyRole('Admin', 'Recepcionista')")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody PacienteRequestDTO dto) {
        try {
            Paciente nuevoPaciente = pacienteService.registrarPaciente(dto);
            return ResponseEntity.ok(mapearDTO(nuevoPaciente));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista', 'Odontologo')")
    public ResponseEntity<List<PacienteResponseDTO>> listarActivos() {
        List<PacienteResponseDTO> lista = pacienteService.listarPacientesActivos()
                .stream()
                .map(this::mapearDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarLogico(@PathVariable Long id) {
        try {
            pacienteService.eliminarLogicoPaciente(id);
            return ResponseEntity.ok("Paciente desactivado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody PacienteRequestDTO dto) {
        try {
            Paciente pacienteActualizado = pacienteService.actualizarPaciente(id, dto);
            return ResponseEntity.ok(mapearDTO(pacienteActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista', 'Odontologo')")
    public ResponseEntity<?> buscarPorDni(@RequestParam String dni) {
        try {
            Paciente paciente = pacienteService.buscarPorDni(dni);
            return ResponseEntity.ok(mapearDTO(paciente));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/admin/inactivos")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<PacienteResponseDTO>> listarInactivos() {
        List<PacienteResponseDTO> lista = pacienteService.listarPacientesInactivos()
                .stream()
                .map(this::mapearDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/admin/{id}/reactivar")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> reactivar(@PathVariable Long id) {
        try {
            Paciente pacienteReactivado = pacienteService.reactivarPaciente(id);
            return ResponseEntity.ok(mapearDTO(pacienteReactivado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private PacienteResponseDTO mapearDTO(Paciente paciente) {
        PacienteResponseDTO dto = new PacienteResponseDTO();
        dto.setId(paciente.getId());
        dto.setDNI(paciente.getDNI());
        dto.setNombre(paciente.getNombre());
        dto.setFechaNacimiento(paciente.getFechaNacimiento());
        dto.setFechaInscripcion(paciente.getFechaInscripcion());
        dto.setEstado(paciente.getEstado().name());
        return dto;
    }
}