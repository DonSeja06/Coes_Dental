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
public class Paciente extends Usuario {

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
}
