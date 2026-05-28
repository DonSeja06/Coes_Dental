package coes.com.example.CoesDental.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "odontologo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Odontologo extends Usuario{

    @NotBlank(message = "El número de Colegiatura no puede estar en blanco")
    @Size(min = 5,max = 5,message = "El número de Colegiatura Odontologico debe tener 5 digitos")
    private String colegiatura;

    @NotNull
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaRegistro;

    @NotNull(message = "El odontologo debe registrar una especialidad")
    @ManyToOne
    @JoinColumn(name = "especialidad_id",referencedColumnName = "id")
    private Especialidad especialidad;

    @OneToMany(mappedBy = "odontologo")
    private List<Cita> citas;
}
