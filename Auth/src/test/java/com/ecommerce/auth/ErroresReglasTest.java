package com.ecommerce.auth;
import com.ecommerce.auth.Domain.exception.ErrorAplicacion;
import com.ecommerce.auth.Domain.model.*;
import com.ecommerce.auth.Domain.model.gateway.*;
import com.ecommerce.auth.Domain.usecase.UsuarioUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

class ErroresReglasTest {
    UsuarioGateway usuarios;
    EncrypterGateway claves;
    MovimientoSaldoGateway movimientos;
    UsuarioUseCase caso;
    final String id="00000000-0000-0000-0000-000000000001";
    @BeforeEach void preparar() {
        usuarios=mock(UsuarioGateway.class); claves=mock(EncrypterGateway.class);
        movimientos=mock(MovimientoSaldoGateway.class);
        caso=new UsuarioUseCase(usuarios,claves,movimientos);
    }
    Usuario valido() {
        Usuario u=new Usuario();
        u.setNombre("Usuario Prueba"); u.setCorreo("prueba@example.com");
        u.setClave("PruebaSegura123!"); u.setRol("USER"); u.setNumeroTelefonico("3001234567"); u.setEdad(22);
        return u;
    }
    @Test void edadInvalidaEsValidacion() {
        Usuario u=valido(); u.setEdad(14);
        var ex=assertThrows(ErrorAplicacion.class,()->caso.guardarUsuario(u));
        assertEquals(ErrorAplicacion.Tipo.VALIDACION,ex.getTipo());
        assertEquals("EDAD_FUERA_DE_RANGO",ex.getCodigo());
        verifyNoInteractions(usuarios);
    }
    @Test void idMalFormadoEsFormato() {
        var ex=assertThrows(ErrorAplicacion.class,()->caso.buscarUsuario("incorrecto"));
        assertEquals(ErrorAplicacion.Tipo.FORMATO,ex.getTipo());
    }
    @Test void recargaNegativaEs422DeNegocio() {
        var ex=assertThrows(ErrorAplicacion.class,()->caso.recargarSaldo(id,new BigDecimal("-1")));
        assertEquals("MONTO_INVALIDO",ex.getCodigo());
    }
    @Test void consultaAusenteEs404DeDominio() {
        when(usuarios.buscarPorIdUsuario(id)).thenReturn(null);
        var ex=assertThrows(ErrorAplicacion.class,()->caso.buscarUsuario(id));
        assertEquals(ErrorAplicacion.Tipo.NO_ENCONTRADO,ex.getTipo());
    }
    @Test void saldoInsuficienteEsConflicto() {
        Usuario u=valido(); u.setIdUsuario(id); u.setSaldo(BigDecimal.ZERO);
        when(usuarios.buscarPorIdUsuario(id)).thenReturn(u);
        when(movimientos.existeReferenciaCompra("ref-prueba")).thenReturn(false);
        var ex=assertThrows(ErrorAplicacion.class,()->caso.descontarSaldo(id,BigDecimal.TEN,"ref-prueba"));
        assertEquals(ErrorAplicacion.Tipo.CONFLICTO,ex.getTipo());
        assertEquals("SALDO_INSUFICIENTE",ex.getCodigo());
    }
    @Test void registroConservaInicializacionDeSaldo() {
        Usuario u=valido(); u.setSaldo(new BigDecimal("999"));
        when(claves.encrypt(anyString())).thenReturn("hash");
        when(usuarios.guardarusuario(any())).thenAnswer(i->i.getArgument(0));
        Usuario creado=caso.guardarUsuario(u);
        assertEquals(0,BigDecimal.ZERO.compareTo(creado.getSaldo()));
    }
}
