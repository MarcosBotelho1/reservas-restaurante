package com.restaurante.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private int numero;
    
    private int capacidade;
    private int proximaReservaIntervalo = 120; // Em minutos
    
    @OneToMany(mappedBy = "mesa")
    private List<Reserva> reservas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public int getProximaReservaIntervalo() {
        return proximaReservaIntervalo;
    }

    public void setProximaReservaIntervalo(int proximaReservaIntervalo) {
        this.proximaReservaIntervalo = proximaReservaIntervalo;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

}