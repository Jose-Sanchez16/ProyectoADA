package com.example.demo.Modelos;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "HUESPED")
public class Huesped {


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "huesped_id")
   private int huespedId;


   @Column(name = "dni_pasaporte", nullable = false, unique = true, length = 20)
   private String dniPasaporte;


   @Column(name = "nombre", nullable = false, length = 100)
   private String nombre;


   @Column(name = "apellido", nullable = false, length = 100)
   private String apellido;


   @Column(name = "email", nullable = false, length = 150)
   private String email;


   @Column(name = "telefono", length = 20)
   private String telefono;


   @Column(name = "pais", length = 50)
   private String pais;


   @Column(name = "fecha_registro", nullable = false)
   @JsonFormat(pattern = "yyyy-MM-dd")
   private LocalDate fechaRegistro;


   @Column(name = "tipo_huesped", length = 20)
   private String tipoHuesped;


   @OneToMany(mappedBy = "huesped", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @JsonManagedReference
   private List<Reserva> reservas = new ArrayList<>();


   // Constructores
   public Huesped() {
       this.fechaRegistro = LocalDate.now();
       this.tipoHuesped = "Regular";
   }


   public Huesped(String dniPasaporte, String nombre, String apellido, String email,
                  String telefono, String pais, String tipoHuesped) {
       this();
       this.dniPasaporte = dniPasaporte;
       this.nombre = nombre;
       this.apellido = apellido;
       this.email = email;
       this.telefono = telefono;
       this.pais = pais;
       this.tipoHuesped = tipoHuesped;
   }


   // Getters y Setters
   public int getHuespedId() {
       return huespedId;
   }


   public void setHuespedId(int huespedId) {
       this.huespedId = huespedId;
   }


   public String getDniPasaporte() {
       return dniPasaporte;
   }


   public void setDniPasaporte(String dniPasaporte) {
       this.dniPasaporte = dniPasaporte;
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


   public String getEmail() {
       return email;
   }


   public void setEmail(String email) {
       this.email = email;
   }


   public String getTelefono() {
       return telefono;
   }


   public void setTelefono(String telefono) {
       this.telefono = telefono;
   }


   public String getPais() {
       return pais;
   }


   public void setPais(String pais) {
       this.pais = pais;
   }


   public LocalDate getFechaRegistro() {
       return fechaRegistro;
   }


   public void setFechaRegistro(LocalDate fechaRegistro) {
       this.fechaRegistro = fechaRegistro;
   }


   public String getTipoHuesped() {
       return tipoHuesped;
   }


   public void setTipoHuesped(String tipoHuesped) {
       this.tipoHuesped = tipoHuesped;
   }


   public List<Reserva> getReservas() {
       return reservas;
   }


   public void setReservas(List<Reserva> reservas) {
       this.reservas = reservas;
   }


   // Método de conveniencia
   public String getNombreCompleto() {
       return nombre + " " + apellido;
   }


   @Override
   public String toString() {
       return "Huesped{" +
               "huespedId=" + huespedId +
               ", dniPasaporte='" + dniPasaporte + '\'' +
               ", nombre='" + nombre + '\'' +
               ", apellido='" + apellido + '\'' +
               ", email='" + email + '\'' +
               ", telefono='" + telefono + '\'' +
               ", pais='" + pais + '\'' +
               ", fechaRegistro=" + fechaRegistro +
               ", tipoHuesped='" + tipoHuesped + '\'' +
               '}';
   }
}

