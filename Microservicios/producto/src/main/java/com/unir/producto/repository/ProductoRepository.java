package com.unir.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unir.producto.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    public abstract Producto findByNombre(String nombre);
}