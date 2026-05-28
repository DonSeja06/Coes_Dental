package coes.com.example.CoesDental.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "registro_clinico")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistroClinico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El detalle es obligatorio")
    private String detalle;

    @ManyToOne
    @JoinColumn(name = "historial_id")
    private HistorialClinico historial;

    @ManyToOne
    @JoinColumn(name = "cita_id")
    private Cita cita;
}
