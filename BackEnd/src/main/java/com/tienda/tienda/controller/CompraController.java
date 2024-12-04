package com.tienda.tienda.controller;

import com.tienda.tienda.model.Compra;
import com.tienda.tienda.service.CompraService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "http://localhost:4200")
public class CompraController {

    @Autowired
    private CompraService compraService;

    // Endpoint para registrar una compra
    @PostMapping("/add")
    public ResponseEntity<String> addCompra(@Valid @RequestBody Compra compra) {
        Compra savedCompra = compraService.saveCompra(compra);
        return ResponseEntity.ok("Compra registrada correctamente con ID: " + savedCompra.getId());
    }

    // Endpoint para obtener el historial de compras de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getComprasByUserId(@PathVariable Long userId) {
        List<Compra> compras = compraService.getComprasByUserId(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Se encontraron " + compras.size() + " compras.");
        response.put("compras", compras);

        return ResponseEntity.ok(response);
    }

    // Endpoint para eliminar una compra
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompra(@PathVariable Long id) {
        compraService.deleteCompra(id);
        return ResponseEntity.ok("Compra eliminada correctamente con ID: " + id);
    }
}