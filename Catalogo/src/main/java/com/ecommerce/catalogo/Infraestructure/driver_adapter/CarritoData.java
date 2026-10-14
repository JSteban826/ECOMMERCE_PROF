package com.ecommerce.catalogo.Infraestructure.driver_adapter;

import com.ecommerce.catalogo.Domain.model.EstadoCarrito;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carritos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarritoData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idCarrito;

    @Column(
            nullable = false
    )
    private String idUsuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,
            length = 20)
    private EstadoCarrito estado;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal total = BigDecimal.ZERO;

    @Column(
            nullable = false
    )
    private LocalDateTime fechaCreacion;

    @Column
    private LocalDateTime fechaCompra;

    @OneToMany(
            mappedBy = "carrito",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ItemCarritoData> items = new ArrayList<>();
}