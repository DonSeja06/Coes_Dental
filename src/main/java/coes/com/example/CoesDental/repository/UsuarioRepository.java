package coes.com.example.CoesDental.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coes.com.example.CoesDental.model.EstadoGeneral;
import coes.com.example.CoesDental.model.Usuario;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Optional<Usuario> findByCorreoAndEstado(String correo, EstadoGeneral estado);

    boolean existsByCorreo(String correo);
    boolean existsByDNI(String DNI);
    boolean existsByRol(coes.com.example.CoesDental.model.Rol rol);

    Optional<Usuario> findByCorreo(String correo);
}
