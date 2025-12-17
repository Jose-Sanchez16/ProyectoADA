package com.example.demo.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Modelos.Huesped;

import java.util.List;
import java.util.Optional;

@Repository
public interface HuespedRepository extends JpaRepository<Huesped, Integer> {

    // Buscar huésped por DNI/Pasaporte
    Optional<Huesped> findByDniPasaporte(String dniPasaporte);

    // Buscar huéspedes por apellido (insensible a mayúsculas)
    List<Huesped> findByApellidoContainingIgnoreCase(String apellido);

    // Buscar huéspedes por tipo
    List<Huesped> findByTipoHuesped(String tipoHuesped);

    // Buscar huéspedes por país
    List<Huesped> findByPais(String pais);

    // Buscar huéspedes por nombre completo
    @Query("SELECT h FROM Huesped h WHERE LOWER(CONCAT(h.nombre, ' ', h.apellido)) LIKE LOWER(CONCAT('%', :nombreCompleto, '%'))")
    List<Huesped> findByNombreCompletoContainingIgnoreCase(@Param("nombreCompleto") String nombreCompleto);

    // Contar huéspedes por tipo
    long countByTipoHuesped(String tipoHuesped);

    // Verificar si existe un huésped con DNI
    boolean existsByDniPasaporte(String dniPasaporte);
}
