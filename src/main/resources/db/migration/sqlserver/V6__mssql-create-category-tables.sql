drop table if exists dbo.beer_category;
drop table if exists dbo.category;

create table dbo.category (
    id varchar(36) NOT NULL PRIMARY KEY,
    description varchar(150),
    created_date datetime2(6) DEFAULT SYSDATETIME(),
    last_modified_date datetime2(6) DEFAULT SYSDATETIME(),
    version bigint DEFAULT NULL
);

create table dbo.beer_category (
    beer_id uniqueidentifier NOT NULL,
    category_id varchar(36) NOT NULL,
    PRIMARY KEY (beer_id, category_id),
    CONSTRAINT FK_beer_category_beer FOREIGN KEY (beer_id) REFERENCES dbo.beer (id),
    CONSTRAINT FK_beer_category_category FOREIGN KEY (category_id) REFERENCES dbo.category (id)
);
