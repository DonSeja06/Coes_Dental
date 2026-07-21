package coes.com.example.CoesDental.service;

import coes.com.example.CoesDental.model.EstadoGeneral;
import coes.com.example.CoesDental.model.Rol;
import coes.com.example.CoesDental.model.Usuario;
import coes.com.example.CoesDental.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public Usuario registrarAdmin(Usuario adminRequest) {
        if (usuarioRepository.existsByRol(Rol.Admin)) {
            throw new RuntimeException("Ya existe un administrador en el sistema");
        }
        if (usuarioRepository.existsByCorreo(adminRequest.getCorreo())) {
            throw new RuntimeException("El correo ya está en uso");
        }
        if (usuarioRepository.existsByDNI(adminRequest.getDNI())) {
            throw new RuntimeException("El DNI ya está en uso");
        }

        Usuario nuevoAdmin = new Usuario();
        nuevoAdmin.setDNI(adminRequest.getDNI());
        nuevoAdmin.setNombre(adminRequest.getNombre());
        nuevoAdmin.setCorreo(adminRequest.getCorreo());
        nuevoAdmin.setTelefono(adminRequest.getTelefono());
        nuevoAdmin.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
        nuevoAdmin.setRol(Rol.Admin);
        nuevoAdmin.setEstado(EstadoGeneral.ACTIVO);

        return usuarioRepository.save(nuevoAdmin);
    }
}