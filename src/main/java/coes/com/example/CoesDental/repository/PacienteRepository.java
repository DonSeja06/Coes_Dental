package coes.com.example.CoesDental.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coes.com.example.CoesDental.model.EstadoGeneral;
import coes.com.example.CoesDental.model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente,Long>{
    List<Paciente> findByEstado(EstadoGeneral estado);
    Optional<Paciente> findByDNIAndEstado(String DNI, EstadoGeneral estado);
    boolean existsByDNI(String DNI);
    Optional<Paciente> findByDNI(String dni);
}
