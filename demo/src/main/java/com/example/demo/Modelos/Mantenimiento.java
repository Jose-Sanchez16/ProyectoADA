package com.example.demo.Modelos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "MANTENIMIENTO")
public class Mantenimiento {


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "mantenimiento_id")
   private int mantenimientoId;


   @Column(name = "fecha", nullable = false)
   @JsonFormat(pattern = "yyyy-MM-dd")
   private LocalDate fecha;


   @Column(name = "descripcion", columnDefinition = "TEXT")
   private String descripcion;


   @Column(name = "estado", nullable = false, length = 20)
   private String estado = "Pendiente";


   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "habitacion_id", nullable = false)
   private Habitacion habitacion;


   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "empleado_id")
   private Empleado empleado;


   // Constructores
   public Mantenimiento() {
       this.fecha = LocalDate.now();
       this.estado = "Pendiente";
   }


   public Mantenimiento(String descripcion, Habitacion habitacion, Empleado empleado) {
       this();
       this.descripcion = descripcion;
       this.habitacion = habitacion;
       this.empleado = empleado;
   }


   // Getters y Setters
   public int getMantenimientoId() {
       return mantenimientoId;
   }


   public void setMantenimientoId(int mantenimientoId) {
       this.mantenimientoId = mantenimientoId;
   }


   public LocalDate getFecha() {
       return fecha;
   }


   public void setFecha(LocalDate fecha) {
       this.fecha = fecha;
   }


   public String getDescripcion() {
       return descripcion;
   }


   public void setDescripcion(String descripcion) {
       this.descripcion = descripcion;
   }


   public String getEstado() {
       return estado;
   }


   public void setEstado(String estado) {
       this.estado = estado;
   }


   public Habitacion getHabitacion() {
       return habitacion;
   }


   public void setHabitacion(Habitacion habitacion) {
       this.habitacion = habitacion;
   }


   public Empleado getEmpleado() {
       return empleado;
   }


   public void setEmpleado(Empleado empleado) {
       this.empleado = empleado;
   }


   // Métodos de conveniencia
   public int getHabitacionId() {
       return habitacion != null ? habitacion.getHabitacionId() : 0;
   }


   public int getEmpleadoId() {
       return empleado != null ? empleado.getEmpleadoId() : 0;
   }


   @Override
   public String toString() {
       return "Mantenimiento{" +
               "mantenimientoId=" + mantenimientoId +
               ", fecha=" + fecha +
               ", descripcion='" + descripcion + '\'' +
               ", estado='" + estado + '\'' +
               ", habitacionId=" + getHabitacionId() +
               ", empleadoId=" + getEmpleadoId() +
               '}';
   }
}
