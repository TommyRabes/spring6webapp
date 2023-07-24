-- alter table if exists users_roles drop constraint if exists FKt4v0rrweyk393bdgt107vdx0x;
-- alter table if exists users_roles drop constraint if exists FKa6mgw1ugc3ak9jpt08nmw2ycm;
-- drop table if exists book_user cascade;
-- drop table if exists role cascade;
-- drop table if exists users_roles cascade;
-- drop sequence if exists book_user_seq;
-- drop sequence if exists role_seq;
create sequence book_user_seq start with 1 increment by 50;
create sequence role_seq start with 1 increment by 50;
create table book_user (id bigint not null, email varchar(255) not null unique, full_name varchar(255), password varchar(255) not null, primary key (id));
create table role (id bigint not null, role varchar(255), primary key (id));
create table users_roles (role_id bigint not null, user_id bigint not null, primary key (role_id, user_id));
alter table if exists users_roles add constraint FKt4v0rrweyk393bdgt107vdx0x foreign key (role_id) references role;
alter table if exists users_roles add constraint FKa6mgw1ugc3ak9jpt08nmw2ycm foreign key (user_id) references book_user;