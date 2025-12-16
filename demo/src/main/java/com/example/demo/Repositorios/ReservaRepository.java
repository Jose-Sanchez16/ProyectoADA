package com.example.demo.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Modelos.Reserva;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    // Buscar reservas por estado
    List<Reserva> findByEstado(String estado);

    // Buscar reservas por huésped
    List<Reserva> findByHuesped_HuespedId(int huespedId);

    // Buscar reservas por habitación
    List<Reserva> findByHabitacion_HabitacionId(int habitacionId);

    // Buscar reservas por rango de fechas
    List<Reserva> findByFechaEntradaBetween(LocalDate inicio, LocalDate fin);

    // Buscar reservas activas en una fecha específica
    @Query("SELECT r FROM Reserva r WHERE r.fechaEntrada <= :fecha AND r.fechaSalida >= :fecha AND r.estado NOT IN ('Cancelada', 'Completada')")
    List<Reserva> findReservasActivasEnFecha(@Param("fecha") LocalDate fecha);

    // Buscar reservas que se superponen con un rango de fechas
    @Query("SELECT r FROM Reserva r WHERE " +
            "(:fechaEntrada BETWEEN r.fechaEntrada AND r.fechaSalida OR " +
            ":fechaSalida BETWEEN r.fechaEntrada AND r.fechaSalida OR " +
            "r.fechaEntrada BETWEEN :fechaEntrada AND :fechaSalida) " +
            "AND r.estado NOT IN ('Cancelada')")
    List<Reserva> findReservasSuperpuestas(
            @Param("fechaEntrada") LocalDate fechaEntrada,
            @Param("fechaSalida") LocalDate fechaSalida);

    // Contar reservas por estado
    long countByEstado(String estado);

    // Obtener reservas pendientes de confirmación
    List<Reserva> findByEstadoOrderByFechaEntradaAsc(String estado);
}
