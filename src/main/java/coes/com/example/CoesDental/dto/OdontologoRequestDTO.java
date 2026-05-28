package coes.com.example.CoesDental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OdontologoRequestDTO {
    @NotBlank
    @Size(min = 8, max = 8)
    private String DNI;

    @NotBlank
    private String nombre;

    @NotBlank
    @Email
    private String correo;

    @NotBlank
    private String password;

    @Size(min = 9, max = 9)
    private String telefono;

    @NotBlank
    @Size(min = 5, max = 5)
    private String colegiatura;

    @NotNull
    private Long especialidadId;
}