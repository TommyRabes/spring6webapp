-- alter table dbo.author_book drop constraint if exists FKn8665s8lv781v4eojs8jo3jao;
-- alter table dbo.author_book drop constraint if exists FKg7j6ud9d32ll232o9mgo90s57;
-- alter table dbo.book drop constraint if exists FKe9ejdb7a8pgx2hl1kq5t5kjj6;
-- alter table dbo.book_keywords drop constraint if exists FKqghwu9h0dmiihtkpkcvy8o6n8;
-- alter table dbo.flight drop constraint if exists FKbevanaac10ymtkpphtuo99qge;
-- alter table dbo.flight drop constraint if exists FK8evbl7btpewywgh7nknscpkgq;
-- alter table dbo.plans_travelers drop constraint if exists FKpeyiowry0lbuapiwjaj1jmof7;
-- alter table dbo.plans_travelers drop constraint if exists FKicxvmtjcy6u8xmphot7613dqf;
-- alter table dbo.post drop constraint if exists FK5l759v7apba3lqguc7bp8h456;
-- alter table dbo.post_keywords drop constraint if exists FK1sm0tbtev7d2wb82fhovj25lw;
-- alter table dbo.visit drop constraint if exists FKsrvt7r6d1ci6e49qeivmip8q1;
-- alter table dbo.visit drop constraint if exists FKtfrcjfkbvyiu0x8wd931cxqkg;
-- alter table dbo.visit drop constraint if exists FK88i2l6qkkn26g2e5viqx8r7fl;
-- alter table dbo.visit_todos drop constraint if exists FKjk3jcywbo9qm69wx8vr5v8rla;
-- drop table if exists dbo.author;
-- drop table if exists dbo.author_book;
-- drop table if exists dbo.beer;
-- drop table if exists dbo.book;
-- drop table if exists dbo.book_keywords;
-- drop table if exists dbo.customer;
-- drop table if exists dbo.flight;
-- drop table if exists dbo.itinerary;
-- drop table if exists dbo.location;
-- drop table if exists dbo.plans_travelers;
-- drop table if exists dbo.post;
-- drop table if exists dbo.post_keywords;
-- drop table if exists dbo.publisher;
-- drop table if exists dbo.traveler;
-- drop table if exists dbo.traveling_plan;
-- drop table if exists dbo.visit;
-- drop table if exists dbo.visit_todos;
-- drop sequence if exists dbo.author_seq;
-- drop sequence if exists dbo.flight_seq;
-- drop sequence if exists dbo.itinerary_seq;
-- drop sequence if exists dbo.location_seq;
-- drop sequence if exists dbo.post_seq;
-- drop sequence if exists dbo.traveler_seq;
-- drop sequence if exists dbo.traveling_plan_seq;
-- drop sequence if exists dbo.visit_seq;
create sequence dbo.author_seq start with 1 increment by 50;
create sequence dbo.flight_seq start with 1 increment by 50;
create sequence dbo.itinerary_seq start with 1 increment by 50;
create sequence dbo.location_seq start with 1 increment by 50;
create sequence dbo.post_seq start with 1 increment by 50;
create sequence dbo.traveler_seq start with 1 increment by 50;
create sequence dbo.traveling_plan_seq start with 1 increment by 50;
create sequence dbo.visit_seq start with 1 increment by 50;
create table dbo.author (id bigint not null, email varchar(255), first_name varchar(255), last_name varchar(255), primary key (id));
create table dbo.author_book (author_id bigint not null, book_id uniqueidentifier not null, primary key (author_id, book_id));
create table dbo.beer (beer_style smallint not null check (beer_style between 0 and 9), price numeric(38,2) not null, quantity_on_hand int, version int, created_date datetime2(6), update_date datetime2(6), id uniqueidentifier not null, upc varchar(100) not null, beer_name varchar(255) not null, primary key (id));
create table dbo.book (publish_date date, publisher_uuid uniqueidentifier, uuid uniqueidentifier not null, content TEXT, isbn varchar(255), title varchar(255), primary key (uuid));
create table dbo.book_keywords (book_uuid uniqueidentifier not null, keywords varchar(255));
create table dbo.customer (birthdate date not null, version int, created_date datetime2(6), update_date datetime2(6), id uniqueidentifier not null, first_name varchar(100) not null, last_name varchar(100) not null, email varchar(320) not null, primary key (id));
create table dbo.flight (cost numeric(38,2) not null, arrival_date datetime2(6), departure_date datetime2(6) not null, destination_id bigint not null, id bigint not null, initial_location_id bigint not null, primary key (id));
create table dbo.itinerary (id bigint not null, name varchar(255) not null, primary key (id));
create table dbo.location (accuracy float(53) not null, latitude float(53) not null, longitude float(53) not null, id bigint not null, description varchar(255) not null, name varchar(255) not null, primary key (id));
create table dbo.plans_travelers (plan_id bigint not null, traveler_id bigint not null, primary key (plan_id, traveler_id));
create table dbo.post (active bit not null, author_id bigint, id bigint not null, posted_on datetime2(6), body TEXT, slug varchar(255), teaser TEXT, title varchar(255), primary key (id));
create table dbo.post_keywords (post_id bigint not null, keywords varchar(255));
create table dbo.publisher (uuid uniqueidentifier not null, address varchar(255), city varchar(255), email varchar(255), first_name varchar(255), last_name varchar(255), state varchar(255), zip varchar(255), primary key (uuid));
create table dbo.traveler (birthdate datetime2(6) not null, id bigint not null, full_name varchar(255) not null, gender varchar(255) not null, primary key (id));
create table dbo.traveling_plan (id bigint not null, name varchar(255) not null, primary key (id));
create table dbo.visit (from_date date, until_date date, entry_flight_id bigint, id bigint not null, itinerary_id bigint, quit_flight_id bigint, primary key (id));
create table dbo.visit_todos (visit_id bigint not null, todos varchar(255));
create unique nonclustered index UK_q748k0r86ncp8lixobgq08ta on dbo.visit (entry_flight_id) where entry_flight_id is not null;
create unique nonclustered index UK_l1hcvf7fnu7uxxf0abk9epygo on dbo.visit (quit_flight_id) where quit_flight_id is not null;
alter table dbo.author_book add constraint FKn8665s8lv781v4eojs8jo3jao foreign key (book_id) references dbo.book;
alter table dbo.author_book add constraint FKg7j6ud9d32ll232o9mgo90s57 foreign key (author_id) references dbo.author;
alter table dbo.book add constraint FKe9ejdb7a8pgx2hl1kq5t5kjj6 foreign key (publisher_uuid) references dbo.publisher;
alter table dbo.book_keywords add constraint FKqghwu9h0dmiihtkpkcvy8o6n8 foreign key (book_uuid) references dbo.book;
alter table dbo.flight add constraint FKbevanaac10ymtkpphtuo99qge foreign key (destination_id) references dbo.location;
alter table dbo.flight add constraint FK8evbl7btpewywgh7nknscpkgq foreign key (initial_location_id) references dbo.location;
alter table dbo.plans_travelers add constraint FKpeyiowry0lbuapiwjaj1jmof7 foreign key (traveler_id) references dbo.traveler;
alter table dbo.plans_travelers add constraint FKicxvmtjcy6u8xmphot7613dqf foreign key (plan_id) references dbo.traveling_plan;
alter table dbo.post add constraint FK5l759v7apba3lqguc7bp8h456 foreign key (author_id) references dbo.author;
alter table dbo.post_keywords add constraint FK1sm0tbtev7d2wb82fhovj25lw foreign key (post_id) references dbo.post;
alter table dbo.visit add constraint FKsrvt7r6d1ci6e49qeivmip8q1 foreign key (entry_flight_id) references dbo.flight;
alter table dbo.visit add constraint FKtfrcjfkbvyiu0x8wd931cxqkg foreign key (itinerary_id) references dbo.itinerary;
alter table dbo.visit add constraint FK88i2l6qkkn26g2e5viqx8r7fl foreign key (quit_flight_id) references dbo.flight;
alter table dbo.visit_todos add constraint FKjk3jcywbo9qm69wx8vr5v8rla foreign key (visit_id) references dbo.visit;
