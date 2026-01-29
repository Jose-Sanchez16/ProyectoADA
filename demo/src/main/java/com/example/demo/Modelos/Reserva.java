package com.example.demo.Modelos;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "reservas")
public class Reserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservaId;
    
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private String estado;
    private int cantidadAdultos;
    private int cantidadNinos;
    private double totalReserva;
    
    @ManyToOne
    @JoinColumn(name = "huesped_id")
    @JsonBackReference
    private Huesped huesped;
    
    @ManyToOne
    @JoinColumn(name = "habitacion_id")
    @JsonBackReference
    private Habitacion habitacion;
    
    // Constructor vacío
    public Reserva() {
    }
    
    // Constructor con parámetros (AGREGA ESTE)
    public Reserva(LocalDate fechaEntrada, LocalDate fechaSalida, String estado, 
                   int cantidadAdultos, int cantidadNinos, double totalReserva,
                   Huesped huesped, Habitacion habitacion) {
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estado = estado;
        this.cantidadAdultos = cantidadAdultos;
        this.cantidadNinos = cantidadNinos;
        this.totalReserva = totalReserva;
        this.huesped = huesped;
        this.habitacion = habitacion;
    }
    
    // Getters y setters...
    public int getReservaId() { return reservaId; }
    public void setReservaId(int reservaId) { this.reservaId = reservaId; }
    
    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDate fechaEntrada) { this.fechaEntrada = fechaEntrada; }
    
    public LocalDate getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public int getCantidadAdultos() { return cantidadAdultos; }
    public void setCantidadAdultos(int cantidadAdultos) { this.cantidadAdultos = cantidadAdultos; }
    
    public int getCantidadNinos() { return cantidadNinos; }
    public void setCantidadNinos(int cantidadNinos) { this.cantidadNinos = cantidadNinos; }
    
    public double getTotalReserva() { return totalReserva; }
    public void setTotalReserva(double totalReserva) { this.totalReserva = totalReserva; }
    
    public Huesped getHuesped() { return huesped; }
    public void setHuesped(Huesped huesped) { this.huesped = huesped; }
    
    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }
}