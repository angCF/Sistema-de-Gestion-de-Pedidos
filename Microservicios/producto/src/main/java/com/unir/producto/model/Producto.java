package com.unir.producto.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "producto")
public class Producto {
  @Id
  @Column(name = "id_producto")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "nombre", nullable = false)
  private String nombre;

  @Column(name = "descripcion")
  private String descripcion;

  @Column(name = "precio_venta", nullable = false)
  private BigDecimal precioVenta;

  @Column(name = "stock", nullable = false)
  private Short stock;
}