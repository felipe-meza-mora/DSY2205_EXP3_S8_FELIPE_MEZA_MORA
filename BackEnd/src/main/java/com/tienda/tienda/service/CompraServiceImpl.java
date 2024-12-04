package com.tienda.tienda.service;

import com.tienda.tienda.model.Compra;
import com.tienda.tienda.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Override
    public Compra saveCompra(Compra compra) {
        return compraRepository.save(compra);
    }

    @Override
    public List<Compra> getComprasByUserId(Long userId) {
        return compraRepository.findByUsuarioId(userId);
    }

    @Override
    public void deleteCompra(Long id) {
        compraRepository.deleteById(id);
    }
}