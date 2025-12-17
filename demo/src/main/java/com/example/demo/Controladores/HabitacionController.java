package com.example.demo.Controladores;

import com.example.demo.Modelos.Habitacion;
import com.example.demo.Repositorios.HabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/hotel/habitaciones")
@CrossOrigin(origins = "*")
public class HabitacionController {

    @Autowired
    private HabitacionRepository habitacionRepository;

    // --- GET /hotel/habitaciones - Obtener todas las habitaciones ---
    @GetMapping
    public ResponseEntity<List<Habitacion>> getAllHabitaciones() {
        try {
            List<Habitacion> habitaciones = habitacionRepository.findAll();
            if (habitaciones.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(habitaciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // --- GET /hotel/habitaciones/{id} - Obtener habitación por ID ---
    @GetMapping("/{id}")
    public ResponseEntity<?> getHabitacionById(@PathVariable("id") int id) {
        try {
            Optional<Habitacion> habitacionData = habitacionRepository.findById(id);

            if (habitacionData.isPresent()) {
                return ResponseEntity.ok(habitacionData.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Habitación no encontrada con ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al obtener la habitación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // --- POST /hotel/habitaciones - Crear nueva habitación ---
    @PostMapping
    public ResponseEntity<?> createHabitacion(@RequestBody Habitacion habitacion) {
        try {
            // Validar campos requeridos
            if (habitacion.getNumero() == null || habitacion.getNumero().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "El número de habitación es requerido");
                return ResponseEntity.badRequest().body(error);
            }

            if (habitacionRepository.existsByNumero(habitacion.getNumero())) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Ya existe una habitación con el número: " + habitacion.getNumero());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }

            if (habitacion.getPiso() <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "El piso debe ser un número positivo");
                return ResponseEntity.badRequest().body(error);
            }

            if (habitacion.getEstado() == null || habitacion.getEstado().trim().isEmpty()) {
                habitacion.setEstado("Disponible");
            }

            Habitacion nuevaHabitacion = habitacionRepository.save(habitacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaHabitacion);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al crear la habitación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // --- PUT /hotel/habitaciones/{id} - Actualizar habitación ---
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHabitacion(@PathVariable("id") int id,
            @RequestBody Habitacion habitacion) {
        try {
            Optional<Habitacion> habitacionData = habitacionRepository.findById(id);

            if (habitacionData.isPresent()) {
                Habitacion existingHabitacion = habitacionData.get();

                if (habitacion.getNumero() != null &&
                        !habitacion.getNumero().equals(existingHabitacion.getNumero()) &&
                        habitacionRepository.existsByNumero(habitacion.getNumero())) {
                    Map<String, String> error = new HashMap<>();
                    error.put("mensaje", "Ya existe una habitación con el número: " + habitacion.getNumero());
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
                }

                // Actualizar campos
                if (habitacion.getNumero() != null && !habitacion.getNumero().isEmpty()) {
                    existingHabitacion.setNumero(habitacion.getNumero());
                }

                if (habitacion.getPiso() > 0) {
                    existingHabitacion.setPiso(habitacion.getPiso());
                }

                if (habitacion.getEstado() != null && !habitacion.getEstado().isEmpty()) {
                    existingHabitacion.setEstado(habitacion.getEstado());
                }

                if (habitacion.getCaracteristicas() != null) {
                    existingHabitacion.setCaracteristicas(habitacion.getCaracteristicas());
                }

                existingHabitacion.setTarifaId(habitacion.getTarifaId());

                Habitacion updatedHabitacion = habitacionRepository.save(existingHabitacion);
                return ResponseEntity.ok(updatedHabitacion);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Habitación no encontrada con ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al actualizar la habitación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // --- DELETE /hotel/habitaciones/{id} - Eliminar habitación ---
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHabitacion(@PathVariable("id") int id) {
        try {
            Optional<Habitacion> habitacionData = habitacionRepository.findById(id);

            if (habitacionData.isPresent()) {
                Habitacion habitacion = habitacionData.get();

                if (habitacion.getReservas() != null && !habitacion.getReservas().isEmpty()) {
                    boolean tieneReservasActivas = habitacion.getReservas().stream()
                            .anyMatch(r -> r != null && r.getEstado() != null &&
                                    !"Cancelada".equalsIgnoreCase(r.getEstado()) &&
                                    !"Completada".equalsIgnoreCase(r.getEstado()));

                    if (tieneReservasActivas) {
                        Map<String, String> error = new HashMap<>();
                        error.put("mensaje", "No se puede eliminar la habitación porque tiene reservas activas");
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
                    }
                }

                habitacionRepository.deleteById(id);

                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Habitación eliminada exitosamente");
                response.put("id", id);
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Habitación no encontrada con ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al eliminar la habitación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // --- GET /hotel/habitaciones/numero/{numero} - Buscar por número ---
    @GetMapping("/numero/{numero}")
    public ResponseEntity<?> getHabitacionByNumero(@PathVariable("numero") String numero) {
        try {
            Optional<Habitacion> habitacionData = habitacionRepository.findByNumero(numero);

            if (habitacionData.isPresent()) {
                return ResponseEntity.ok(habitacionData.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Habitación no encontrada con número: " + numero);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al buscar la habitación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // --- GET /hotel/habitaciones/estado/{estado} - Buscar por estado ---
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> getHabitacionesByEstado(@PathVariable("estado") String estado) {
        try {
            List<Habitacion> habitaciones = habitacionRepository.findByEstado(estado);

            if (habitaciones.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(habitaciones);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al buscar habitaciones: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // --- GET /hotel/habitaciones/disponibles - Obtener habitaciones disponibles
    // ---
    @GetMapping("/disponibles")
    public ResponseEntity<?> getHabitacionesDisponibles() {
        try {
            List<Habitacion> habitaciones = habitacionRepository.findHabitacionesDisponibles();

            if (habitaciones.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(habitaciones);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al obtener habitaciones disponibles: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // --- GET /hotel/habitaciones/ocupadas - Obtener habitaciones ocupadas ---
    @GetMapping("/ocupadas")
    public ResponseEntity<?> getHabitacionesOcupadas() {
        try {
            List<Habitacion> habitaciones = habitacionRepository.findHabitacionesOcupadas();

            if (habitaciones.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(habitaciones);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al obtener habitaciones ocupadas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // --- GET /hotel/habitaciones/piso/{piso} - Buscar por piso ---
    @GetMapping("/piso/{piso}")
    public ResponseEntity<?> getHabitacionesByPiso(@PathVariable("piso") int piso) {
        try {
            List<Habitacion> habitaciones = habitacionRepository.findByPiso(piso);

            if (habitaciones.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(habitaciones);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al buscar habitaciones: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // --- PUT /hotel/habitaciones/{id}/estado - Cambiar estado de habitación ---
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstadoHabitacion(@PathVariable("id") int id,
            @RequestBody Map<String, String> estadoRequest) {
        try {
            String nuevoEstado = estadoRequest.get("estado");

            if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "El estado es requerido");
                return ResponseEntity.badRequest().body(error);
            }

            Optional<Habitacion> habitacionData = habitacionRepository.findById(id);

            if (habitacionData.isPresent()) {
                Habitacion habitacion = habitacionData.get();
                String estadoAnterior = habitacion.getEstado();

                if ("Ocupada".equalsIgnoreCase(estadoAnterior) &&
                        "Disponible".equalsIgnoreCase(nuevoEstado) &&
                        habitacion.getReservas() != null) {
                    boolean tieneReservasActivas = habitacion.getReservas().stream()
                            .anyMatch(r -> r != null && r.getEstado() != null &&
                                    !"Cancelada".equalsIgnoreCase(r.getEstado()) &&
                                    !"Completada".equalsIgnoreCase(r.getEstado()));

                    if (tieneReservasActivas) {
                        Map<String, String> error = new HashMap<>();
                        error.put("mensaje", "No se puede cambiar a disponible porque hay reservas activas");
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
                    }
                }

                habitacion.setEstado(nuevoEstado);
                Habitacion habitacionActualizada = habitacionRepository.save(habitacion);

                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Estado de habitación actualizado exitosamente");
                response.put("estadoAnterior", estadoAnterior);
                response.put("estadoNuevo", nuevoEstado);
                response.put("habitacion", habitacionActualizada);

                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Habitación no encontrada con ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al cambiar el estado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // --- GET /hotel/habitaciones/estadisticas - Obtener estadísticas ---
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        try {
            Map<String, Object> estadisticas = new HashMap<>();

            long totalHabitaciones = habitacionRepository.count();
            List<Habitacion> todas = habitacionRepository.findAll();

            long disponibles = todas.stream()
                    .filter(h -> "Disponible".equalsIgnoreCase(h.getEstado()))
                    .count();
            long ocupadas = todas.stream()
                    .filter(h -> "Ocupada".equalsIgnoreCase(h.getEstado()))
                    .count();
            long reservadas = todas.stream()
                    .filter(h -> "Reservada".equalsIgnoreCase(h.getEstado()))
                    .count();
            long mantenimiento = todas.stream()
                    .filter(h -> "Mantenimiento".equalsIgnoreCase(h.getEstado()))
                    .count();
            long limpieza = todas.stream()
                    .filter(h -> "Limpieza".equalsIgnoreCase(h.getEstado()))
                    .count();

            estadisticas.put("totalHabitaciones", totalHabitaciones);
            estadisticas.put("habitacionesDisponibles", disponibles);
            estadisticas.put("habitacionesOcupadas", ocupadas);
            estadisticas.put("habitacionesReservadas", reservadas);
            estadisticas.put("habitacionesEnMantenimiento", mantenimiento);
            estadisticas.put("habitacionesEnLimpieza", limpieza);

            double tasaOcupacion = totalHabitaciones > 0 ? (double) ocupadas / totalHabitaciones * 100 : 0;
            estadisticas.put("tasaOcupacion", Math.round(tasaOcupacion * 100.0) / 100.0);

            return ResponseEntity.ok(estadisticas);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("mensaje", "Error al obtener estadísticas: " + e.getMessage());
            error.put("error", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

        }
    }
}