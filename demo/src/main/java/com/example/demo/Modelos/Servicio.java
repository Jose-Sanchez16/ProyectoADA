package com.example.demo.Modelos;

import jakarta.persistence.*;


@Entity
@Table(name = "SERVICIO")
public class Servicio {


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "servicio_id")
   private int servicioId;


   @Column(name = "nombre", nullable = false, length = 100)
   private String nombre;


   @Column(name = "descripcion", columnDefinition = "TEXT")
   private String descripcion;


   @Column(name = "precio", nullable = false)
   private double precio;


   @Column(name = "categoria_servicio", length = 50)
   private String categoriaServicio;


   // Constructores
   public Servicio() {
   }


   public Servicio(String nombre, String descripcion, double precio, String categoriaServicio) {
       this.nombre = nombre;
       this.descripcion = descripcion;
       this.precio = precio;
       this.categoriaServicio = categoriaServicio;
   }


   // Getters y Setters
   public int getServicioId() {
       return servicioId;
   }


   public void setServicioId(int servicioId) {
       this.servicioId = servicioId;
   }


   public String getNombre() {
       return nombre;
   }


   public void setNombre(String nombre) {
       this.nombre = nombre;
   }


   public String getDescripcion() {
       return descripcion;
   }


   public void setDescripcion(String descripcion) {
       this.descripcion = descripcion;
   }


   public double getPrecio() {
       return precio;
   }


   public void setPrecio(double precio) {
       this.precio = precio;
   }


   public String getCategoriaServicio() {
       return categoriaServicio;
   }


   public void setCategoriaServicio(String categoriaServicio) {
       this.categoriaServicio = categoriaServicio;
   }


   @Override
   public String toString() {
       return "Servicio{" +
               "servicioId=" + servicioId +
               ", nombre='" + nombre + '\'' +
               ", descripcion='" + descripcion + '\'' +
               ", precio=" + precio +
               ", categoriaServicio='" + categoriaServicio + '\'' +
               '}';
   }
}

