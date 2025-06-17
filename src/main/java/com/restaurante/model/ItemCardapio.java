package com.restaurante.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ItemCardapio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String descricao;
    private double preco;
    
    @OneToMany(mappedBy = "itemCardapio")
    private List<ItemPedido> pedidos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public List<ItemPedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<ItemPedido> pedidos) {
        this.pedidos = pedidos;
    }
}