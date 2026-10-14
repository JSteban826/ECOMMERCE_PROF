package com.ecommerce.catalogo.Infraestructure.mapper;

import com.ecommerce.catalogo.Domain.model.Carrito;
import com.ecommerce.catalogo.Domain.model.ItemCarrito;
import com.ecommerce.catalogo.Infraestructure.driver_adapter.CarritoData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MapperCarrito {

    private final MapperItemCarrito mapperItemCarrito;

    public CarritoData toData(Carrito carrito) {

        CarritoData carritoData = new CarritoData();

        carritoData.setIdCarrito(
                carrito.getIdCarrito()
        );

        carritoData.setIdUsuario(
                carrito.getIdUsuario()
        );

        carritoData.setEstado(
                carrito.getEstado()
        );

        carritoData.setTotal(
                carrito.getTotal()
        );

        carritoData.setFechaCreacion(
                carrito.getFechaCreacion()
        );

        carritoData.setFechaCompra(
                carrito.getFechaCompra()
        );

        return carritoData;
    }

    public Carrito toDomain(CarritoData carritoData) {

        Carrito carrito = new Carrito();

        carrito.setIdCarrito(
                carritoData.getIdCarrito()
        );

        carrito.setIdUsuario(
                carritoData.getIdUsuario()
        );

        carrito.setEstado(
                carritoData.getEstado()
        );

        carrito.setTotal(
                carritoData.getTotal()
        );

        carrito.setFechaCreacion(
                carritoData.getFechaCreacion()
        );

        carrito.setFechaCompra(
                carritoData.getFechaCompra()
        );

        List<ItemCarrito> items =
                carritoData.getItems()
                        .stream()
                        .map(mapperItemCarrito::toDomain)
                        .toList();

        carrito.setItems(items);

        return carrito;
    }
}