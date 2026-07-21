package coes.com.example.CoesDental.dto;

import java.time.LocalDateTime;

import coes.com.example.CoesDental.model.EstadoPago;
import coes.com.example.CoesDental.model.MetodoPago;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagoRequestDTO {

    @NotNull(message = "El id de la cita es obligatorio")
    private Long citaId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private Double monto;

    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodoPago;

    @NotNull(message = "El estado del pago es obligatorio")
    private EstadoPago estadoPago;

    private LocalDateTime fechaPago;
}
