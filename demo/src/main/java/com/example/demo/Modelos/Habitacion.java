package com.example.demo.Modelos;



import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "HABITACION")
public class Habitacion {


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "habitacion_id")
   private int habitacionId;


   @Column(name = "numero", nullable = false, unique = true, length = 10)
   private String numero;


   @Column(name = "piso", nullable = false)
   private int piso;


   @Column(name = "estado", nullable = false, length = 20)
   private String estado = "Disponible";


   @Column(name = "caracteristicas", columnDefinition = "TEXT")
   private String caracteristicas;


   @Column(name = "tarifa_id")
   private Integer tarifaId;


   @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @JsonManagedReference
   private List<Reserva> reservas = new ArrayList<>();


   // Constructores
   public Habitacion() {
       this.estado = "Disponible";
   }


   public Habitacion(String numero, int piso, String caracteristicas, Integer tarifaId) {
       this();
       this.numero = numero;
       this.piso = piso;
       this.caracteristicas = caracteristicas;
       this.tarifaId = tarifaId;
   }


   // Getters y Setters
   public int getHabitacionId() {
       return habitacionId;
   }


   public void setHabitacionId(int habitacionId) {
       this.habitacionId = habitacionId;
   }


   public String getNumero() {
       return numero;
   }


   public void setNumero(String numero) {
       this.numero = numero;
   }


   public int getPiso() {
       return piso;
   }


   public void setPiso(int piso) {
       this.piso = piso;
   }


   public String getEstado() {
       return estado;
   }


   public void setEstado(String estado) {
       this.estado = estado;
   }


   public String getCaracteristicas() {
       return caracteristicas;
   }


   public void setCaracteristicas(String caracteristicas) {
       this.caracteristicas = caracteristicas;
   }


   public Integer getTarifaId() {
       return tarifaId;
   }


   public void setTarifaId(Integer tarifaId) {
       this.tarifaId = tarifaId;
   }


   public List<Reserva> getReservas() {
       return reservas;
   }


   public void setReservas(List<Reserva> reservas) {
       this.reservas = reservas;
   }


   // Métodos de conveniencia
   public boolean estaDisponible() {
       return "Disponible".equalsIgnoreCase(estado);
   }


   public boolean estaOcupada() {
       return "Ocupada".equalsIgnoreCase(estado);
   }


   public boolean enMantenimiento() {
       return "Mantenimiento".equalsIgnoreCase(estado);
   }


   @Override
   public String toString() {
       return "Habitacion{" +
               "habitacionId=" + habitacionId +
               ", numero='" + numero + '\'' +
               ", piso=" + piso +
               ", estado='" + estado + '\'' +
               ", caracteristicas='" + caracteristicas + '\'' +
               ", tarifaId=" + tarifaId +
               '}';
   }
}

