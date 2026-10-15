package com.ecommerce.catalogo.Infraestructure.driver_adapter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_carrito_producto",
                        columnNames = {
                                "id_carrito",
                                "id_producto"
                        }
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemCarritoData {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItemCarrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CarritoData carrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ProductoData producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal precioUnitario;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal subtotal;
}