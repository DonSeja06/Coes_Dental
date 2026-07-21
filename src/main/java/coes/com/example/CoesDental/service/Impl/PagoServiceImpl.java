package coes.com.example.CoesDental.service.Impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coes.com.example.CoesDental.dto.PagoRequestDTO;
import coes.com.example.CoesDental.dto.PagoResponseDTO;
import coes.com.example.CoesDental.model.Cita;
import coes.com.example.CoesDental.model.Pago;
import coes.com.example.CoesDental.repository.CitaRepository;
import coes.com.example.CoesDental.repository.PagoRepository;
import coes.com.example.CoesDental.service.PagoService;

@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Override
    public List<PagoResponseDTO> findAll() {
        return pagoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PagoResponseDTO findById(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));
        return mapToDTO(pago);
    }

    @Override
    public PagoResponseDTO save(PagoRequestDTO pagoRequestDTO) {
        Cita cita = citaRepository.findById(pagoRequestDTO.getCitaId())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + pagoRequestDTO.getCitaId()));

        // Check if there's already a Pago for this cita
        Pago pago = pagoRepository.findAll().stream()
                .filter(p -> p.getCita().getId().equals(cita.getId()))
                .findFirst()
                .orElse(new Pago());

        if (pago.getId() != null && pago.getEstadoPago() == coes.com.example.CoesDental.model.EstadoPago.PAGADO) {
            throw new RuntimeException("Esta cita ya tiene un pago registrado y completado.");
        }

        pago.setCita(cita);
        pago.setMonto(pagoRequestDTO.getMonto());
        pago.setMetodoPago(pagoRequestDTO.getMetodoPago());
        pago.setEstadoPago(pagoRequestDTO.getEstadoPago());
        
        if (pagoRequestDTO.getFechaPago() != null) {
            pago.setFechaPago(pagoRequestDTO.getFechaPago());
        } else {
            pago.setFechaPago(LocalDateTime.now());
        }

        Pago savedPago = pagoRepository.save(pago);
        return mapToDTO(savedPago);
    }

    @Override
    public PagoResponseDTO update(Long id, PagoRequestDTO pagoRequestDTO) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));

        Cita cita = citaRepository.findById(pagoRequestDTO.getCitaId())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + pagoRequestDTO.getCitaId()));

        pago.setCita(cita);
        pago.setMonto(pagoRequestDTO.getMonto());
        pago.setMetodoPago(pagoRequestDTO.getMetodoPago());
        pago.setEstadoPago(pagoRequestDTO.getEstadoPago());
        
        if (pagoRequestDTO.getFechaPago() != null) {
            pago.setFechaPago(pagoRequestDTO.getFechaPago());
        }

        Pago updatedPago = pagoRepository.save(pago);
        return mapToDTO(updatedPago);
    }

    @Override
    public void delete(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new RuntimeException("Pago no encontrado con id: " + id);
        }
        pagoRepository.deleteById(id);
    }

    private PagoResponseDTO mapToDTO(Pago pago) {
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setId(pago.getId());
        dto.setCitaId(pago.getCita().getId());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setEstadoPago(pago.getEstadoPago());
        dto.setFechaPago(pago.getFechaPago());
        if (pago.getCita().getPaciente() != null) {
            dto.setNombrePaciente(pago.getCita().getPaciente().getNombre());
        }
        if (pago.getCita().getOdontologo() != null) {
            dto.setNombreOdontologo(pago.getCita().getOdontologo().getNombre());
        }
        if (pago.getCita().getFechaCita() != null) {
            dto.setFechaCita(pago.getCita().getFechaCita());
        }
        return dto;
    }
}
