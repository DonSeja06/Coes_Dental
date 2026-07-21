package coes.com.example.CoesDental.controller;

import coes.com.example.CoesDental.dto.RegistroClinicoResponseDTO;
import coes.com.example.CoesDental.repository.RegistroClinicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/historial")
@PreAuthorize("hasAnyRole('Admin', 'Odontologo', 'Paciente', 'Recepcionista')")
public class RegistroClinicoController {

    @Autowired
    private RegistroClinicoRepository registroClinicoRepository;

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<?> obtenerHistorialPorPaciente(@PathVariable Long pacienteId) {
        try {
            List<RegistroClinicoResponseDTO> historialLimpio = registroClinicoRepository.findByCita_Paciente_Id(pacienteId)
                    .stream()
                    .map(registro -> {
                        RegistroClinicoResponseDTO dto = new RegistroClinicoResponseDTO();
                        dto.setId(registro.getId());
                        dto.setDetalle(registro.getDetalle());
                        dto.setFechaAtencion(registro.getCita().getFechaCita());
                        dto.setNombrePaciente(registro.getCita().getPaciente().getNombre());
                        dto.setNombreOdontologo(registro.getCita().getOdontologo().getNombre());
                        
                        return dto;
                    }).collect(Collectors.toList());

            return ResponseEntity.ok(historialLimpio);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener el historial: " + e.getMessage());
        }
    }
}