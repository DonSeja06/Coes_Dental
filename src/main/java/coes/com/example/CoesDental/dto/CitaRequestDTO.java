package coes.com.example.CoesDental.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CitaRequestDTO {

    @NotNull(message = "La fecha de la cita es obligatoria")
    @FutureOrPresent(message = "La fecha debe ser actual o futura")
    private LocalDateTime fechaCita;

    @NotNull(message = "El ID del odontólogo es obligatorio")
    private Long odontologoId;

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long pacienteId;

    private Long consultorioId;
}