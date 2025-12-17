package com.example.demo.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Modelos.Habitacion;
import com.example.demo.Modelos.Huesped;
import com.example.demo.Modelos.Reserva;
import com.example.demo.Repositorios.HabitacionRepository;
import com.example.demo.Repositorios.HuespedRepository;
import com.example.demo.Repositorios.ReservaRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/hotel/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    // ============================================
    // GET /hotel/reservas - Obtener todas las reservas
    // ============================================
    @GetMapping
    public ResponseEntity<List<Reserva>> getAllReservas() {
        try {
            List<Reserva> reservas = reservaRepository.findAll();

            if (reservas.isEmpty()) {
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.noContent().build();
            }

            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().build();
        }
    }

    // ============================================
    // GET /hotel/reservas/{id} - Obtener reserva por ID
    // ============================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservaById(@PathVariable("id") int id) {
        try {
            Optional<Reserva> reservaData = reservaRepository.findById(id);

            if (reservaData.isPresent()) {
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.ok(reservaData.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Reserva no encontrada con ID: " + id);
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al obtener la reserva: " + e.getMessage());
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // ============================================
    // POST /hotel/reservas - Crear nueva reserva
    // ============================================
    @PostMapping
    public ResponseEntity<?> createReserva(@RequestBody Reserva reserva) {
        try {
            // Validar campos requeridos
            if (reserva.getFechaEntrada() == null || reserva.getFechaSalida() == null) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Fecha de entrada y salida son requeridas");
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.badRequest().body(error);
            }

            // Validar que la fecha de salida sea posterior a la de entrada
            if (reserva.getFechaSalida().isBefore(reserva.getFechaEntrada()) ||
                    reserva.getFechaSalida().isEqual(reserva.getFechaEntrada())) {

                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "La fecha de salida debe ser posterior a la fecha de entrada");
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.badRequest().body(error);
            }

            // Validar que el huésped existe
            if (reserva.getHuesped() == null || reserva.getHuesped().getHuespedId() == 0) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Huésped es requerido");
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.badRequest().body(error);
            }

            Optional<Huesped> huespedData = huespedRepository.findById(reserva.getHuesped().getHuespedId());
            if (!huespedData.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Huésped no encontrado con ID: " + reserva.getHuesped().getHuespedId());
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.badRequest().body(error);
            }

            // Validar que la habitación existe
            if (reserva.getHabitacion() == null || reserva.getHabitacion().getHabitacionId() == 0) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Habitación es requerida");
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.badRequest().body(error);
            }

            Optional<Habitacion> habitacionData = habitacionRepository
                    .findById(reserva.getHabitacion().getHabitacionId());
            if (!habitacionData.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Habitación no encontrada con ID: " + reserva.getHabitacion().getHabitacionId());
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.badRequest().body(error);
            }

            Habitacion habitacion = habitacionData.get();

            // Validar que la habitación esté disponible
            if (!"Disponible".equalsIgnoreCase(habitacion.getEstado())) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "La habitación no está disponible. Estado actual: " + habitacion.getEstado());
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }

            // Validar que no haya reservas superpuestas para la misma habitación
            // NOTA: Este método debe existir en tu ReservaRepository
            List<Reserva> reservasSuperpuestas = reservaRepository.findReservasSuperpuestas(
                    reserva.getFechaEntrada(), reserva.getFechaSalida());

            boolean haySuperposicion = reservasSuperpuestas.stream()
                    .anyMatch(r -> r.getHabitacion().getHabitacionId() == habitacion.getHabitacionId());

            if (haySuperposicion) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "La habitación ya está reservada en las fechas seleccionadas");
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }

            // Establecer las relaciones
            reserva.setHuesped(huespedData.get());
            reserva.setHabitacion(habitacion);

            // Cambiar estado de la habitación
            habitacion.setEstado("Reservada");
            habitacionRepository.save(habitacion);

            // Crear la reserva
            Reserva nuevaReserva = reservaRepository.save(reserva);
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al crear la reserva: " + e.getMessage());
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // ============================================
    // PUT /hotel/reservas/{id} - Actualizar reserva
    // ============================================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReserva(@PathVariable("id") int id,
            @RequestBody Reserva reserva) {
        try {
            Optional<Reserva> reservaData = reservaRepository.findById(id);

            if (reservaData.isPresent()) {
                Reserva existingReserva = reservaData.get();

                // Validar fechas
                if (reserva.getFechaSalida() != null && reserva.getFechaEntrada() != null) {
                    if (reserva.getFechaSalida().isBefore(reserva.getFechaEntrada())) {
                        Map<String, String> error = new HashMap<>();
                        error.put("mensaje", "La fecha de salida debe ser posterior a la fecha de entrada");
                        // CORREGIDO: Sin ambigüedad
                        return ResponseEntity.badRequest().body(error);
                    }
                }

                // Actualizar campos
                if (reserva.getFechaEntrada() != null) {
                    existingReserva.setFechaEntrada(reserva.getFechaEntrada());
                }

                if (reserva.getFechaSalida() != null) {
                    existingReserva.setFechaSalida(reserva.getFechaSalida());
                }

                if (reserva.getEstado() != null && !reserva.getEstado().isEmpty()) {
                    existingReserva.setEstado(reserva.getEstado());
                }

                if (reserva.getTotalReserva() > 0) {
                    existingReserva.setTotalReserva(reserva.getTotalReserva());
                }

                if (reserva.getCantidadAdultos() > 0) {
                    existingReserva.setCantidadAdultos(reserva.getCantidadAdultos());
                }

                if (reserva.getCantidadNinos() >= 0) {
                    existingReserva.setCantidadNinos(reserva.getCantidadNinos());
                }

                Reserva updatedReserva = reservaRepository.save(existingReserva);
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.ok(updatedReserva);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Reserva no encontrada con ID: " + id);
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al actualizar la reserva: " + e.getMessage());
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // ============================================
    // DELETE /hotel/reservas/{id} - Eliminar reserva
    // ============================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReserva(@PathVariable("id") int id) {
        try {
            Optional<Reserva> reservaData = reservaRepository.findById(id);

            if (reservaData.isPresent()) {
                Reserva reserva = reservaData.get();

                // Liberar la habitación si está reservada
                if (reserva.getHabitacion() != null &&
                        "Reservada".equalsIgnoreCase(reserva.getHabitacion().getEstado())) {

                    Habitacion habitacion = reserva.getHabitacion();
                    habitacion.setEstado("Disponible");
                    habitacionRepository.save(habitacion);
                }

                reservaRepository.deleteById(id);

                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Reserva eliminada exitosamente");
                response.put("id", id);
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Reserva no encontrada con ID: " + id);
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al eliminar la reserva: " + e.getMessage());
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // ============================================
    // PUT /hotel/reservas/{id}/cancelar - Cancelar reserva
    // ============================================
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarReserva(@PathVariable("id") int id) {
        try {
            Optional<Reserva> reservaData = reservaRepository.findById(id);

            if (reservaData.isPresent()) {
                Reserva reserva = reservaData.get();

                // Verificar si ya está cancelada
                if ("Cancelada".equalsIgnoreCase(reserva.getEstado())) {
                    Map<String, String> error = new HashMap<>();
                    error.put("mensaje", "La reserva ya está cancelada");
                    // CORREGIDO: Sin ambigüedad
                    return ResponseEntity.badRequest().body(error);
                }

                // Cambiar estado de la reserva
                reserva.setEstado("Cancelada");

                // Liberar la habitación
                if (reserva.getHabitacion() != null) {
                    Habitacion habitacion = reserva.getHabitacion();
                    habitacion.setEstado("Disponible");
                    habitacionRepository.save(habitacion);
                }

                Reserva reservaCancelada = reservaRepository.save(reserva);

                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Reserva cancelada exitosamente");
                response.put("reserva", reservaCancelada);
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Reserva no encontrada con ID: " + id);
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al cancelar la reserva: " + e.getMessage());
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // ============================================
    // PUT /hotel/reservas/{id}/confirmar - Confirmar reserva
    // ============================================
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarReserva(@PathVariable("id") int id) {
        try {
            Optional<Reserva> reservaData = reservaRepository.findById(id);

            if (reservaData.isPresent()) {
                Reserva reserva = reservaData.get();

                // Verificar si ya está confirmada
                if ("Confirmada".equalsIgnoreCase(reserva.getEstado())) {
                    Map<String, String> error = new HashMap<>();
                    error.put("mensaje", "La reserva ya está confirmada");
                    // CORREGIDO: Sin ambigüedad
                    return ResponseEntity.badRequest().body(error);
                }

                // Cambiar estado de la reserva
                reserva.setEstado("Confirmada");

                Reserva reservaConfirmada = reservaRepository.save(reserva);

                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Reserva confirmada exitosamente");
                response.put("reserva", reservaConfirmada);
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Reserva no encontrada con ID: " + id);
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al confirmar la reserva: " + e.getMessage());
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // ============================================
    // GET /hotel/reservas/estado/{estado} - Buscar por estado
    // ============================================
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> getReservasByEstado(@PathVariable("estado") String estado) {
        try {
            List<Reserva> reservas = reservaRepository.findByEstado(estado);

            if (reservas.isEmpty()) {
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.noContent().build();
            }

            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al buscar reservas: " + e.getMessage());
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // ============================================
    // GET /hotel/reservas/huesped/{huespedId} - Buscar por huésped
    // ============================================
    @GetMapping("/huesped/{huespedId}")
    public ResponseEntity<?> getReservasByHuesped(@PathVariable("huespedId") int huespedId) {
        try {
            List<Reserva> reservas = reservaRepository.findByHuesped_HuespedId(huespedId);

            if (reservas.isEmpty()) {
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.noContent().build();
            }

            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al buscar reservas: " + e.getMessage());
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // ============================================
    // GET /hotel/reservas/estadisticas - Obtener estadísticas
    // ============================================
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        try {
            Map<String, Object> estadisticas = new HashMap<>();

            long totalReservas = reservaRepository.count();
            long pendientes = reservaRepository.countByEstado("Pendiente");
            long confirmadas = reservaRepository.countByEstado("Confirmada");
            long canceladas = reservaRepository.countByEstado("Cancelada");
            long completadas = reservaRepository.countByEstado("Completada");

            estadisticas.put("totalReservas", totalReservas);
            estadisticas.put("reservasPendientes", pendientes);
            estadisticas.put("reservasConfirmadas", confirmadas);
            estadisticas.put("reservasCanceladas", canceladas);
            estadisticas.put("reservasCompletadas", completadas);

            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().build();
        }
    }

    // ============================================
    // GET /hotel/reservas/disponibilidad - Verificar disponibilidad
    // ============================================
    @GetMapping("/disponibilidad")
    public ResponseEntity<?> verificarDisponibilidad(
            @RequestParam("fechaEntrada") String fechaEntradaStr,
            @RequestParam("fechaSalida") String fechaSalidaStr) {

        try {
            LocalDate fechaEntrada = LocalDate.parse(fechaEntradaStr);
            LocalDate fechaSalida = LocalDate.parse(fechaSalidaStr);

            // Validar fechas
            if (fechaSalida.isBefore(fechaEntrada) || fechaSalida.isEqual(fechaEntrada)) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "La fecha de salida debe ser posterior a la fecha de entrada");
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.badRequest().body(error);
            }

            // Obtener todas las habitaciones
            List<Habitacion> todasHabitaciones = habitacionRepository.findAll();

            // Obtener reservas superpuestas
            List<Reserva> reservasSuperpuestas = reservaRepository.findReservasSuperpuestas(
                    fechaEntrada, fechaSalida);

            // Filtrar habitaciones disponibles
            List<Habitacion> habitacionesDisponibles = todasHabitaciones.stream()
                    .filter(habitacion -> {
                        // Verificar que la habitación esté disponible
                        if (!"Disponible".equalsIgnoreCase(habitacion.getEstado())) {
                            return false;
                        }

                        // Verificar que no tenga reservas superpuestas
                        boolean tieneReservaSuperpuesta = reservasSuperpuestas.stream()
                                .anyMatch(reserva -> reserva.getHabitacion().getHabitacionId() == habitacion
                                        .getHabitacionId());

                        return !tieneReservaSuperpuesta;
                    })
                    .toList();

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("fechaEntrada", fechaEntrada);
            respuesta.put("fechaSalida", fechaSalida);
            respuesta.put("dias", fechaEntrada.until(fechaSalida).getDays());
            respuesta.put("habitacionesDisponibles", habitacionesDisponibles.size());
            respuesta.put("habitaciones", habitacionesDisponibles);

            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al verificar disponibilidad: " + e.getMessage());
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().body(error);
        }
    }
}