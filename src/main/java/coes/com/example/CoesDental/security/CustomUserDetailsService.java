package coes.com.example.CoesDental.security;

import coes.com.example.CoesDental.model.EstadoGeneral;
import coes.com.example.CoesDental.model.Usuario;
import coes.com.example.CoesDental.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = repository.findByCorreoAndEstado(correo, EstadoGeneral.ACTIVO)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado o inactivo"));
        
        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name())
                .build();
    }
}