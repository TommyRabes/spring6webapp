drop table if exists dbo.beer_order_shipment;

create table dbo.beer_order_shipment (
    id varchar(36) NOT NULL PRIMARY KEY,
    beer_order_id varchar(36) NOT NULL UNIQUE,
    tracking_number varchar(50),
    created_date datetime2(6) DEFAULT SYSDATETIME(),
    last_modified_date datetime2(6) DEFAULT SYSDATETIME(),
    version bigint DEFAULT NULL,
    CONSTRAINT FK_beer_order_shipment_beer_order FOREIGN KEY (beer_order_id) REFERENCES dbo.beer_order (id)
);

alter table dbo.beer_order
    add beer_order_shipment_id varchar(36);

alter table dbo.beer_order
    add constraint FK_beer_order_beer_order_shipment
    FOREIGN KEY (beer_order_shipment_id) REFERENCES dbo.beer_order_shipment (id);
