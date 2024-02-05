CREATE EXTENSION IF NOT EXISTS pgcrypto;

DROP TABLE IF EXISTS purchase_order;

CREATE TABLE purchase_order (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id INT,
    product_id INT,
    price NUMERIC(10, 2),
    status VARCHAR(50)
);

DROP TABLE IF EXISTS product;

CREATE TABLE product (
    id INT NOT NULL PRIMARY KEY,
    name varchar(50),
    price NUMERIC(10, 2)
);

INSERT INTO product(id, name, price)
VALUES
(1, 'Product A', 100.0),
(2, 'Product B', 200.0),
(3, 'Product C', 300.0);