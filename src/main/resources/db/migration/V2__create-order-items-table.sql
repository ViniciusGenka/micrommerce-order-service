CREATE TABLE order_items
(
    id            BINARY(16) PRIMARY KEY,
    order_id      BINARY(16),
    product_id    BINARY(16),
    name          VARCHAR(255),
    quantity      INT,
    unit_price    NUMERIC,
    total_price   NUMERIC,
    FOREIGN KEY (order_id) REFERENCES orders (id)
);