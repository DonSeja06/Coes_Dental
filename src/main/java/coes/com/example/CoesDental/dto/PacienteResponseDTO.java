package coes.com.example.CoesDental.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class PacienteResponseDTO {
    private Long id;
    private String DNI;
    private String nombre;
    private LocalDateTime fechaNacimiento;
    private LocalDateTime fechaInscripcion;
    private String estado;
}