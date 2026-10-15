package com.ecommerce.catalogo;
import com.ecommerce.catalogo.Domain.model.*;
import com.ecommerce.catalogo.Domain.usecase.*;
import com.ecommerce.catalogo.Infraestructure.entry_points.*;
import com.ecommerce.catalogo.Infraestructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RutasEndpointsTest {
    MockMvc mvc;
    String productos="/api/ecommerce/productos";
    String carritos="/api/ecommerce/carritos";
    String id="00000000-0000-0000-0000-000000000001";
    @BeforeEach void preparar() {
        ProductoUseCase p=mock(ProductoUseCase.class); CarritoUseCase c=mock(CarritoUseCase.class);
        Producto producto=new Producto(); Carrito carrito=new Carrito();
        when(p.guardarProducto(any())).thenReturn(producto); when(p.listarProductos()).thenReturn(List.of());
        when(p.buscarProducto(anyLong())).thenReturn(producto); when(p.actualizarProducto(any())).thenReturn(producto);
        when(c.crearCarrito(anyString())).thenReturn(carrito); when(c.buscarCarrito(anyLong())).thenReturn(carrito);
        when(c.buscarCarritoActivo(anyString())).thenReturn(carrito);
        when(c.agregarProducto(anyLong(),anyLong(),anyInt())).thenReturn(carrito);
        when(c.cambiarCantidad(anyLong(),anyLong(),anyInt())).thenReturn(carrito);
        when(c.retirarProducto(anyLong(),anyLong())).thenReturn(carrito); when(c.comprarCarrito(anyLong())).thenReturn(carrito);
        mvc=MockMvcBuilders.standaloneSetup(new ProductoController(p),new CarritoController(c))
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }
    @Test void crearProducto() throws Exception { mvc.perform(post(productos+"/save").contentType(MediaType.APPLICATION_JSON).content("""
{}
"""))
        .andExpect(status().isCreated()); }
    @Test void listarProductos() throws Exception { mvc.perform(get(productos)).andExpect(status().isOk()); }
    @Test void consultarProducto() throws Exception { mvc.perform(get(productos+"/1")).andExpect(status().isOk()); }
    @Test void actualizarProducto() throws Exception { mvc.perform(put(productos+"/1").contentType(MediaType.APPLICATION_JSON).content("""
{}
"""))
        .andExpect(status().isOk()); }
    @Test void eliminarProducto() throws Exception { mvc.perform(delete(productos+"/1")).andExpect(status().isNoContent()); }
    @Test void crearCarrito() throws Exception { mvc.perform(post(carritos+"/usuario/"+id)).andExpect(status().isCreated()); }
    @Test void consultarCarrito() throws Exception { mvc.perform(get(carritos+"/1")).andExpect(status().isOk()); }
    @Test void consultarCarritoActivo() throws Exception { mvc.perform(get(carritos+"/usuario/"+id+"/activo")).andExpect(status().isOk()); }
    @Test void agregarProducto() throws Exception { mvc.perform(post(carritos+"/1/productos").contentType(MediaType.APPLICATION_JSON)
        .content("""
{"idProducto":1,"cantidad":1}
""")).andExpect(status().isOk()); }
    @Test void cambiarCantidad() throws Exception { mvc.perform(put(carritos+"/1/productos/1/cantidad").contentType(MediaType.APPLICATION_JSON)
        .content("2")).andExpect(status().isOk()); }
    @Test void retirarProducto() throws Exception { mvc.perform(delete(carritos+"/1/productos/1")).andExpect(status().isOk()); }
    @Test void comprar() throws Exception { mvc.perform(post(carritos+"/1/comprar")).andExpect(status().isOk()); }
}
