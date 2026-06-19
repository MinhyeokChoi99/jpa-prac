INSERT INTO product (name, price, stock)
VALUES
('JPA Programming Book', 35000, 100),
('Spring Boot Book', 42000, 80),
('QueryDSL Book', 38000, 50);

INSERT INTO member (email, name)
VALUES
('kim@example.com', 'Kim'),
('lee@example.com', 'Lee'),
('park@example.com', 'Park');

INSERT INTO orders (member_id, order_date, status)
VALUES
(1, CURRENT_TIMESTAMP, 'READY'),
(2, CURRENT_TIMESTAMP, 'PROCESS'),
(3, CURRENT_TIMESTAMP, 'CANCEL');

INSERT INTO order_item (order_id, product_id, order_price, count)
VALUES
(1, 1, 70000, 2),
(1, 2, 42000, 1),
(2, 3, 114000, 3),
(3, 1, 35000, 1);