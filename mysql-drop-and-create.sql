alter table users_roles drop foreign key FKt4v0rrweyk393bdgt107vdx0x;
alter table users_roles drop foreign key FKa6mgw1ugc3ak9jpt08nmw2ycm;
drop table if exists book_user;
drop table if exists book_user_seq;
drop table if exists role;
drop table if exists role_seq;
drop table if exists users_roles;
create table book_user (id bigint not null, email varchar(255) not null, full_name varchar(255), password varchar(255) not null, primary key (id)) engine=InnoDB;
create table book_user_seq (next_val bigint) engine=InnoDB;
insert into book_user_seq values ( 1 );
create table role (id bigint not null, role varchar(255), primary key (id)) engine=InnoDB;
create table role_seq (next_val bigint) engine=InnoDB;
insert into role_seq values ( 1 );
create table users_roles (role_id bigint not null, user_id bigint not null, primary key (role_id, user_id)) engine=InnoDB;
alter table book_user add constraint UK_kln6olcvf0pgkprrd1mgg5bhf unique (email);
alter table users_roles add constraint FKt4v0rrweyk393bdgt107vdx0x foreign key (role_id) references role (id);
alter table users_roles add constraint FKa6mgw1ugc3ak9jpt08nmw2ycm foreign key (user_id) references book_user (id);
alter table users_roles drop foreign key FKt4v0rrweyk393bdgt107vdx0x;
alter table users_roles drop foreign key FKa6mgw1ugc3ak9jpt08nmw2ycm;
drop table if exists book_user;
drop table if exists book_user_seq;
drop table if exists role;
drop table if exists role_seq;
drop table if exists users_roles;
create table book_user (id bigint not null, email varchar(255) not null, full_name varchar(255), password varchar(255) not null, primary key (id)) engine=InnoDB;
create table book_user_seq (next_val bigint) engine=InnoDB;
insert into book_user_seq values ( 1 );
create table role (id bigint not null, role varchar(255), primary key (id)) engine=InnoDB;
create table role_seq (next_val bigint) engine=InnoDB;
insert into role_seq values ( 1 );
create table users_roles (role_id bigint not null, user_id bigint not null, primary key (role_id, user_id)) engine=InnoDB;
alter table book_user add constraint UK_kln6olcvf0pgkprrd1mgg5bhf unique (email);
alter table users_roles add constraint FKt4v0rrweyk393bdgt107vdx0x foreign key (role_id) references role (id);
alter table users_roles add constraint FKa6mgw1ugc3ak9jpt08nmw2ycm foreign key (user_id) references book_user (id);
