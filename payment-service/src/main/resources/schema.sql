DROP TABLE IF EXISTS payment;

CREATE TABLE payment (
    user_id INT NOT NULL PRIMARY KEY,
    balance NUMERIC(10, 2)
);