package com.unir.orden.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.unir.orden.dto.ProductoRequest;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    
    @ElementCollection
    @CollectionTable(name = "ids_productos", joinColumns = @JoinColumn(name = "id_orden"))
    private List<ProductoRequest> idProductos;

    @Column(name = "num_documento_comprador", nullable = false)
    private String numDocumentoComprador;

    @Column(name = "nombre_comprador", nullable = false)
    private String nombreComprador;

    @Column(name = "precio_compra", nullable = false)
    private BigDecimal precioCompra;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDate fechaCompra;
}
