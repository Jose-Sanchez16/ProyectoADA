package com.example.demo.Modelos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "FACTURA")
public class Factura {


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "factura_id")
   private int facturaId;


   @Column(name = "fecha_emision", nullable = false)
   @JsonFormat(pattern = "yyyy-MM-dd")
   private LocalDate fechaEmision;


   @Column(name = "subtotal", nullable = false)
   private double subtotal;


   @Column(name = "impuestos", nullable = false)
   private double impuestos = 0.18;


   @Column(name = "total", nullable = false)
   private double total;


   @Column(name = "estado", nullable = false, length = 20)
   private String estado = "Pendiente";


   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "reserva_id", nullable = false)
   private Reserva reserva;


   // Constructores
   public Factura() {
       this.fechaEmision = LocalDate.now();
       this.estado = "Pendiente";
       this.impuestos = 0.18;
   }


   public Factura(double subtotal, Reserva reserva) {
       this();
       this.subtotal = subtotal;
       this.reserva = reserva;
       calcularTotal();
   }


   // Getters y Setters
   public int getFacturaId() {
       return facturaId;
   }


   public void setFacturaId(int facturaId) {
       this.facturaId = facturaId;
   }


   public LocalDate getFechaEmision() {
       return fechaEmision;
   }


   public void setFechaEmision(LocalDate fechaEmision) {
       this.fechaEmision = fechaEmision;
   }


   public double getSubtotal() {
       return subtotal;
   }


   public void setSubtotal(double subtotal) {
       this.subtotal = subtotal;
       calcularTotal();
   }


   public double getImpuestos() {
       return impuestos;
   }


   public void setImpuestos(double impuestos) {
       this.impuestos = impuestos;
       calcularTotal();
   }


   public double getTotal() {
       return total;
   }


   public void setTotal(double total) {
       this.total = total;
   }


   public String getEstado() {
       return estado;
   }


   public void setEstado(String estado) {
       this.estado = estado;
   }


   public Reserva getReserva() {
       return reserva;
   }


   public void setReserva(Reserva reserva) {
       this.reserva = reserva;
   }


   // Métodos de conveniencia
   private void calcularTotal() {
       this.total = this.subtotal + (this.subtotal * this.impuestos);
   }


   public int getReservaId() {
       return reserva != null ? reserva.getReservaId() : 0;
   }


   @Override
   public String toString() {
       return "Factura{" +
               "facturaId=" + facturaId +
               ", fechaEmision=" + fechaEmision +
               ", subtotal=" + subtotal +
               ", impuestos=" + impuestos +
               ", total=" + total +
               ", estado='" + estado + '\'' +
               ", reservaId=" + getReservaId() +
               '}';
   }
}
