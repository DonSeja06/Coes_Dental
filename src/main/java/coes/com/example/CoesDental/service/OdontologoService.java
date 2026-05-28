package coes.com.example.CoesDental.service;

import coes.com.example.CoesDental.dto.OdontologoRequestDTO;
import coes.com.example.CoesDental.model.Especialidad;
import coes.com.example.CoesDental.model.EstadoGeneral;
import coes.com.example.CoesDental.model.Odontologo;
import coes.com.example.CoesDental.model.Rol;
import coes.com.example.CoesDental.repository.EspecialidadRepository;
import coes.com.example.CoesDental.repository.OdontologoRepository;
import coes.com.example.CoesDental.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OdontologoService {

    @Autowired
    private OdontologoRepository odontologoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public Odontologo registrarOdontologo(OdontologoRequestDTO dto) {
        
        if (usuarioRepository.existsByDNI(dto.getDNI())) {
            throw new RuntimeException("El DNI ya está registrado");
        }
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        if (odontologoRepository.existsByColegiatura(dto.getColegiatura())) {
            throw new RuntimeException("El número de colegiatura ya existe");
        }

        Especialidad especialidad = especialidadRepository.findById(dto.getEspecialidadId())
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        Odontologo nuevoOdontologo = new Odontologo();
        nuevoOdontologo.setDNI(dto.getDNI());
        nuevoOdontologo.setNombre(dto.getNombre());
        nuevoOdontologo.setCorreo(dto.getCorreo());
        nuevoOdontologo.setTelefono(dto.getTelefono());
        
        nuevoOdontologo.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        nuevoOdontologo.setRol(Rol.Odontologo);
        nuevoOdontologo.setEstado(EstadoGeneral.ACTIVO);
        
        nuevoOdontologo.setFechaRegistro(LocalDateTime.now());

        nuevoOdontologo.setColegiatura(dto.getColegiatura());
        nuevoOdontologo.setEspecialidad(especialidad);

        return odontologoRepository.save(nuevoOdontologo);
    }

    @Transactional(readOnly = true)
    public List<Odontologo> listarOdontologosActivos() {
        return odontologoRepository.findByEstado(EstadoGeneral.ACTIVO);
    }

    @Transactional
    public void eliminarLogicoOdontologo(Long id) {
        Odontologo odontologo = odontologoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Odontólogo no encontrado con el ID: " + id));
        
        odontologo.setEstado(EstadoGeneral.INACTIVO);

        odontologoRepository.save(odontologo);
    }

    @Transactional
    public Odontologo actualizarOdontologo(Long id, OdontologoRequestDTO dto) {
        Odontologo odontologo = odontologoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Odontólogo no encontrado con el ID: " + id));

        if (!odontologo.getCorreo().equals(dto.getCorreo())) {
            if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
                throw new RuntimeException("El nuevo correo ya está en uso por otro usuario");
            }
            odontologo.setCorreo(dto.getCorreo());
        }

        Especialidad especialidad = especialidadRepository.findById(dto.getEspecialidadId())
                .orElseThrow(() -> new RuntimeException("La especialidad seleccionada no existe"));

        odontologo.setNombre(dto.getNombre());
        odontologo.setTelefono(dto.getTelefono());

        odontologo.setColegiatura(dto.getColegiatura());
        odontologo.setEspecialidad(especialidad);

        return odontologoRepository.save(odontologo);
    }

    public Odontologo buscarPorDni(String dni) {
        return odontologoRepository.findByDNI(dni)
                .orElseThrow(() -> new RuntimeException("No se encontró ningún odontólogo con el DNI: " + dni));
    }

    public List<Odontologo> listarOdontologosInactivos() {
        return odontologoRepository.findByEstado(coes.com.example.CoesDental.model.EstadoGeneral.INACTIVO);
    }

    @Transactional
    public Odontologo reactivarOdontologo(Long id) {
        Odontologo odontologo = odontologoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Odontólogo no encontrado"));
        odontologo.setEstado(coes.com.example.CoesDental.model.EstadoGeneral.ACTIVO);
        return odontologoRepository.save(odontologo);
    }
}