package coes.com.example.CoesDental.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import coes.com.example.CoesDental.model.Cita;

@Repository
public interface CitaRepository extends JpaRepository<Cita,Long>{
        @Query("SELECT c FROM Cita c WHERE c.odontologo.id = :odontologoId AND c.fechaCita BETWEEN :inicio AND :fin ORDER BY c.fechaCita ASC")
        List<Cita> findCitasByOdontologoAndRangoFechas(
                @Param("odontologoId") Long odontologoId, 
                @Param("inicio") LocalDateTime inicio, 
                @Param("fin") LocalDateTime fin
        );

        @Query("SELECT COUNT(c) FROM Cita c WHERE c.odontologo.id = :odontologoId AND c.fechaCita = :fechaCita AND c.estado != 'CANCELADA'")
        long countCitasActivasPorOdontologoYFecha(
                @Param("odontologoId") Long odontologoId, 
                @Param("fechaCita") LocalDateTime fechaCita
        );

        @Query("SELECT COUNT(c) FROM Cita c WHERE c.consultorio.id = :consultorioId AND c.fechaCita = :fechaCita AND c.estado != 'CANCELADA' AND c.estado != 'FINALIZADA'")
        long countCitasActivasPorConsultorioYFecha(
                @Param("consultorioId") Long consultorioId, 
                @Param("fechaCita") LocalDateTime fechaCita
        );

        List<Cita> findByOdontologoId(Long odontologoId);
}
