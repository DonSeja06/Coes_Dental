package coes.com.example.CoesDental.controller;

import coes.com.example.CoesDental.dto.ConsultorioResponseDTO;
import coes.com.example.CoesDental.model.Consultorio;
import coes.com.example.CoesDental.service.ConsultorioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/admin/consultorios")
public class ConsultorioController {

    @Autowired
    private ConsultorioService consultorioService;

    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ConsultorioResponseDTO> registrar(@Valid @RequestBody Consultorio consultorio) {
        Consultorio nuevoConsultorio = consultorioService.registrarConsultorio(consultorio);
        return ResponseEntity.ok(mapearDTO(nuevoConsultorio));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Consultorio consultorio) {
        try {
            Consultorio consultorioActualizado = consultorioService.actualizarConsultorio(id, consultorio);
            return ResponseEntity.ok(mapearDTO(consultorioActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista')")
    public ResponseEntity<List<ConsultorioResponseDTO>> listarActivos() {
        List<ConsultorioResponseDTO> lista = consultorioService.listarConsultoriosActivos()
                .stream()
                .map(this::mapearDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> eliminarLogico(@PathVariable Long id) {
        try {
            consultorioService.eliminarLogicoConsultorio(id);
            return ResponseEntity.ok("Consultorio desactivado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private ConsultorioResponseDTO mapearDTO(Consultorio consultorio) {
        ConsultorioResponseDTO dto = new ConsultorioResponseDTO();
        dto.setId(consultorio.getId());
        dto.setNombreConsultorio(consultorio.getNombreConsultorio());
        dto.setPiso(consultorio.getPiso());
        return dto;
    }

    @PutMapping("/{id}/reactivar")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> reactivar(@PathVariable Long id) {
        try {
            Consultorio consultorioReactivado = consultorioService.reactivarConsultorio(id);
            return ResponseEntity.ok(mapearDTO(consultorioReactivado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}