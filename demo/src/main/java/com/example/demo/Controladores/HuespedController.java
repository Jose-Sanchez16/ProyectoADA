package com.example.demo.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Modelos.Huesped;
import com.example.demo.Repositorios.HuespedRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/hotel/huespedes")
@CrossOrigin(origins = "*")
public class HuespedController {
    
    @Autowired
    private HuespedRepository huespedRepository;
    
    // ============================================
    // GET /hotel/huespedes - Obtener todos los huéspedes
    // ============================================
    @GetMapping
    public ResponseEntity<List<Huesped>> getAllHuespedes() {
        try {
            List<Huesped> huespedes = huespedRepository.findAll();
            
            if (huespedes.isEmpty()) {
                // CORREGIDO: Usando métodos estáticos
                return ResponseEntity.noContent().build();
            }
            
            // CORREGIDO: Usando métodos estáticos
            return ResponseEntity.ok(huespedes);
        } catch (Exception e) {
            // CORREGIDO: Usando métodos estáticos
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // ============================================
    // GET /hotel/huespedes/{id} - Obtener huésped por ID
    // ============================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getHuespedById(@PathVariable("id") int id) {
        try {
            Optional<Huesped> huespedData = huespedRepository.findById(id);
            
            if (huespedData.isPresent()) {
                // CORREGIDO: Usando métodos estáticos
                return ResponseEntity.ok(huespedData.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Huésped no encontrado con ID: " + id);
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // ============================================
    // POST /hotel/huespedes - Crear nuevo huésped
    // ============================================
    @PostMapping
    public ResponseEntity<?> createHuesped(@RequestBody Huesped huesped) {
        try {
            // Validar que el DNI no exista
            if (huespedRepository.existsByDniPasaporte(huesped.getDniPasaporte())) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Ya existe un huésped con el DNI/pasaporte: " + huesped.getDniPasaporte());
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }
            
            // Validar campos requeridos
            if (huesped.getNombre() == null || huesped.getNombre().trim().isEmpty() ||
                huesped.getApellido() == null || huesped.getApellido().trim().isEmpty() ||
                huesped.getEmail() == null || huesped.getEmail().trim().isEmpty()) {
                
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Nombre, apellido y email son campos requeridos");
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.badRequest().body(error);
            }
            
            Huesped nuevoHuesped = huespedRepository.save(huesped);
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoHuesped);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al crear el huésped: " + e.getMessage());
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    // ============================================
    // PUT /hotel/huespedes/{id} - Actualizar huésped
    // ============================================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHuesped(@PathVariable("id") int id, 
                                           @RequestBody Huesped huesped) {
        try {
            Optional<Huesped> huespedData = huespedRepository.findById(id);
            
            if (huespedData.isPresent()) {
                Huesped existingHuesped = huespedData.get();
                
                // Verificar si se está cambiando el DNI
                if (!existingHuesped.getDniPasaporte().equals(huesped.getDniPasaporte())) {
                    // Verificar que el nuevo DNI no exista
                    Optional<Huesped> huespedConMismoDni = huespedRepository
                        .findByDniPasaporte(huesped.getDniPasaporte());
                    
                    if (huespedConMismoDni.isPresent() && 
                        huespedConMismoDni.get().getHuespedId() != id) {
                        
                        Map<String, String> error = new HashMap<>();
                        error.put("mensaje", "Ya existe otro huésped con el DNI/pasaporte: " + 
                                 huesped.getDniPasaporte());
                        // CORREGIDO: Sin ambigüedad
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
                    }
                }
                
                // Actualizar campos
                existingHuesped.setDniPasaporte(huesped.getDniPasaporte());
                existingHuesped.setNombre(huesped.getNombre());
                existingHuesped.setApellido(huesped.getApellido());
                existingHuesped.setEmail(huesped.getEmail());
                existingHuesped.setTelefono(huesped.getTelefono());
                existingHuesped.setPais(huesped.getPais());
                existingHuesped.setTipoHuesped(huesped.getTipoHuesped());
                
                Huesped updatedHuesped = huespedRepository.save(existingHuesped);
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.ok(updatedHuesped);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Huésped no encontrado con ID: " + id);
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al actualizar el huésped: " + e.getMessage());
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    // ============================================
    // DELETE /hotel/huespedes/{id} - Eliminar huésped
    // ============================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHuesped(@PathVariable("id") int id) {
        try {
            Optional<Huesped> huespedData = huespedRepository.findById(id);
            
            if (huespedData.isPresent()) {
                Huesped huesped = huespedData.get();
                
                // Verificar si el huésped tiene reservas activas
                if (huesped.getReservas() != null && !huesped.getReservas().isEmpty()) {
                    // Verificar si hay reservas no canceladas
                    boolean tieneReservasActivas = huesped.getReservas().stream()
                        .anyMatch(r -> r != null && r.getEstado() != null &&
                                !"Cancelada".equalsIgnoreCase(r.getEstado()) && 
                                !"Completada".equalsIgnoreCase(r.getEstado()));
                    
                    if (tieneReservasActivas) {
                        Map<String, String> error = new HashMap<>();
                        error.put("mensaje", "No se puede eliminar el huésped porque tiene reservas activas");
                        // CORREGIDO: Sin ambigüedad
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
                    }
                }
                
                huespedRepository.deleteById(id);
                
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Huésped eliminado exitosamente");
                response.put("id", id);
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Huésped no encontrado con ID: " + id);
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al eliminar el huésped: " + e.getMessage());
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    // ============================================
    // GET /hotel/huespedes/dni/{dni} - Buscar por DNI
    // ============================================
    @GetMapping("/dni/{dni}")
    public ResponseEntity<?> getHuespedByDni(@PathVariable("dni") String dni) {
        try {
            Optional<Huesped> huespedData = huespedRepository.findByDniPasaporte(dni);
            
            if (huespedData.isPresent()) {
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.ok(huespedData.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Huésped no encontrado con DNI/pasaporte: " + dni);
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al buscar el huésped: " + e.getMessage());
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    // ============================================
    // GET /hotel/huespedes/buscar/{nombre} - Buscar por nombre/apellido
    // ============================================
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<?> searchHuespedes(@PathVariable("nombre") String nombre) {
        try {
            List<Huesped> huespedes = huespedRepository.findByNombreCompletoContainingIgnoreCase(nombre);
            
            if (huespedes.isEmpty()) {
                // CORREGIDO: Sin ambigüedad
                return ResponseEntity.noContent().build();
            }
            
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.ok(huespedes);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error en la búsqueda: " + e.getMessage());
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    // ============================================
    // GET /hotel/huespedes/estadisticas - Obtener estadísticas
    // ============================================
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        try {
            Map<String, Object> estadisticas = new HashMap<>();
            
            long totalHuespedes = huespedRepository.count();
            long regularCount = huespedRepository.countByTipoHuesped("Regular");
            long vipCount = huespedRepository.countByTipoHuesped("VIP");
            long corporativoCount = huespedRepository.countByTipoHuesped("Corporativo");
            
            estadisticas.put("totalHuespedes", totalHuespedes);
            estadisticas.put("huespedesRegulares", regularCount);
            estadisticas.put("huespedesVIP", vipCount);
            estadisticas.put("huespedesCorporativos", corporativoCount);
            
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            // CORREGIDO: Sin ambigüedad
            return ResponseEntity.internalServerError().build();
        }
    }
}