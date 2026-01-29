package com.example.demo.Data;

import com.example.demo.Modelos.Habitacion;
import com.example.demo.Modelos.Huesped;
import com.example.demo.Modelos.Reserva;
import com.example.demo.Repositorios.HabitacionRepository;
import com.example.demo.Repositorios.HuespedRepository;
import com.example.demo.Repositorios.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Override
    public void run(String... args) throws Exception {
        // Limpiar datos existentes (opcional, cuidado en producción)
        // reservaRepository.deleteAll();
        // habitacionRepository.deleteAll();
        // huespedRepository.deleteAll();

        // Verificar si ya hay datos
        if (huespedRepository.count() == 0) {
            cargarHuespedes();
        }

        if (habitacionRepository.count() == 0) {
            cargarHabitaciones();
        }

        if (reservaRepository.count() == 0) {
            cargarReservas();
        }

        System.out.println("=== DATOS DE PRUEBA CARGADOS ===");
        System.out.println("Huéspedes: " + huespedRepository.count());
        System.out.println("Habitaciones: " + habitacionRepository.count());
        System.out.println("Reservas: " + reservaRepository.count());
    }

    private void cargarHuespedes() {
        List<Huesped> huespedes = Arrays.asList(
                // Constructor debe ser: dniPasaporte, nombre, apellido, email, telefono, pais,
                // tipoHuesped
                new Huesped("12345678A", "Juan", "Pérez", "juan.perez@email.com", "600111222", "España", "Regular"),

                new Huesped("87654321B", "María", "Gómez", "maria.gomez@email.com", "600222333", "España", "Regular"),

                new Huesped("11223344C", "Carlos", "López", "carlos.lopez@email.com", "+52 5512345678", "México",
                        "VIP"),

                new Huesped("55667788D", "Ana", "Martínez", "ana.martinez@email.com", "+54 91123456789", "Argentina",
                        "Regular"),

                new Huesped("99887766E", "Luis", "Rodríguez", "luis.rodriguez@email.com", "+57 3101234567", "Colombia",
                        "Corporativo"));

        huespedRepository.saveAll(huespedes);
        System.out.println("Huéspedes cargados: " + huespedes.size());
    }

    private void cargarHabitaciones() {
        List<Habitacion> habitaciones = Arrays.asList(
                // Habitaciones estándar
                new Habitacion("101", 1, "Disponible", "Cama doble, TV, Baño privado", 1),
                new Habitacion("102", 1, "Disponible", "Cama doble, TV, Baño privado, Vista calle", 1),
                new Habitacion("103", 1, "Disponible", "Dos camas individuales, TV", 1),

                new Habitacion("201", 2, "Disponible", "Cama king size, TV 55', Jacuzzi", 2),
                new Habitacion("202", 2, "Reservada", "Suite junior, Terraza, Vista al mar", 2),
                new Habitacion("203", 2, "Disponible", "Cama doble, Mini bar", 2),

                new Habitacion("301", 3, "Ocupada", "Suite presidencial, 2 habitaciones, Sala", 3),
                new Habitacion("302", 3, "Mantenimiento", "Cama doble, TV, Baño privado", 1),
                new Habitacion("303", 3, "Limpieza", "Cama individual, Baño compartido", 1),

                new Habitacion("401", 4, "Disponible", "Suite familiar, 4 camas individuales", 4),
                new Habitacion("402", 4, "Disponible", "Habitación accesible, Cama doble", 1),
                new Habitacion("403", 4, "Reservada", "Cama king size, Balcón, Vista piscina", 2));

        habitacionRepository.saveAll(habitaciones);
        System.out.println("Habitaciones cargadas: " + habitaciones.size());
    }

    private void cargarReservas() {
        List<Huesped> huespedes = huespedRepository.findAll();
        List<Habitacion> habitaciones = habitacionRepository.findAll();

        if (!huespedes.isEmpty() && !habitaciones.isEmpty()) {
            List<Reserva> reservas = Arrays.asList(
                    // Reserva confirmada
                    new Reserva(
                            LocalDate.now().plusDays(1),
                            LocalDate.now().plusDays(3),
                            "Confirmada",
                            2,
                            1,
                            300.0,
                            huespedes.get(0),
                            habitaciones.get(0)),

                    // Reserva pendiente
                    new Reserva(
                            LocalDate.now().plusDays(5),
                            LocalDate.now().plusDays(7),
                            "Pendiente",
                            1,
                            0,
                            200.0,
                            huespedes.get(1),
                            habitaciones.get(3)),

                    // Reserva cancelada
                    new Reserva(
                            LocalDate.now().minusDays(10),
                            LocalDate.now().minusDays(8),
                            "Cancelada",
                            2,
                            2,
                            400.0,
                            huespedes.get(2),
                            habitaciones.get(6)),

                    // Reserva completada
                    new Reserva(
                            LocalDate.now().minusDays(5),
                            LocalDate.now().minusDays(3),
                            "Completada",
                            1,
                            0,
                            150.0,
                            huespedes.get(3),
                            habitaciones.get(9)));

            // Cambiar estado de habitaciones reservadas
            for (Reserva reserva : reservas) {
                if ("Confirmada".equals(reserva.getEstado()) || "Pendiente".equals(reserva.getEstado())) {
                    Habitacion habitacion = reserva.getHabitacion();
                    habitacion.setEstado("Reservada");
                    habitacionRepository.save(habitacion);
                }
            }

            reservaRepository.saveAll(reservas);
            System.out.println("Reservas cargadas: " + reservas.size());
        }
    }
}
