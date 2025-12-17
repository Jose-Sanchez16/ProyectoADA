package com.example.demo.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Modelos.Habitacion;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {

    // Buscar habitación por número
    Optional<Habitacion> findByNumero(String numero);

    // Buscar habitaciones por estado
    List<Habitacion> findByEstado(String estado);

    // Buscar habitaciones por piso
    List<Habitacion> findByPiso(int piso);

    // Buscar habitaciones disponibles
    @Query("SELECT h FROM Habitacion h WHERE h.estado = 'Disponible'")
    List<Habitacion> findHabitacionesDisponibles();

    // Buscar habitaciones ocupadas
    @Query("SELECT h FROM Habitacion h WHERE h.estado = 'Ocupada'")
    List<Habitacion> findHabitacionesOcupadas();

    // Buscar habitaciones en mantenimiento
    @Query("SELECT h FROM Habitacion h WHERE h.estado = 'Mantenimiento'")
    List<Habitacion> findHabitacionesEnMantenimiento();

    // Buscar habitaciones por características
    @Query("SELECT h FROM Habitacion h WHERE LOWER(h.caracteristicas) LIKE LOWER(CONCAT('%', :caracteristica, '%'))")
    List<Habitacion> findByCaracteristicasContaining(@Param("caracteristica") String caracteristica);

    // Verificar si existe una habitación con número
    boolean existsByNumero(String numero);

    // Contar habitaciones por estado
    long countByEstado(String estado);
}
