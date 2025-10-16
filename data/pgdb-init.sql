CREATE TABLE IF NOT EXISTS products_out (
    id SERIAL PRIMARY KEY,
    name varchar(255),
    description varchar(512),
    weight float,
    price NUMERIC(10,3),
    create_at timestamp
);