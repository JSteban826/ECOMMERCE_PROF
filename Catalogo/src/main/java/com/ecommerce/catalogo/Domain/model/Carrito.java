package com.ecommerce.catalogo.Domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Carrito {

    private Long idCarrito;
    private String idUsuario;
    private EstadoCarrito estado;
    private BigDecimal total;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaCompra;
    private List<ItemCarrito> items;
}