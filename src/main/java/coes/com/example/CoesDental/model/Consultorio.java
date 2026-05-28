package coes.com.example.CoesDental.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "consultorio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Consultorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del consultorio no puede estar en blanco")
    @Size(min = 2, message = "El nombre debe tener minimo dos caracteres")
    private String nombreConsultorio;

    @Positive
    @Min(value = 1, message = "El valor del piso debe ser positivo y mayor a 1")
    private Long piso;

    @OneToMany(mappedBy = "consultorio")
    private List<Cita> citas;

    @Enumerated(EnumType.STRING)
    private EstadoGeneral estado;
}
