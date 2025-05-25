package com.unir.orden.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unir.orden.models.Orden;

@Repository
public interface OrdenRepository extends JpaRepository<Orden,Long> {
    public abstract List<Orden> findByNumDocumentoComprador(String cedula);    
}
