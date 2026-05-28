package coes.com.example.CoesDental.service;

import coes.com.example.CoesDental.model.Consultorio;
import coes.com.example.CoesDental.model.EstadoGeneral;
import coes.com.example.CoesDental.repository.ConsultorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsultorioService {

    @Autowired
    private ConsultorioRepository consultorioRepository;

    @Transactional
    public Consultorio registrarConsultorio(Consultorio consultorio) {
        consultorio.setEstado(EstadoGeneral.ACTIVO);
        return consultorioRepository.save(consultorio);
    }

    @Transactional(readOnly = true)
    public List<Consultorio> listarConsultoriosActivos() {
        return consultorioRepository.findByEstado(EstadoGeneral.ACTIVO);
    }

    @Transactional
    public void eliminarLogicoConsultorio(Long id) {
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado"));
        consultorio.setEstado(EstadoGeneral.INACTIVO);
        consultorioRepository.save(consultorio);
    }

    @Transactional
    public Consultorio reactivarConsultorio(Long id) {
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado con el ID: " + id));
        
        consultorio.setEstado(EstadoGeneral.ACTIVO); 
        return consultorioRepository.save(consultorio);
    }
}