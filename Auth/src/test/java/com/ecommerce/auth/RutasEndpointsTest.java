package com.ecommerce.auth;
import com.ecommerce.auth.Domain.model.Usuario;
import com.ecommerce.auth.Domain.usecase.UsuarioUseCase;
import com.ecommerce.auth.Infraestructure.entry_points.UsuarioController;
import com.ecommerce.auth.Infraestructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.math.BigDecimal;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RutasEndpointsTest {
    MockMvc mvc;
    String base="/api/ecommerce/usuarios";
    String id="00000000-0000-0000-0000-000000000001";
    @BeforeEach void preparar() {
        UsuarioUseCase caso=mock(UsuarioUseCase.class);
        Usuario u=new Usuario(); u.setIdUsuario(id); u.setNombre("Prueba"); u.setSaldo(BigDecimal.ZERO);
        u.setClave("hash-no-publico");
        when(caso.guardarUsuario(any())).thenReturn(u); when(caso.buscarUsuario(anyString())).thenReturn(u);
        when(caso.actualizarUsuario(any())).thenReturn(u); when(caso.login(any(),any())).thenReturn("Credenciales válidas");
        when(caso.consultarSaldo(anyString())).thenReturn(BigDecimal.ZERO);
        when(caso.recargarSaldo(anyString(),any())).thenReturn(u);
        when(caso.descontarSaldo(anyString(),any(),any())).thenReturn(u);
        when(caso.revertirDescuento(anyString(),any())).thenReturn(u);
        mvc=MockMvcBuilders.standaloneSetup(new UsuarioController(caso))
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }
    @Test void registro() throws Exception { mvc.perform(post(base+"/save").contentType(MediaType.APPLICATION_JSON).content("""
{}
"""))
        .andExpect(status().isOk()).andExpect(jsonPath("$.clave").doesNotExist()).andExpect(jsonPath("$.saldo").exists()); }
    @Test void consulta() throws Exception { mvc.perform(get(base+"/"+id)).andExpect(status().isOk()); }
    @Test void actualizacion() throws Exception { mvc.perform(put(base+"/"+id).contentType(MediaType.APPLICATION_JSON).content("""
{}
"""))
        .andExpect(status().isOk()); }
    @Test void eliminacion() throws Exception { mvc.perform(delete(base+"/"+id)).andExpect(status().isOk()); }
    @Test void login() throws Exception { mvc.perform(post(base+"/login").contentType(MediaType.APPLICATION_JSON).content("""
{}
"""))
        .andExpect(status().isOk()); }
    @Test void saldo() throws Exception { mvc.perform(get(base+"/"+id+"/saldo")).andExpect(status().isOk()); }
    @Test void recarga() throws Exception { mvc.perform(put(base+"/"+id+"/saldo/recargar").contentType(MediaType.APPLICATION_JSON).content("10"))
        .andExpect(status().isOk()); }
    @Test void descuento() throws Exception { mvc.perform(put(base+"/"+id+"/saldo/descontar").contentType(MediaType.APPLICATION_JSON)
        .content("""
{"valor":1,"referenciaCompra":"QA"}
""")).andExpect(status().isOk()); }
    @Test void reversion() throws Exception { mvc.perform(put(base+"/"+id+"/saldo/revertir").contentType(MediaType.APPLICATION_JSON)
        .content("""
{"referenciaCompra":"QA"}
""")).andExpect(status().isOk()); }
}
