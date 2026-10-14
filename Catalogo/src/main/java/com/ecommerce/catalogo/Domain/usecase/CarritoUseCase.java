package com.ecommerce.catalogo.Domain.usecase;

import com.ecommerce.catalogo.Domain.model.Carrito;
import com.ecommerce.catalogo.Domain.model.EstadoCarrito;
import com.ecommerce.catalogo.Domain.model.ItemCarrito;
import com.ecommerce.catalogo.Domain.model.Producto;
import com.ecommerce.catalogo.Domain.model.gateway.CarritoGateway;
import com.ecommerce.catalogo.Domain.model.gateway.ItemCarritoGateway;
import com.ecommerce.catalogo.Domain.model.gateway.ProductoGateway;
import com.ecommerce.catalogo.Domain.model.gateway.AuthGateway;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CarritoUseCase {

    private final CarritoGateway carritoGateway;
    private final ItemCarritoGateway itemCarritoGateway;
    private final ProductoGateway productoGateway;
    private final AuthGateway authGateway;
    public Carrito crearCarrito(String idUsuario) {

        validarIdUsuario(idUsuario);


        authGateway.validarUsuario(idUsuario);

        validarUsuarioSinCarritoActivo(idUsuario);

        Carrito carrito = new Carrito();

        carrito.setIdUsuario(idUsuario);
        carrito.setEstado(EstadoCarrito.ACTIVO);
        carrito.setTotal(BigDecimal.ZERO);
        carrito.setFechaCreacion(LocalDateTime.now());
        carrito.setFechaCompra(null);
        carrito.setItems(new ArrayList<>());

        return carritoGateway.guardarCarrito(carrito);
    }

    public Carrito buscarCarrito(Long idCarrito) {

        validarIdCarrito(idCarrito);

        Carrito carrito = obtenerCarritoExistente(
                idCarrito
        );

        carrito.setItems(
                itemCarritoGateway.listarPorCarrito(
                        idCarrito
                )
        );

        carrito.setTotal(
                calcularTotal(carrito.getItems())
        );

        return carrito;
    }

    public Carrito buscarCarritoActivo(
            String idUsuario
    ) {

        validarIdUsuario(idUsuario);

        Carrito carrito = Optional.ofNullable(
                        carritoGateway
                                .buscarCarritoActivoPorUsuario(
                                        idUsuario
                                )
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El usuario no tiene un carrito activo"
                        )
                );

        carrito.setItems(
                itemCarritoGateway.listarPorCarrito(
                        carrito.getIdCarrito()
                )
        );

        carrito.setTotal(
                calcularTotal(carrito.getItems())
        );

        return carrito;
    }

    public Carrito agregarProducto(
            Long idCarrito,
            Long idProducto,
            Integer cantidad
    ) {

        validarIdCarrito(idCarrito);
        validarIdProducto(idProducto);
        validarCantidad(cantidad);

        Carrito carrito = obtenerCarritoExistente(
                idCarrito
        );

        validarCarritoActivo(carrito);

        Producto producto = obtenerProductoExistente(
                idProducto
        );

        ItemCarrito itemExistente =
                itemCarritoGateway
                        .buscarPorCarritoYProducto(
                                idCarrito,
                                idProducto
                        );

        Integer cantidadActual =
                Optional.ofNullable(itemExistente)
                        .map(ItemCarrito::getCantidad)
                        .orElse(0);

        Integer nuevaCantidad =
                cantidadActual + cantidad;

        validarStock(
                producto.getStock(),
                nuevaCantidad
        );

        ItemCarrito itemCarrito =
                Optional.ofNullable(itemExistente)
                        .orElseGet(ItemCarrito::new);

        itemCarrito.setIdCarrito(idCarrito);
        itemCarrito.setIdProducto(idProducto);
        itemCarrito.setCantidad(nuevaCantidad);

        itemCarrito.setPrecioUnitario(
                producto.getPrecio()
        );

        itemCarrito.setSubtotal(
                calcularSubtotal(
                        producto.getPrecio(),
                        nuevaCantidad
                )
        );

        Optional.ofNullable(
                        itemCarrito.getIdItemCarrito()
                )
                .map(id ->
                        itemCarritoGateway
                                .actualizarItem(itemCarrito)
                )
                .orElseGet(() ->
                        itemCarritoGateway
                                .guardarItem(itemCarrito)
                );

        return actualizarTotalCarrito(carrito);
    }

    public Carrito cambiarCantidad(
            Long idCarrito,
            Long idProducto,
            Integer cantidad
    ) {

        validarIdCarrito(idCarrito);
        validarIdProducto(idProducto);
        validarCantidad(cantidad);

        Carrito carrito = obtenerCarritoExistente(
                idCarrito
        );

        validarCarritoActivo(carrito);

        Producto producto = obtenerProductoExistente(
                idProducto
        );

        validarStock(
                producto.getStock(),
                cantidad
        );

        ItemCarrito itemCarrito =
                obtenerItemExistente(
                        idCarrito,
                        idProducto
                );

        itemCarrito.setCantidad(cantidad);

        itemCarrito.setPrecioUnitario(
                producto.getPrecio()
        );

        itemCarrito.setSubtotal(
                calcularSubtotal(
                        producto.getPrecio(),
                        cantidad
                )
        );

        itemCarritoGateway.actualizarItem(
                itemCarrito
        );

        return actualizarTotalCarrito(carrito);
    }

    public Carrito retirarProducto(
            Long idCarrito,
            Long idProducto
    ) {

        validarIdCarrito(idCarrito);
        validarIdProducto(idProducto);

        Carrito carrito = obtenerCarritoExistente(
                idCarrito
        );

        validarCarritoActivo(carrito);

        obtenerItemExistente(
                idCarrito,
                idProducto
        );

        itemCarritoGateway
                .eliminarPorCarritoYProducto(
                        idCarrito,
                        idProducto
                );

        return actualizarTotalCarrito(carrito);
    }
    @Transactional
    public Carrito comprarCarrito(
            Long idCarrito
    ) {

        validarIdCarrito(idCarrito);

        Carrito carrito = obtenerCarritoExistente(
                idCarrito
        );

        validarCarritoActivo(carrito);

        authGateway.validarUsuario(
                carrito.getIdUsuario()
        );

        List<ItemCarrito> items =
                itemCarritoGateway.listarPorCarrito(
                        idCarrito
                );

        validarCarritoConProductos(items);

        validarStockProductos(items);

        BigDecimal total =
                calcularTotal(items);

        validarTotalCompra(total);

        BigDecimal saldoUsuario =
                authGateway.consultarSaldo(
                        carrito.getIdUsuario()
                );

        validarSaldoCompra(
                saldoUsuario,
                total
        );

        String referenciaCompra =
                "CARRITO-" + carrito.getIdCarrito();

        carrito.setItems(items);
        carrito.setTotal(total);
        carrito.setEstado(
                EstadoCarrito.PROCESANDO
        );

        carritoGateway.actualizarCarrito(
                carrito
        );

        ejecutarDescuento(
                carrito,
                total,
                referenciaCompra
        );

        try {

            actualizarInventario(items);

            carrito.setEstado(
                    EstadoCarrito.COMPRADO
            );

            carrito.setFechaCompra(
                    LocalDateTime.now()
            );

            Carrito carritoComprado =
                    carritoGateway.actualizarCarrito(
                            carrito
                    );

            carritoComprado.setItems(items);

            return carritoComprado;

        } catch (RuntimeException exception) {

            compensarCompra(
                    carrito,
                    referenciaCompra
            );

            throw new IllegalArgumentException(
                    "La compra no pudo completarse y el saldo fue revertido",
                    exception
            );
        }
    }

    private Carrito actualizarTotalCarrito(
            Carrito carrito
    ) {

        List<ItemCarrito> items =
                itemCarritoGateway.listarPorCarrito(
                        carrito.getIdCarrito()
                );

        carrito.setItems(items);
        carrito.setTotal(calcularTotal(items));

        Carrito carritoActualizado =
                carritoGateway.actualizarCarrito(
                        carrito
                );

        carritoActualizado.setItems(items);

        return carritoActualizado;
    }
    private void validarStockProductos(
            List<ItemCarrito> items
    ) {

        items.forEach(item -> {

            Producto producto =
                    obtenerProductoExistente(
                            item.getIdProducto()
                    );

            validarStock(
                    producto.getStock(),
                    item.getCantidad()
            );
        });
    }
    private void validarCarritoConProductos(
            List<ItemCarrito> items
    ) {

        Optional.ofNullable(items)
                .filter(lista -> !lista.isEmpty())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El carrito no contiene productos"
                        )
                );
    }
    private void actualizarInventario(
            List<ItemCarrito> items
    ) {

        items.forEach(item -> {

            Producto producto =
                    obtenerProductoExistente(
                            item.getIdProducto()
                    );

            validarStock(
                    producto.getStock(),
                    item.getCantidad()
            );

            Integer nuevoStock =
                    producto.getStock()
                            - item.getCantidad();

            producto.setStock(nuevoStock);

            productoGateway.actualizarProducto(
                    producto
            );
        });
    }

    private BigDecimal calcularSubtotal(
            BigDecimal precio,
            Integer cantidad
    ) {

        return precio.multiply(
                BigDecimal.valueOf(cantidad)
        );
    }
    private void validarSaldoCompra(
            BigDecimal saldo,
            BigDecimal total
    ) {

        Optional.ofNullable(saldo)
                .filter(valor ->
                        valor.compareTo(total) >= 0
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Saldo insuficiente para realizar la compra"
                        )
                );
    }
    private void validarTotalCompra(
            BigDecimal total
    ) {

        Optional.ofNullable(total)
                .filter(valor ->
                        valor.compareTo(
                                BigDecimal.ZERO
                        ) > 0
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El total de la compra debe ser mayor que cero"
                        )
                );
    }

    private BigDecimal calcularTotal(
            List<ItemCarrito> items
    ) {

        return Optional.ofNullable(items)
                .orElseGet(List::of)
                .stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    private Carrito obtenerCarritoExistente(
            Long idCarrito
    ) {

        return Optional.ofNullable(
                        carritoGateway.buscarPorIdCarrito(
                                idCarrito
                        )
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe un carrito con el ID: "
                                        + idCarrito
                        )
                );
    }

    private Producto obtenerProductoExistente(
            Long idProducto
    ) {

        return Optional.ofNullable(
                        productoGateway.buscarPorIdProducto(
                                idProducto
                        )
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe un producto con el ID: "
                                        + idProducto
                        )
                );
    }

    private ItemCarrito obtenerItemExistente(
            Long idCarrito,
            Long idProducto
    ) {

        return Optional.ofNullable(
                        itemCarritoGateway
                                .buscarPorCarritoYProducto(
                                        idCarrito,
                                        idProducto
                                )
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El producto no se encuentra en el carrito"
                        )
                );
    }

    private void validarUsuarioSinCarritoActivo(
            String idUsuario
    ) {

        Optional.ofNullable(
                        carritoGateway
                                .buscarCarritoActivoPorUsuario(
                                        idUsuario
                                )
                )
                .map(carrito -> {
                    throw new IllegalArgumentException(
                            "El usuario ya tiene un carrito activo"
                    );
                });
    }

    private void compensarCompra(
            Carrito carrito,
            String referenciaCompra
    ) {

        authGateway.revertirDescuento(
                carrito.getIdUsuario(),
                referenciaCompra
        );

        carrito.setEstado(
                EstadoCarrito.ACTIVO
        );

        carrito.setFechaCompra(null);

        carritoGateway.actualizarCarrito(
                carrito
        );
    }

    private void validarCarritoActivo(
            Carrito carrito
    ) {

        Optional.ofNullable(carrito.getEstado())
                .filter(
                        estado ->
                                estado == EstadoCarrito.ACTIVO
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El carrito no se encuentra activo"
                        )
                );
    }

    private void validarStock(
            Integer stock,
            Integer cantidad
    ) {

        Optional.ofNullable(stock)
                .filter(
                        stockDisponible ->
                                stockDisponible >= cantidad
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Stock insuficiente para la cantidad solicitada"
                        )
                );
    }

    private void validarIdUsuario(
            String idUsuario
    ) {

        Optional.ofNullable(idUsuario)
                .filter(valor -> !valor.isBlank())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El ID del usuario es obligatorio"
                        )
                );
    }
    private void ejecutarDescuento(
            Carrito carrito,
            BigDecimal total,
            String referenciaCompra
    ) {

        try {

            authGateway.descontarSaldo(
                    carrito.getIdUsuario(),
                    total,
                    referenciaCompra
            );

        } catch (RuntimeException exception) {

            carrito.setEstado(
                    EstadoCarrito.ACTIVO
            );

            carritoGateway.actualizarCarrito(
                    carrito
            );

            throw new IllegalArgumentException(
                    "No fue posible descontar el saldo del usuario",
                    exception
            );
        }
    }


    private void validarIdCarrito(
            Long idCarrito
    ) {

        Optional.ofNullable(idCarrito)
                .filter(valor -> valor > 0)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El ID del carrito debe ser mayor que cero"
                        )
                );
    }

    private void validarIdProducto(
            Long idProducto
    ) {

        Optional.ofNullable(idProducto)
                .filter(valor -> valor > 0)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El ID del producto debe ser mayor que cero"
                        )
                );
    }

    private void validarCantidad(
            Integer cantidad
    ) {

        Optional.ofNullable(cantidad)
                .filter(valor -> valor > 0)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La cantidad debe ser mayor que cero"
                        )
                );
    }
}