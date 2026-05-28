package coes.com.example.CoesDental.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coes.com.example.CoesDental.model.EstadoGeneral;
import coes.com.example.CoesDental.model.Odontologo;

@Repository
public interface OdontologoRepository extends JpaRepository<Odontologo,Long>{
    List<Odontologo> findByEstado(EstadoGeneral estado);
    
    boolean existsByColegiatura(String colegiatura);
    Optional<Odontologo> findByDNI(String dni);
}
