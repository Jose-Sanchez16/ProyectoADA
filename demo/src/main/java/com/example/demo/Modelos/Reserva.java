package com.example.demo.Modelos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "RESERVA")
public class Reserva {


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "reserva_id")
   private int reservaId;


   @Column(name = "fecha_entrada", nullable = false)
   @JsonFormat(pattern = "yyyy-MM-dd")
   private LocalDate fechaEntrada;


   @Column(name = "fecha_salida", nullable = false)
   @JsonFormat(pattern = "yyyy-MM-dd")
   private LocalDate fechaSalida;


   @Column(name = "estado", nullable = false, length = 20)
   private String estado = "Pendiente";


   @Column(name = "total_reserva", nullable = false)
   private double totalReserva;


   @Column(name = "cantidad_adultos", nullable = false)
   private int cantidadAdultos = 1;


   @Column(name = "cantidad_ninos")
   private int cantidadNinos = 0;


   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "huesped_id", nullable = false)
   @JsonBackReference
   private Huesped huesped;


   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "habitacion_id", nullable = false)
   private Habitacion habitacion;


   @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @JsonManagedReference
   private List<ReservaServicio> servicios = new ArrayList<>();


   // Constructores
   public Reserva() {
       this.fechaEntrada = LocalDate.now();
       this.fechaSalida = LocalDate.now().plusDays(1);
       this.estado = "Pendiente";
   }


   public Reserva(LocalDate fechaEntrada, LocalDate fechaSalida,
                  double totalReserva, int cantidadAdultos,
                  int cantidadNinos, Huesped huesped, Habitacion habitacion) {
       this();
       this.fechaEntrada = fechaEntrada;
       this.fechaSalida = fechaSalida;
       this.totalReserva = totalReserva;
       this.cantidadAdultos = cantidadAdultos;
       this.cantidadNinos = cantidadNinos;
       this.huesped = huesped;
       this.habitacion = habitacion;
   }


   // Getters y Setters
   public int getReservaId() {
       return reservaId;
   }


   public void setReservaId(int reservaId) {
       this.reservaId = reservaId;
   }


   public LocalDate getFechaEntrada() {
       return fechaEntrada;
   }


   public void setFechaEntrada(LocalDate fechaEntrada) {
       this.fechaEntrada = fechaEntrada;
   }


   public LocalDate getFechaSalida() {
       return fechaSalida;
   }


   public void setFechaSalida(LocalDate fechaSalida) {
       this.fechaSalida = fechaSalida;
   }


   public String getEstado() {
       return estado;
   }


   public void setEstado(String estado) {
       this.estado = estado;
   }


   public double getTotalReserva() {
       return totalReserva;
   }


   public void setTotalReserva(double totalReserva) {
       this.totalReserva = totalReserva;
   }


   public int getCantidadAdultos() {
       return cantidadAdultos;
   }


   public void setCantidadAdultos(int cantidadAdultos) {
       this.cantidadAdultos = cantidadAdultos;
   }


   public int getCantidadNinos() {
       return cantidadNinos;
   }


   public void setCantidadNinos(int cantidadNinos) {
       this.cantidadNinos = cantidadNinos;
   }


   public Huesped getHuesped() {
       return huesped;
   }


   public void setHuesped(Huesped huesped) {
       this.huesped = huesped;
   }


   public Habitacion getHabitacion() {
       return habitacion;
   }


   public void setHabitacion(Habitacion habitacion) {
       this.habitacion = habitacion;
   }


   public List<ReservaServicio> getServicios() {
       return servicios;
   }


   public void setServicios(List<ReservaServicio> servicios) {
       this.servicios = servicios;
   }


   // Métodos de conveniencia
   public int getDiasEstadia() {
       if (fechaEntrada != null && fechaSalida != null) {
           return (int) java.time.temporal.ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
       }
       return 0;
   }


   public double getTotalConServicios() {
       double total = totalReserva;
       if (servicios != null) {
           for (ReservaServicio servicio : servicios) {
               total += servicio.getSubtotal();
           }
       }
       return total;
   }


   // Métodos para compatibilidad con el código antiguo
   public int getHuespedId() {
       return huesped != null ? huesped.getHuespedId() : 0;
   }


   public void setHuespedId(int huespedId) {
       // Este método es solo para compatibilidad
       // En una implementación real, necesitarías buscar el Huesped
   }


   public int getHabitacionId() {
       return habitacion != null ? habitacion.getHabitacionId() : 0;
   }


   public void setHabitacionId(int habitacionId) {
       // Este método es solo para compatibilidad
   }


   @Override
   public String toString() {
       return "Reserva{" +
               "reservaId=" + reservaId +
               ", fechaEntrada=" + fechaEntrada +
               ", fechaSalida=" + fechaSalida +
               ", estado='" + estado + '\'' +
               ", totalReserva=" + totalReserva +
               ", cantidadAdultos=" + cantidadAdultos +
               ", cantidadNinos=" + cantidadNinos +
               ", huespedId=" + getHuespedId() +
               ", habitacionId=" + getHabitacionId() +
               '}';
   }
}
