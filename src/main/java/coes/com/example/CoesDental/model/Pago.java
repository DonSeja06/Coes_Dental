package coes.com.example.CoesDental.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pago")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La cita es obligatoria")
    @ManyToOne
    @JoinColumn(name = "cita_id", referencedColumnName = "id")
    private Cita cita;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private Double monto;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado del pago es obligatorio")
    private EstadoPago estadoPago;

    private LocalDateTime fechaPago;
}
