package com.example.demo.Modelos;

import jakarta.persistence.*;


@Entity
@Table(name = "EMPLEADO")
public class Empleado {


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "empleado_id")
   private int empleadoId;


   @Column(name = "nombre", nullable = false, length = 100)
   private String nombre;


   @Column(name = "apellido", nullable = false, length = 100)
   private String apellido;


   @Column(name = "rol", nullable = false, length = 50)
   private String rol;


   @Column(name = "usuario", nullable = false, unique = true, length = 50)
   private String usuario;


   @Column(name = "contrasena", nullable = false, length = 100)
   private String contrasena;


   @Column(name = "telefono", length = 20)
   private String telefono;


   @Column(name = "email", length = 150)
   private String email;


   // Constructores
   public Empleado() {
   }


   public Empleado(String nombre, String apellido, String rol,
                  String usuario, String contrasena, String telefono, String email) {
       this.nombre = nombre;
       this.apellido = apellido;
       this.rol = rol;
       this.usuario = usuario;
       this.contrasena = contrasena;
       this.telefono = telefono;
       this.email = email;
   }


   // Getters y Setters
   public int getEmpleadoId() {
       return empleadoId;
   }


   public void setEmpleadoId(int empleadoId) {
       this.empleadoId = empleadoId;
   }


   public String getNombre() {
       return nombre;
   }


   public void setNombre(String nombre) {
       this.nombre = nombre;
   }


   public String getApellido() {
       return apellido;
   }


   public void setApellido(String apellido) {
       this.apellido = apellido;
   }


   public String getRol() {
       return rol;
   }


   public void setRol(String rol) {
       this.rol = rol;
   }


   public String getUsuario() {
       return usuario;
   }


   public void setUsuario(String usuario) {
       this.usuario = usuario;
   }


   public String getContrasena() {
       return contrasena;
   }


   public void setContrasena(String contrasena) {
       this.contrasena = contrasena;
   }


   public String getTelefono() {
       return telefono;
   }


   public void setTelefono(String telefono) {
       this.telefono = telefono;
   }


   public String getEmail() {
       return email;
   }


   public void setEmail(String email) {
       this.email = email;
   }


   @Override
   public String toString() {
       return "Empleado{" +
               "empleadoId=" + empleadoId +
               ", nombre='" + nombre + '\'' +
               ", apellido='" + apellido + '\'' +
               ", rol='" + rol + '\'' +
               ", usuario='" + usuario + '\'' +
               ", contrasena='" + contrasena + '\'' +
               ", telefono='" + telefono + '\'' +
               ", email='" + email + '\'' +
               '}';
   }
}

