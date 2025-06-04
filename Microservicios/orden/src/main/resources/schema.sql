CREATE TABLE IF NOT EXISTS orden (
  id_orden BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  num_documento_comprador VARCHAR(255) NOT NULL,
  nombre_comprador VARCHAR(255) NOT NULL,
  precio_compra DECIMAL(38,2) NOT NULL,
  fecha_compra DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS ids_productos (
  id_orden BIGINT NOT NULL,
  id_producto BIGINT NOT NULL,
  PRIMARY KEY (id_orden, id_producto),
  CONSTRAINT fk_ids_productos_orden FOREIGN KEY (id_orden) REFERENCES orden(id_orden),
  CONSTRAINT fk_ids_productos_producto FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);