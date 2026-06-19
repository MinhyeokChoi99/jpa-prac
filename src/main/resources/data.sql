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