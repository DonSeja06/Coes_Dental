package coes.com.example.CoesDental.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class CitaResponseDTO {
    private Long id;
    private Long pacienteId;
    private LocalDateTime fechaCita;
    private String estadoCita;
    private String nombrePaciente;
    private String nombreOdontologo;
    private String nombreConsultorio;
}