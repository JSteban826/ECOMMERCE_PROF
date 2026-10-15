package com.ecommerce.catalogo.Infraestructure.entry_points;
import com.ecommerce.catalogo.Domain.model.Carrito;
import com.ecommerce.catalogo.Domain.model.ItemCarrito;
import com.ecommerce.catalogo.Domain.usecase.CarritoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ecommerce/carritos")
@RequiredArgsConstructor
public class CarritoController {
    private final CarritoUseCase carritoUseCase;
    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<Carrito> crearCarrito(@PathVariable("idUsuario") String idUsuario) {
        return ResponseEntity.status(201).body(carritoUseCase.crearCarrito(idUsuario));
    }
    @GetMapping("/{idCarrito}")
    public ResponseEntity<Carrito> buscarCarrito(@PathVariable("idCarrito") Long idCarrito) {
        return ResponseEntity.ok(carritoUseCase.buscarCarrito(idCarrito));
    }
    @GetMapping("/usuario/{idUsuario}/activo")
    public ResponseEntity<Carrito> buscarCarritoActivo(@PathVariable("idUsuario") String idUsuario) {
        return ResponseEntity.ok(carritoUseCase.buscarCarritoActivo(idUsuario));
    }
    @PostMapping("/{idCarrito}/productos")
    public ResponseEntity<Carrito> agregarProducto(@PathVariable("idCarrito") Long idCarrito,
            @RequestBody ItemCarrito item) {
        return ResponseEntity.ok(carritoUseCase.agregarProducto(idCarrito,
                item.getIdProducto(), item.getCantidad()));
    }
    @PutMapping("/{idCarrito}/productos/{idProducto}/cantidad")
    public ResponseEntity<Carrito> cambiarCantidad(@PathVariable("idCarrito") Long idCarrito,
            @PathVariable("idProducto") Long idProducto, @RequestBody Integer cantidad) {
        return ResponseEntity.ok(carritoUseCase.cambiarCantidad(idCarrito, idProducto, cantidad));
    }
    @DeleteMapping("/{idCarrito}/productos/{idProducto}")
    public ResponseEntity<Carrito> retirarProducto(@PathVariable("idCarrito") Long idCarrito,
            @PathVariable("idProducto") Long idProducto) {
        return ResponseEntity.ok(carritoUseCase.retirarProducto(idCarrito, idProducto));
    }
    @PostMapping("/{idCarrito}/comprar")
    public ResponseEntity<Carrito> comprarCarrito(@PathVariable("idCarrito") Long idCarrito) {
        return ResponseEntity.ok(carritoUseCase.comprarCarrito(idCarrito));
    }
}
