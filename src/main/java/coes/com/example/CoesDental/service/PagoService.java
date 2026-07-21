package coes.com.example.CoesDental.service;

import java.util.List;

import coes.com.example.CoesDental.dto.PagoRequestDTO;
import coes.com.example.CoesDental.dto.PagoResponseDTO;

public interface PagoService {
    List<PagoResponseDTO> findAll();
    PagoResponseDTO findById(Long id);
    PagoResponseDTO save(PagoRequestDTO pagoRequestDTO);
    PagoResponseDTO update(Long id, PagoRequestDTO pagoRequestDTO);
    void delete(Long id);
}
