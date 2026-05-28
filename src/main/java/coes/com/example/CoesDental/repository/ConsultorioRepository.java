package coes.com.example.CoesDental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coes.com.example.CoesDental.model.Consultorio;
import coes.com.example.CoesDental.model.EstadoGeneral;

@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio,Long>{
    List<Consultorio> findByEstado(EstadoGeneral estado);
}
