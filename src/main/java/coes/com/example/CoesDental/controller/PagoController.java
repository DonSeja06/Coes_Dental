package coes.com.example.CoesDental.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coes.com.example.CoesDental.dto.PagoRequestDTO;
import coes.com.example.CoesDental.dto.PagoResponseDTO;
import coes.com.example.CoesDental.service.PagoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista')")
    public ResponseEntity<List<PagoResponseDTO>> getAllPagos() {
        return ResponseEntity.ok(pagoService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista')")
    public ResponseEntity<PagoResponseDTO> getPagoById(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista')")
    public ResponseEntity<PagoResponseDTO> createPago(@Valid @RequestBody PagoRequestDTO pagoRequestDTO) {
        return new ResponseEntity<>(pagoService.save(pagoRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista')")
    public ResponseEntity<PagoResponseDTO> updatePago(@PathVariable Long id, @Valid @RequestBody PagoRequestDTO pagoRequestDTO) {
        return ResponseEntity.ok(pagoService.update(id, pagoRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('Admin', 'Recepcionista')")
    public ResponseEntity<Void> deletePago(@PathVariable Long id) {
        pagoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
