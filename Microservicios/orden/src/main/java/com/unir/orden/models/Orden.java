package com.unir.orden.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="orden")
public class Orden {
    @Id
    @Column(name = "id_orden")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idProducto;
    private String numDocumentoComprador;
    private String nombreComprador;
    private Integer cantidad;          
    private BigDecimal precioCompra;   
    private LocalDate fechaCompra;
}
