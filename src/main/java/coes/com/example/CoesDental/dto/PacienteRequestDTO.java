package coes.com.example.CoesDental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class PacienteRequestDTO {
    @NotBlank
    @Size(min = 8, max = 8)
    private String DNI;

    @NotBlank
    private String nombre;

    @NotNull
    private LocalDateTime fechaNacimiento;
}