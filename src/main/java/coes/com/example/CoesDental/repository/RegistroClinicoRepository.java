package coes.com.example.CoesDental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coes.com.example.CoesDental.model.RegistroClinico;

@Repository
public interface RegistroClinicoRepository extends JpaRepository<RegistroClinico,Long>{
    List<RegistroClinico> findByCita_Paciente_Id(Long pacienteId);
}
