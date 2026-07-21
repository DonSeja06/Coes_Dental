package coes.com.example.CoesDental.controller;

import coes.com.example.CoesDental.dto.OdontologoRequestDTO;
import coes.com.example.CoesDental.dto.OdontologoResponseDTO;
import coes.com.example.CoesDental.model.Odontologo;
import coes.com.example.CoesDental.service.OdontologoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/admin/odontologos")
@PreAuthorize("hasRole('Admin')")
public class OdontologoController {

    @Autowired
    private OdontologoService odontologoService;

    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody OdontologoRequestDTO dto) {
        try {
            Odontologo nuevoOdontologo = odontologoService.registrarOdontologo(dto);

            OdontologoResponseDTO response = new OdontologoResponseDTO();
            response.setId(nuevoOdontologo.getId());
            response.setDNI(nuevoOdontologo.getDNI());
            response.setNombre(nuevoOdontologo.getNombre());
            response.setCorreo(nuevoOdontologo.getCorreo());
            response.setColegiatura(nuevoOdontologo.getColegiatura());
            response.setEspecialidad(nuevoOdontologo.getEspecialidad().getNombre());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista', 'Odontologo')")
    public ResponseEntity<List<OdontologoResponseDTO>> listarActivos() {
        List<OdontologoResponseDTO> listaLimpia = odontologoService.listarOdontologosActivos()
                .stream()
                .map(odontologo -> {
                    OdontologoResponseDTO response = new OdontologoResponseDTO();
                    response.setId(odontologo.getId());
                    response.setDNI(odontologo.getDNI());
                    response.setNombre(odontologo.getNombre());
                    response.setCorreo(odontologo.getCorreo());
                    response.setColegiatura(odontologo.getColegiatura());
                    response.setEspecialidad(odontologo.getEspecialidad().getNombre());
                    return response;
                }).toList();

        return ResponseEntity.ok(listaLimpia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarLogico(@PathVariable Long id) {
        try {
            odontologoService.eliminarLogicoOdontologo(id);
            return ResponseEntity.ok("Odontólogo desactivado correctamente en el sistema");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody OdontologoRequestDTO dto) {
        try {
            Odontologo odontologoActualizado = odontologoService.actualizarOdontologo(id, dto);
            
            OdontologoResponseDTO response = new OdontologoResponseDTO();
            response.setId(odontologoActualizado.getId());
            response.setDNI(odontologoActualizado.getDNI());
            response.setNombre(odontologoActualizado.getNombre());
            response.setCorreo(odontologoActualizado.getCorreo());
            response.setColegiatura(odontologoActualizado.getColegiatura());
            response.setEspecialidad(odontologoActualizado.getEspecialidad().getNombre());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista', 'Odontologo')")
    public ResponseEntity<?> buscarPorDni(@RequestParam String dni) {
        try {
            Odontologo odontologo = odontologoService.buscarPorDni(dni);
            
            OdontologoResponseDTO response = new OdontologoResponseDTO();
            response.setId(odontologo.getId());
            response.setDNI(odontologo.getDNI());
            response.setNombre(odontologo.getNombre());
            response.setCorreo(odontologo.getCorreo());
            response.setColegiatura(odontologo.getColegiatura());
            response.setEspecialidad(odontologo.getEspecialidad().getNombre());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<OdontologoResponseDTO>> listarInactivos() {
        List<OdontologoResponseDTO> lista = odontologoService.listarOdontologosInactivos()
                .stream()
                .map(o -> {
                    OdontologoResponseDTO response = new OdontologoResponseDTO();
                    response.setId(o.getId());
                    response.setDNI(o.getDNI());
                    response.setNombre(o.getNombre());
                    response.setCorreo(o.getCorreo());
                    response.setColegiatura(o.getColegiatura());
                    response.setEspecialidad(o.getEspecialidad().getNombre());
                    return response;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}/reactivar")
    public ResponseEntity<?> reactivar(@PathVariable Long id) {
        try {
            Odontologo o = odontologoService.reactivarOdontologo(id);
            OdontologoResponseDTO response = new OdontologoResponseDTO();
            response.setId(o.getId());
            response.setDNI(o.getDNI());
            response.setNombre(o.getNombre());
            response.setCorreo(o.getCorreo());
            response.setColegiatura(o.getColegiatura());
            response.setEspecialidad(o.getEspecialidad().getNombre());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}