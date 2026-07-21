package coes.com.example.CoesDental.controller;

import coes.com.example.CoesDental.model.Recepcionista;
import coes.com.example.CoesDental.service.RecepcionistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recepcionistas")
@CrossOrigin("*")
public class RecepcionistaController {

    @Autowired
    private RecepcionistaService recepcionistaService;

    @PreAuthorize("hasRole('Admin')")
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Recepcionista r) {
        try {
            return ResponseEntity.ok(recepcionistaService.registrar(r));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('Admin')")
    @GetMapping
    public ResponseEntity<List<Recepcionista>> listarTodos() {
        return ResponseEntity.ok(recepcionistaService.listarTodos());
    }
}
