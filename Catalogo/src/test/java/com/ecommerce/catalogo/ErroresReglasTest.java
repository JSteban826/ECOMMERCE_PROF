package com.ecommerce.catalogo;
import com.ecommerce.catalogo.Domain.exception.ErrorAplicacion;
import com.ecommerce.catalogo.Domain.model.*;
import com.ecommerce.catalogo.Domain.model.gateway.*;
import com.ecommerce.catalogo.Domain.usecase.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ErroresReglasTest {
    ProductoGateway productos; CarritoGateway carritos; ItemCarritoGateway items; AuthGateway auth;
    ProductoUseCase productoCaso; CarritoUseCase carritoCaso;
    @BeforeEach void preparar() {
        productos=mock(ProductoGateway.class); carritos=mock(CarritoGateway.class);
        items=mock(ItemCarritoGateway.class); auth=mock(AuthGateway.class);
        productoCaso=new ProductoUseCase(productos);
        carritoCaso=new CarritoUseCase(carritos,items,productos,auth);
    }
    @Test void productoAusente404() {
        var ex=assertThrows(ErrorAplicacion.class,()->productoCaso.buscarProducto(1L));
        assertEquals("PRODUCTO_NO_ENCONTRADO",ex.getCodigo());
        assertEquals(ErrorAplicacion.Tipo.NO_ENCONTRADO,ex.getTipo());
    }
    @Test void idNoPositivo400() {
        var ex=assertThrows(ErrorAplicacion.class,()->productoCaso.buscarProducto(0L));
        assertEquals(ErrorAplicacion.Tipo.FORMATO,ex.getTipo());
    }
    @Test void precioInvalido422() {
        Producto p=new Producto(); p.setNombre("Producto Prueba"); p.setDescripcion("Prueba");
        p.setPrecio(BigDecimal.ZERO); p.setStock(10); p.setImagenUrl("https://example.com/p.png");
        var ex=assertThrows(ErrorAplicacion.class,()->productoCaso.guardarProducto(p));
        assertEquals("PRECIO_INVALIDO",ex.getCodigo());
    }
    @Test void carritoAusente404() {
        var ex=assertThrows(ErrorAplicacion.class,()->carritoCaso.buscarCarrito(1L));
        assertEquals("CARRITO_NO_ENCONTRADO",ex.getCodigo());
    }
    @Test void cantidadInvalida422() {
        var ex=assertThrows(ErrorAplicacion.class,()->carritoCaso.agregarProducto(1L,1L,0));
        assertEquals(ErrorAplicacion.Tipo.VALIDACION,ex.getTipo());
        assertEquals("CANTIDAD_INVALIDA",ex.getCodigo());
    }
    @Test void carritoNoActivo409() {
        Carrito c=new Carrito(); c.setIdCarrito(1L); c.setEstado(EstadoCarrito.COMPRADO);
        when(carritos.buscarPorIdCarrito(1L)).thenReturn(c);
        var ex=assertThrows(ErrorAplicacion.class,()->carritoCaso.agregarProducto(1L,1L,1));
        assertEquals("CARRITO_NO_ACTIVO",ex.getCodigo());
        assertEquals(ErrorAplicacion.Tipo.CONFLICTO,ex.getTipo());
    }
    @Test void stockInsuficiente409() {
        Carrito c=new Carrito(); c.setIdCarrito(1L); c.setEstado(EstadoCarrito.ACTIVO);
        Producto p=new Producto(); p.setIdProducto(1L); p.setStock(0); p.setPrecio(BigDecimal.ONE);
        when(carritos.buscarPorIdCarrito(1L)).thenReturn(c); when(productos.buscarPorIdProducto(1L)).thenReturn(p);
        var ex=assertThrows(ErrorAplicacion.class,()->carritoCaso.agregarProducto(1L,1L,1));
        assertEquals("STOCK_INSUFICIENTE",ex.getCodigo());
    }
    @Test void dependenciaNoSeConvierteEnUsuarioInexistente() {
        doThrow(new ErrorAplicacion(ErrorAplicacion.Tipo.NO_DISPONIBLE,"AUTH_NO_DISPONIBLE","No disponible"))
                .when(auth).validarUsuario("00000000-0000-0000-0000-000000000001");
        var ex=assertThrows(ErrorAplicacion.class,()->carritoCaso.crearCarrito("00000000-0000-0000-0000-000000000001"));
        assertEquals(ErrorAplicacion.Tipo.NO_DISPONIBLE,ex.getTipo());
    }
}
