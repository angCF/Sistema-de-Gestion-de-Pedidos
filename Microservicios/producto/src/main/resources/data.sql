ALTER TABLE producto ADD CONSTRAINT pk_producto PRIMARY KEY (id_producto);

INSERT IGNORE INTO producto (id_producto, nombre, descripcion, precio_venta, stock) VALUES
(1, 'Harry Potter y la piedra filosofal - J.K. Rowling', 'El primer libro de la famosa saga sobre un joven mago y su escuela de magia.', 12.99, 150),
(2, 'Ford Mustang GT', 'Un muscle car clásico, con motor V8 y un diseño impresionante.', 45999.99, 25),
(3, 'Sony PlayStation 5', 'La consola de videojuegos más potente, con gráficos 4K y una nueva generación de juegos.', 499.99, 75);
