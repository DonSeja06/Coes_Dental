package coes.com.example.CoesDental.controller;

import coes.com.example.CoesDental.dto.LoginRequest;
import coes.com.example.CoesDental.dto.LoginResponse;
import coes.com.example.CoesDental.model.Usuario;
import coes.com.example.CoesDental.repository.UsuarioRepository;
import coes.com.example.CoesDental.service.AdminService;
import coes.com.example.CoesDental.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/registrar-admin")
    public ResponseEntity<?> registrarAdminInit(@Valid @RequestBody Usuario admin) {
        try {
            Usuario nuevoAdmin = adminService.registrarAdmin(admin);
            return ResponseEntity.ok(nuevoAdmin);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getCorreo(),
                            request.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            String role = userDetails.getAuthorities().iterator().next().getAuthority();

            Usuario usuarioLogueado = usuarioRepository.findByCorreo(request.getCorreo()).get();

            return ResponseEntity.ok(
                    new LoginResponse(token, userDetails.getUsername(), role, usuarioLogueado.getId()));

        } catch (AuthenticationException e) {
            return ResponseEntity
                    .badRequest()
                    .body("CORREO O PASSWORD INCORRECTOS O USUARIO INACTIVO");
        }
    }

    @PutMapping("/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@Valid @RequestBody coes.com.example.CoesDental.dto.CambioPasswordRequest request) {
        try {
            // Verificar credenciales actuales
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getCorreo(),
                            request.getPasswordActual()));

            Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Codificar nueva contraseña y guardar
            // No podemos inyectar BCryptPasswordEncoder directamente si causa dependencia circular,
            // pero lo crearemos temporalmente o se asume inyectado (usamos el default)
            org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder = 
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
            usuario.setPassword(passwordEncoder.encode(request.getNuevaPassword()));
            usuarioRepository.save(usuario);

            return ResponseEntity.ok("Contraseña actualizada correctamente");
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("La contraseña actual es incorrecta");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}