DROP TABLE IF EXISTS "Client";

CREATE TABLE "Client" (
    id bigint NOT NULL,
    name varchar(100) NOT NULL,
    email varchar(100) NOT NULL,
    birthdate DATE NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS Product;

CREATE TABLE Product (
    id bigint NOT NULL,
    name varchar(100) NOT NULL,
    price NUMERIC (20, 2) NOT NULL,
    quantity INTEGER DEFAULT 0,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS "Order";

CREATE TABLE "Order" (
    id bigint NOT NULL,
    client_id bigint NOT NULL,
    product_id bigint NOT NULL,
    quantity INTEGER NOT NULL,
    total_price NUMERIC (25, 2) NOT NULL,
    status varchar(100) NOT NULL DEFAULT 'Pending',
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES "Client" (id),
    FOREIGN KEY (product_id) REFERENCES Product (id)
);
