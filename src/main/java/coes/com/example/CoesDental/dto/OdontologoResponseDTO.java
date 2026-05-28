package coes.com.example.CoesDental.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OdontologoResponseDTO {
    private Long id;
    private String DNI;
    private String nombre;
    private String correo;
    private String colegiatura;
    private String especialidad;
}