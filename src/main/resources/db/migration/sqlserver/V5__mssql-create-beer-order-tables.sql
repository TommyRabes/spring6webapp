create table dbo.beer_order(
    id varchar(36) NOT NULL,
    customer_ref varchar(255),
    version bigint,
    customer_id uniqueidentifier NOT NULL,
    created_date datetime2(6) NOT NULL,
    last_modified_date datetime2(6) NOT NULL,
    PRIMARY KEY (id)
);

create table dbo.beer_order_line(
    id varchar(36) NOT NULL,
    beer_id uniqueidentifier NOT NULL,
    order_quantity int NOT NULL CHECK (order_quantity > 0),
    quantity_allocated INT NOT NULL CHECK (quantity_allocated > 0),
    beer_order_id varchar(36) NOT NULL,
    version bigint,
    created_date datetime2(6) NOT NULL,
    last_modified_date datetime2(6) NOT NULL,
    PRIMARY KEY (id)
);

alter table dbo.beer_order add constraint FK_beer_order_customer FOREIGN KEY (customer_id) REFERENCES dbo.customer;
alter table dbo.beer_order_line add constraint FK_beer_order_line_beer FOREIGN KEY (beer_id) REFERENCES dbo.beer;
alter table dbo.beer_order_line add constraint FK_beer_order_line_beer_order FOREIGN KEY (beer_order_id) REFERENCES dbo.beer_order;