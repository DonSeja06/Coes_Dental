package coes.com.example.CoesDental.dto;

import java.time.LocalDateTime;

import coes.com.example.CoesDental.model.EstadoPago;
import coes.com.example.CoesDental.model.MetodoPago;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagoResponseDTO {
    private Long id;
    private Long citaId;
    private Double monto;
    private MetodoPago metodoPago;
    private EstadoPago estadoPago;
    private LocalDateTime fechaPago;
    private String nombrePaciente;
    private String nombreOdontologo;
    private LocalDateTime fechaCita;
}
