package coes.com.example.CoesDental.controller;

import coes.com.example.CoesDental.dto.EspecialidadResponseDTO;
import coes.com.example.CoesDental.model.Especialidad;
import coes.com.example.CoesDental.service.EspecialidadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/especialidades")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    @PostMapping
    public ResponseEntity<EspecialidadResponseDTO> registrar(@Valid @RequestBody Especialidad especialidad) {
        Especialidad nuevaEspecialidad = especialidadService.registrarEspecialidad(especialidad);
        return ResponseEntity.ok(mapearDTO(nuevaEspecialidad));
    }

    @GetMapping
    public ResponseEntity<List<EspecialidadResponseDTO>> listarTodas() {
        List<EspecialidadResponseDTO> lista = especialidadService.listarEspecialidades()
                .stream()
                .map(this::mapearDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Especialidad especialidad) {
        try {
            Especialidad actualizada = especialidadService.actualizarEspecialidad(id, especialidad);
            return ResponseEntity.ok(mapearDTO(actualizada));   
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    private EspecialidadResponseDTO mapearDTO(Especialidad especialidad) {
        EspecialidadResponseDTO dto = new EspecialidadResponseDTO();
        dto.setId(especialidad.getId());
        dto.setNombre(especialidad.getNombre());
        return dto;
    }
}