package coes.com.example.CoesDental.service;

import coes.com.example.CoesDental.dto.PacienteRequestDTO;
import coes.com.example.CoesDental.model.EstadoGeneral;
import coes.com.example.CoesDental.model.HistorialClinico;
import coes.com.example.CoesDental.model.Paciente;
import coes.com.example.CoesDental.repository.HistorialClinicoRepository;
import coes.com.example.CoesDental.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import coes.com.example.CoesDental.model.Rol;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private HistorialClinicoRepository historialClinicoRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public Paciente registrarPaciente(PacienteRequestDTO dto) {
        if (pacienteRepository.existsByDNI(dto.getDNI())) {
            throw new RuntimeException("El DNI ya está registrado");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new RuntimeException("La contraseña es obligatoria para registrar al paciente");
        }

        Paciente nuevoPaciente = new Paciente();
        nuevoPaciente.setDNI(dto.getDNI());
        nuevoPaciente.setNombre(dto.getNombre());
        nuevoPaciente.setFechaNacimiento(dto.getFechaNacimiento());
        nuevoPaciente.setEstado(EstadoGeneral.ACTIVO);
        nuevoPaciente.setCorreo(dto.getCorreo());
        nuevoPaciente.setTelefono(dto.getTelefono());
        nuevoPaciente.setPassword(passwordEncoder.encode(dto.getPassword()));
        nuevoPaciente.setRol(Rol.Paciente);

        nuevoPaciente.setFechaInscripcion(LocalDateTime.now());

        Paciente pacienteGuardado = pacienteRepository.save(nuevoPaciente);

        HistorialClinico historial = new HistorialClinico();
        historial.setPaciente(pacienteGuardado);
        historialClinicoRepository.save(historial);

        return pacienteGuardado;
    }

    @Transactional(readOnly = true)
    public List<Paciente> listarPacientesActivos() {
        return pacienteRepository.findByEstado(EstadoGeneral.ACTIVO);
    }

    @Transactional
    public void eliminarLogicoPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        paciente.setEstado(EstadoGeneral.INACTIVO);
        pacienteRepository.save(paciente);
    }

    @Transactional
    public Paciente actualizarPaciente(Long id, PacienteRequestDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con el ID: " + id));

        paciente.setNombre(dto.getNombre());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setCorreo(dto.getCorreo());
        paciente.setTelefono(dto.getTelefono());

        return pacienteRepository.save(paciente);
    }

    public Paciente buscarPorDni(String dni) {
        return pacienteRepository.findByDNI(dni)
                .orElseThrow(() -> new RuntimeException("No se encontró ningún paciente con el DNI: " + dni));
    }

    public List<Paciente> listarPacientesInactivos() {
        return pacienteRepository.findByEstado(coes.com.example.CoesDental.model.EstadoGeneral.INACTIVO);
    }

    @Transactional
    public Paciente reactivarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        paciente.setEstado(coes.com.example.CoesDental.model.EstadoGeneral.ACTIVO);
        return pacienteRepository.save(paciente);
    }
}