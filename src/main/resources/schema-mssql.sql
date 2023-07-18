DROP TABLE IF EXISTS dbo."Order";
DROP TABLE IF EXISTS dbo.Client;
DROP TABLE IF EXISTS dbo.Product;

CREATE TABLE dbo.Client (
    id bigint NOT NULL,
    name varchar(100) NOT NULL,
    email varchar(100) NOT NULL,
    birthdate date NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE dbo.Product (
    id bigint NOT NULL,
    name varchar(100) NOT NULL,
    price numeric (20, 2) NOT NULL,
    quantity int DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE TABLE dbo."Order" (
    id bigint NOT NULL,
    client_id bigint NOT NULL,
    product_id bigint NOT NULL,
    quantity int NOT NULL,
    total_price numeric (25, 2) NOT NULL,
    status varchar(100) DEFAULT 'Pending' NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES Client (id),
    FOREIGN KEY (product_id) REFERENCES Product (id)
);
