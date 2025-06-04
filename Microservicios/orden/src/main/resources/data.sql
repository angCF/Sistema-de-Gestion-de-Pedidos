ALTER TABLE ids_productos ADD CONSTRAINT unique_producto_por_orden UNIQUE (id_orden, id_producto);

INSERT IGNORE INTO orden (id_orden, num_documento_comprador, nombre_comprador, precio_compra, fecha_compra) VALUES
(1, '12345678', 'Juan Perez', 150.00, '2025-06-03'),
(2, '87654321', 'Ana Gomez', 200.50, '2025-06-04'),
(3, '11223344', 'Luis Diaz', 300.75, '2025-06-05');

INSERT IGNORE INTO ids_productos (id_orden, id_producto, nombre_producto, cantidad) VALUES
(1, 1, 'Harry Potter y la piedra filosofal - J.K. Rowling', 2),
(1, 3, 'Sony PlayStation 5', 1),
(2, 2, 'Ford Mustang GT', 1),
(3, 1, 'Harry Potter y la piedra filosofal - J.K. Rowling', 1),
(3, 3, 'Sony PlayStation 5', 2);

