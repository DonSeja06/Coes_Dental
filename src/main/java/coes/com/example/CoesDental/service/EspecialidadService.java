package coes.com.example.CoesDental.service;

import coes.com.example.CoesDental.model.Especialidad;
import coes.com.example.CoesDental.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Transactional
    public Especialidad registrarEspecialidad(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }

    @Transactional(readOnly = true)
    public List<Especialidad> listarEspecialidades() {
        return especialidadRepository.findAll();
    }

    @Transactional
    public Especialidad actualizarEspecialidad(Long id, Especialidad datosActualizados) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con el ID: " + id));
        especialidad.setNombre(datosActualizados.getNombre());
        especialidad.setCosto(datosActualizados.getCosto());
        return especialidadRepository.save(especialidad);
    }
}