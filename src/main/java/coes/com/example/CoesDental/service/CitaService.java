package coes.com.example.CoesDental.service;

import coes.com.example.CoesDental.dto.CitaRequestDTO;
import coes.com.example.CoesDental.model.*;
import coes.com.example.CoesDental.repository.CitaRepository;
import coes.com.example.CoesDental.repository.ConsultorioRepository;
import coes.com.example.CoesDental.repository.OdontologoRepository;
import coes.com.example.CoesDental.repository.PacienteRepository;
import coes.com.example.CoesDental.repository.RegistroClinicoRepository;
import coes.com.example.CoesDental.repository.PagoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private OdontologoRepository odontologoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultorioRepository consultorioRepository;

    @Autowired
    private RegistroClinicoRepository registroClinicoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Transactional
    public Cita registrarCita(CitaRequestDTO dto) {

        Odontologo odontologo = odontologoRepository.findById(dto.getOdontologoId())
                .orElseThrow(() -> new RuntimeException("Odontólogo no encontrado"));
        if (odontologo.getEstado() != EstadoGeneral.ACTIVO) {
            throw new RuntimeException("El odontólogo no está activo en el sistema");
        }

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        if (paciente.getEstado() != EstadoGeneral.ACTIVO) {
            throw new RuntimeException("El paciente no está activo en el sistema");
        }

        Consultorio consultorio = consultorioRepository.findById(dto.getConsultorioId())
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado"));
        if (consultorio.getEstado() != EstadoGeneral.ACTIVO) {
            throw new RuntimeException("El consultorio no está disponible/activo");
        }

        long cruces = citaRepository.countCitasActivasPorOdontologoYFecha(dto.getOdontologoId(), dto.getFechaCita());
        if (cruces > 0) {
            throw new RuntimeException("El odontólogo ya tiene una cita programada en ese horario exacto");
        }

        long crucesConsultorio = citaRepository.countCitasActivasPorConsultorioYFecha(dto.getConsultorioId(),
                dto.getFechaCita());
        if (crucesConsultorio > 0) {
            throw new RuntimeException("El consultorio ya está reservado para otra cita en ese horario exacto");
        }

        Cita nuevaCita = new Cita();
        nuevaCita.setFechaCita(dto.getFechaCita());
        nuevaCita.setOdontologo(odontologo);
        nuevaCita.setPaciente(paciente);
        nuevaCita.setConsultorio(consultorio);
        nuevaCita.setEstado(EstadoCita.CREADA);

        return citaRepository.save(nuevaCita);
    }

    @Transactional
    public Cita solicitarCita(CitaRequestDTO dto) {
        Odontologo odontologo = odontologoRepository.findById(dto.getOdontologoId())
                .orElseThrow(() -> new RuntimeException("Odontólogo no encontrado"));
        if (odontologo.getEstado() != EstadoGeneral.ACTIVO) {
            throw new RuntimeException("El odontólogo no está activo en el sistema");
        }

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        if (paciente.getEstado() != EstadoGeneral.ACTIVO) {
            throw new RuntimeException("El paciente no está activo en el sistema");
        }

        long cruces = citaRepository.countCitasActivasPorOdontologoYFecha(dto.getOdontologoId(), dto.getFechaCita());
        if (cruces > 0) {
            throw new RuntimeException("El odontólogo ya tiene una cita programada en ese horario exacto");
        }

        List<coes.com.example.CoesDental.model.Consultorio> consultorios = consultorioRepository.findAll();
        coes.com.example.CoesDental.model.Consultorio consultorioDisponible = null;
        for (coes.com.example.CoesDental.model.Consultorio c : consultorios) {
            if (c.getEstado() == coes.com.example.CoesDental.model.EstadoGeneral.ACTIVO && citaRepository.countCitasActivasPorConsultorioYFecha(c.getId(), dto.getFechaCita()) == 0) {
                consultorioDisponible = c;
                break;
            }
        }

        if (consultorioDisponible == null) {
            throw new RuntimeException("No hay consultorios disponibles para esta fecha y hora");
        }

        Cita nuevaCita = new Cita();
        nuevaCita.setFechaCita(dto.getFechaCita());
        nuevaCita.setOdontologo(odontologo);
        nuevaCita.setPaciente(paciente);
        nuevaCita.setConsultorio(consultorioDisponible);
        nuevaCita.setEstado(EstadoCita.PENDIENTE_APROBACION);

        return citaRepository.save(nuevaCita);
    }

    @Transactional
    public Cita aprobarSolicitud(Long id, Long consultorioId) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (cita.getEstado() != EstadoCita.PENDIENTE_APROBACION) {
            throw new RuntimeException("La cita no está pendiente de aprobación");
        }

        Consultorio consultorio = consultorioRepository.findById(consultorioId)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado"));

        long crucesConsultorio = citaRepository.countCitasActivasPorConsultorioYFecha(consultorioId, cita.getFechaCita());
        if (crucesConsultorio > 0) {
            throw new RuntimeException("El consultorio ya está reservado para otra cita en ese horario exacto");
        }

        cita.setConsultorio(consultorio);
        cita.setEstado(EstadoCita.CREADA);
        return citaRepository.save(cita);
    }

    @Transactional
    public void rechazarSolicitud(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        
        if (cita.getEstado() != EstadoCita.PENDIENTE_APROBACION) {
            throw new RuntimeException("La cita no está pendiente de aprobación");
        }
        
        cita.setEstado(EstadoCita.CANCELADA);
        citaRepository.save(cita);
    }

    @Transactional(readOnly = true)
    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }

    @Transactional
    public void cancelarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        cita.setEstado(EstadoCita.CANCELADA);
        citaRepository.save(cita);
    }

    @Transactional
    public Cita posponerCita(Long idCita, LocalDateTime nuevaFecha) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (cita.getEstado() == EstadoCita.CANCELADA || cita.getEstado() == EstadoCita.FINALIZADA) {
            throw new RuntimeException("No se puede posponer una cita cancelada o ya finalizada");
        }

        long cruces = citaRepository.countCitasActivasPorOdontologoYFecha(cita.getOdontologo().getId(), nuevaFecha);
        if (cruces > 0) {
            throw new RuntimeException("El odontólogo ya tiene una cita programada en ese nuevo horario");
        }

        long crucesConsultorio = citaRepository.countCitasActivasPorConsultorioYFecha(
                cita.getConsultorio().getId(),
                nuevaFecha);

        if (crucesConsultorio > 0) {
            throw new RuntimeException("El consultorio ya está reservado para otra cita en ese horario");
        }

        cita.setFechaCita(nuevaFecha);
        cita.setEstado(EstadoCita.REPROGRAMADA);

        return citaRepository.save(cita);
    }

    @Transactional
    public Cita iniciarAtencion(Long idCita) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new RuntimeException("No se puede atender una cita cancelada");
        }

        cita.setEstado(EstadoCita.ATENCION);
        return citaRepository.save(cita);
    }

    @Transactional
    public RegistroClinico finalizarCita(Long idCita, String detalleAtencion) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (cita.getEstado() != EstadoCita.ATENCION) {
            throw new RuntimeException("La cita debe estar en ATENCION para poder ser finalizada");
        }

        cita.setEstado(EstadoCita.FINALIZADA);
        citaRepository.save(cita);

        HistorialClinico historial = cita.getPaciente().getHistorialClinico();
        if (historial == null) {
            throw new RuntimeException("El paciente no tiene un historial clínico asignado");
        }

        RegistroClinico registro = new RegistroClinico();
        registro.setDetalle(detalleAtencion);
        registro.setHistorial(historial);
        registro.setCita(cita);
        RegistroClinico savedRegistro = registroClinicoRepository.save(registro);

        Double costoEspecialidad = cita.getOdontologo().getEspecialidad().getCosto();
        if (costoEspecialidad == null) {
            costoEspecialidad = 0.0;
        }

        Pago pago = new Pago();
        pago.setCita(cita);
        pago.setMonto(costoEspecialidad);
        pago.setMetodoPago(MetodoPago.POR_DEFINIR);
        pago.setEstadoPago(EstadoPago.PENDIENTE);
        pagoRepository.save(pago);

        return savedRegistro;
    }

    public List<Cita> listarCitasPorOdontologo(Long odontologoId) {
        return citaRepository.findByOdontologoId(odontologoId);
    }

    public List<Cita> listarCitasPorPaciente(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }
}