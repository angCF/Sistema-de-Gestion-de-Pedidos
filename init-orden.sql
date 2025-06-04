CREATE TABLE IF NOT EXISTS orden (
  id_orden BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  num_documento_comprador VARCHAR(255) NOT NULL,
  nombre_comprador VARCHAR(255) NOT NULL,
  precio_compra DECIMAL(38,2) NOT NULL,
  fecha_compra DATE NOT NULL
);

INSERT IGNORE INTO orden (num_documento_comprador, nombre_comprador, precio_compra, fecha_compra) VALUES
('12345678', 'Juan Perez', 150.00, '2025-06-03'),
('87654321', 'Ana Gomez', 200.50, '2025-06-04'),
('11223344', 'Luis Diaz', 300.75, '2025-06-05');

CREATE TABLE IF NOT EXISTS ids_productos (
  id_orden BIGINT,
  id_producto BIGINT,
  nombre_producto VARCHAR(255),
  cantidad INT,
  PRIMARY KEY (id_orden, id_producto),
  CONSTRAINT fk_ids_productos_orden FOREIGN KEY (id_orden) REFERENCES orden(id_orden) -- Revisar qué pasa si se quita
);

INSERT IGNORE INTO ids_productos (id_orden, id_producto, nombre_producto, cantidad) VALUES
(1, 1, 'Harry Potter y la piedra filosofal - J.K. Rowling', 2),
(1, 3, 'Sony PlayStation 5', 1),
(2, 2, 'Ford Mustang GT', 1),
(3, 1, 'Harry Potter y la piedra filosofal - J.K. Rowling', 1),
(3, 3, 'Sony PlayStation 5', 2);