package coes.com.example.CoesDental.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "paciente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El DNI no puede estar vacio")
    @Column(unique = true)
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 digitos")
    private String DNI;

    @NotBlank(message = "El nombre del paciente no puede estar vacio")
    @Size(min = 2,message = "El nombre del paciente debe tener minimo dos caracteres")
    private String nombre;

    @NotNull(message = "La fecha de nacimiento del paciente es obligatoria")
    private LocalDateTime fechaNacimiento;

    @NotNull
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaInscripcion;

    @OneToMany(mappedBy = "paciente")
    private List<Cita> citas;

    @OneToOne(mappedBy = "paciente")
    private HistorialClinico historialClinico;

    @Enumerated(EnumType.STRING)
    private EstadoGeneral estado;
}
