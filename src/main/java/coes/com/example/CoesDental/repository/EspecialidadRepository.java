package coes.com.example.CoesDental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coes.com.example.CoesDental.model.Especialidad;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad,Long>{
    
}
