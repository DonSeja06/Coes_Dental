package coes.com.example.CoesDental.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El DNI no puede estar vacio")
    @Column(unique = true)
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 digitos")
    private String DNI;

    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(min = 2,max = 100,message = "El nombre debe tener entre 2 y 100 palabras")
    private String nombre;

    @NotBlank(message = "El correo no puede ser vacio")
    @Column(unique = true)
    @Email
    private String correo;

    @NotBlank(message = "La contraseña no puede estara en blanco")
    private String password;

    @Size(min = 9, max = 9, message = "El número de telefono debe tener 9 digitos")
    private String telefono;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Enumerated(EnumType.STRING)
    private EstadoGeneral estado;
}
