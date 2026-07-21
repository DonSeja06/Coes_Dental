package coes.com.example.CoesDental.service;

import coes.com.example.CoesDental.model.EstadoGeneral;
import coes.com.example.CoesDental.model.Recepcionista;
import coes.com.example.CoesDental.model.Rol;
import coes.com.example.CoesDental.repository.RecepcionistaRepository;
import coes.com.example.CoesDental.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecepcionistaService {

    @Autowired
    private RecepcionistaRepository recepcionistaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public Recepcionista registrar(Recepcionista r) {
        if (usuarioRepository.existsByCorreo(r.getCorreo())) {
            throw new RuntimeException("El correo ya está en uso");
        }
        if (usuarioRepository.existsByDNI(r.getDNI())) {
            throw new RuntimeException("El DNI ya está en uso");
        }
        r.setPassword(passwordEncoder.encode(r.getPassword()));
        r.setRol(Rol.Recepcionista);
        r.setEstado(EstadoGeneral.ACTIVO);
        return recepcionistaRepository.save(r);
    }

    public List<Recepcionista> listarTodos() {
        return recepcionistaRepository.findAll();
    }
}
