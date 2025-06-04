CREATE TABLE IF NOT EXISTS producto (
  id_producto BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(255) NOT NULL,
  descripcion VARCHAR(255),
  precio_venta DECIMAL(38,2) NOT NULL,
  stock SMALLINT NOT NULL
);

INSERT IGNORE INTO producto (nombre, descripcion, precio_venta, stock) VALUES
('Harry Potter y la piedra filosofal - J.K. Rowling', 'El primer libro de la famosa saga sobre un joven mago y su escuela de magia.', 12.99, 150),
('Ford Mustang GT', 'Un muscle car clásico, con motor V8 y un diseño impresionante.', 45999.99, 25),
('Sony PlayStation 5', 'La consola de videojuegos más potente, con gráficos 4K y una nueva generación de juegos.', 499.99, 75);