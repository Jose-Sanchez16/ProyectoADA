package com.example.demo.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Modelos.ReservaServicio;

import java.util.List;

@Repository
public interface ReservaServicioRepository extends JpaRepository<ReservaServicio, Integer> {

    // Buscar servicios por reserva
    List<ReservaServicio> findByReserva_ReservaId(int reservaId);

    // Buscar servicios por servicio
    List<ReservaServicio> findByServicio_ServicioId(int servicioId);

    // Calcular total de servicios por reserva
    // Nota: Esta consulta necesita una implementación personalizada
}
