package com.example.demo.Modelos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;


@Entity
@Table(name = "RESERVA_SERVICIO")
public class ReservaServicio {


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "reserva_servicio_id")
   private int reservaServicioId;


   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "reserva_id", nullable = false)
   @JsonBackReference
   private Reserva reserva;


   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "servicio_id", nullable = false)
   private Servicio servicio;


   @Column(name = "cantidad", nullable = false)
   private int cantidad = 1;


   @Column(name = "precio_unitario", nullable = false)
   private double precioUnitario;


   // Constructores
   public ReservaServicio() {
       this.cantidad = 1;
   }


   public ReservaServicio(Reserva reserva, Servicio servicio, int cantidad, double precioUnitario) {
       this.reserva = reserva;
       this.servicio = servicio;
       this.cantidad = cantidad;
       this.precioUnitario = precioUnitario;
   }


   // Getters y Setters
   public int getReservaServicioId() {
       return reservaServicioId;
   }


   public void setReservaServicioId(int reservaServicioId) {
       this.reservaServicioId = reservaServicioId;
   }


   public Reserva getReserva() {
       return reserva;
   }


   public void setReserva(Reserva reserva) {
       this.reserva = reserva;
   }


   public Servicio getServicio() {
       return servicio;
   }


   public void setServicio(Servicio servicio) {
       this.servicio = servicio;
   }


   public int getCantidad() {
       return cantidad;
   }


   public void setCantidad(int cantidad) {
       this.cantidad = cantidad;
   }


   public double getPrecioUnitario() {
       return precioUnitario;
   }


   public void setPrecioUnitario(double precioUnitario) {
       this.precioUnitario = precioUnitario;
   }


   // Métodos de conveniencia
   public double getSubtotal() {
       return cantidad * precioUnitario;
   }


   // Métodos para compatibilidad
   public int getReservaId() {
       return reserva != null ? reserva.getReservaId() : 0;
   }


   public int getServicioId() {
       return servicio != null ? servicio.getServicioId() : 0;
   }


   @Override
   public String toString() {
       return "ReservaServicio{" +
               "reservaServicioId=" + reservaServicioId +
               ", reservaId=" + getReservaId() +
               ", servicioId=" + getServicioId() +
               ", cantidad=" + cantidad +
               ", precioUnitario=" + precioUnitario +
               ", subtotal=" + getSubtotal() +
               '}';
   }
}

