package com.tienda.tienda.service;

import com.tienda.tienda.model.Compra;

import java.util.List;

public interface CompraService {
    Compra saveCompra(Compra compra);
    List<Compra> getComprasByUserId(Long userId);
    void deleteCompra(Long id);
}