package coes.com.example.CoesDental.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recepcionista")
@Getter
@Setter
public class Recepcionista extends Usuario {
    // Hereda todos los atributos de Usuario. 
    // Se crea la entidad para mantener el patrón de Odontologo y tener un Repositorio propio.
}
