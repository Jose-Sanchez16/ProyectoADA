package com.example.demo.Modelos;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "habitaciones")
public class Habitacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int habitacionId;
    
    private String numero;
    private int piso;
    private String estado;
    private String caracteristicas;
    private int tarifaId;
    
    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL)
    private List<Reserva> reservas;
    
    // Constructor vacío
    public Habitacion() {
    }
    
    // Constructor con parámetros (AGREGA ESTE)
    public Habitacion(String numero, int piso, String estado, String caracteristicas, int tarifaId) {
        this.numero = numero;
        this.piso = piso;
        this.estado = estado;
        this.caracteristicas = caracteristicas;
        this.tarifaId = tarifaId;
    }
    
    // Getters y setters...
    public int getHabitacionId() { return habitacionId; }
    public void setHabitacionId(int habitacionId) { this.habitacionId = habitacionId; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public int getPiso() { return piso; }
    public void setPiso(int piso) { this.piso = piso; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(String caracteristicas) { this.caracteristicas = caracteristicas; }
    
    public int getTarifaId() { return tarifaId; }
    public void setTarifaId(int tarifaId) { this.tarifaId = tarifaId; }
    
    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }
}