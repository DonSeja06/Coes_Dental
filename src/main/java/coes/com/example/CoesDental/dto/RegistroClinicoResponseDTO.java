package coes.com.example.CoesDental.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class RegistroClinicoResponseDTO {
    private Long id;
    private String detalle;
    private LocalDateTime fechaAtencion;
    private String nombrePaciente;
    private String nombreOdontologo;
}