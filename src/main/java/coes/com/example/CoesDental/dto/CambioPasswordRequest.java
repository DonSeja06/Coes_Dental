package coes.com.example.CoesDental.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CambioPasswordRequest {
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @NotBlank(message = "La contraseña actual es obligatoria")
    private String passwordActual;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    private String nuevaPassword;
}
