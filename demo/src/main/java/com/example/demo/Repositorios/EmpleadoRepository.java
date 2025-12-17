package com.example.demo.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Modelos.Empleado;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    // Buscar empleado por usuario
    Optional<Empleado> findByUsuario(String usuario);

    // Buscar empleados por rol
    java.util.List<Empleado> findByRol(String rol);

    // Verificar credenciales
    Optional<Empleado> findByUsuarioAndContrasena(String usuario, String contrasena);

    // Verificar si existe un usuario
    boolean existsByUsuario(String usuario);
}
