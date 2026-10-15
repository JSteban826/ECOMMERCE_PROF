package com.ecommerce.catalogo.Infraestructure.mapper;

import com.ecommerce.catalogo.Domain.model.ItemCarrito;
import com.ecommerce.catalogo.Infraestructure.driver_adapter.CarritoData;
import com.ecommerce.catalogo.Infraestructure.driver_adapter.ItemCarritoData;
import com.ecommerce.catalogo.Infraestructure.driver_adapter.ProductoData;
import org.springframework.stereotype.Component;

@Component
public class MapperItemCarrito {

    public ItemCarritoData toData(
            ItemCarrito itemCarrito,
            CarritoData carritoData,
            ProductoData productoData
    ) {

        ItemCarritoData itemCarritoData =
                new ItemCarritoData();

        itemCarritoData.setIdItemCarrito(
                itemCarrito.getIdItemCarrito()
        );

        itemCarritoData.setCarrito(carritoData);
        itemCarritoData.setProducto(productoData);

        itemCarritoData.setCantidad(
                itemCarrito.getCantidad()
        );

        itemCarritoData.setPrecioUnitario(
                itemCarrito.getPrecioUnitario()
        );

        itemCarritoData.setSubtotal(
                itemCarrito.getSubtotal()
        );

        return itemCarritoData;
    }

    public ItemCarrito toDomain(
            ItemCarritoData itemCarritoData
    ) {

        ItemCarrito itemCarrito = new ItemCarrito();

        itemCarrito.setIdItemCarrito(
                itemCarritoData.getIdItemCarrito()
        );

        itemCarrito.setIdCarrito(
                itemCarritoData
                        .getCarrito()
                        .getIdCarrito()
        );

        itemCarrito.setIdProducto(
                itemCarritoData
                        .getProducto()
                        .getIdProducto()
        );

        itemCarrito.setCantidad(
                itemCarritoData.getCantidad()
        );

        itemCarrito.setPrecioUnitario(
                itemCarritoData.getPrecioUnitario()
        );

        itemCarrito.setSubtotal(
                itemCarritoData.getSubtotal()
        );

        return itemCarrito;
    }
}