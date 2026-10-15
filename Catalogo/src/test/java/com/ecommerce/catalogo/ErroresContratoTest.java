package com.ecommerce.catalogo;
import com.ecommerce.catalogo.Domain.exception.ErrorAplicacion;
import com.ecommerce.catalogo.Domain.exception.ErrorAplicacion.Tipo;
import com.ecommerce.catalogo.Infraestructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

class ErroresContratoTest {
    MockMvc mvc;
    @RestController static class ControladorPrueba {
        @GetMapping("/prueba/{tipo}")
        Object error(@PathVariable("tipo") Tipo tipo) {
            throw new ErrorAplicacion(tipo, "CODIGO_PRUEBA", "Mensaje público controlado");
        }
        @PostMapping(value="/json", consumes="application/json")
        Object json(@RequestBody java.util.Map<String,Object> body) { return body; }
        @GetMapping("/bug") Object bug() { throw new IllegalArgumentException("detalle-privado"); }
    }
    @BeforeEach void preparar() {
        mvc = MockMvcBuilders.standaloneSetup(new ControladorPrueba())
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }
    @Test void formato400() throws Exception { verificar(Tipo.FORMATO,400); }
    @Test void validacion422() throws Exception { verificar(Tipo.VALIDACION,422); }
    @Test void ausente404() throws Exception { verificar(Tipo.NO_ENCONTRADO,404); }
    @Test void conflicto409() throws Exception { verificar(Tipo.CONFLICTO,409); }
    @Test void dependencia503() throws Exception { verificar(Tipo.NO_DISPONIBLE,503); }
    @Test void timeout504() throws Exception { verificar(Tipo.TIEMPO_ESPERA,504); }
    @Test void respuestaInvalida502() throws Exception { verificar(Tipo.RESPUESTA_INVALIDA,502); }
    @Test void interno500() throws Exception { verificar(Tipo.INTERNO,500); }
    void verificar(Tipo tipo,int estado) throws Exception {
        mvc.perform(get("/prueba/"+tipo.name())).andExpect(status().is(estado))
                .andExpect(jsonPath("$.status").value(estado))
                .andExpect(jsonPath("$.code").value("CODIGO_PRUEBA"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }
    @Test void jsonRoto400() throws Exception {
        mvc.perform(post("/json").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest());
    }
    @Test void metodo405ConAllow() throws Exception {
        mvc.perform(get("/json")).andExpect(status().isMethodNotAllowed())
                .andExpect(header().exists("Allow"));
    }
    @Test void formato415() throws Exception {
        mvc.perform(post("/json").contentType(MediaType.APPLICATION_XML).content("<x/>"))
                .andExpect(status().isUnsupportedMediaType());
    }
    @Test void bugEs500SinDetallePrivado() throws Exception {
        mvc.perform(get("/bug")).andExpect(status().isInternalServerError())
                .andExpect(content().string(not(containsString("detalle-privado"))));
    }
}
