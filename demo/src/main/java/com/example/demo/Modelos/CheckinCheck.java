package com.example.demo.Modelos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CHECKIN_CHECK")
public class CheckinCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_id")
    private int checkId;

    @Column(name = "tipo", nullable = false, length = 10)
    private String tipo;

    @Column(name = "fecha_hora", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    // Constructores
    public CheckinCheck() {
        this.fechaHora = LocalDateTime.now();
    }

    public CheckinCheck(String tipo, Reserva reserva, Empleado empleado) {
        this();
        this.tipo = tipo;
        this.reserva = reserva;
        this.empleado = empleado;
    }

    // Getters y Setters
    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    // Métodos de conveniencia
    public int getReservaId() {
        return reserva != null ? reserva.getReservaId() : 0;
    }

    public int getEmpleadoId() {
        return empleado != null ? empleado.getEmpleadoId() : 0;
    }

    @Override
    public String toString() {
        return "CheckinCheck{" +
                "checkId=" + checkId +
                ", tipo='" + tipo + '\'' +
                ", fechaHora=" + fechaHora +
                ", reservaId=" + getReservaId() +
                ", empleadoId=" + getEmpleadoId() +
                '}';
    }
}
