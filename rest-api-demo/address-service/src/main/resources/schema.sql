drop table if exists address;

create table address
(
    id integer primary key auto_increment,
    country varchar(50) not null,
    city varchar(50) not null,
    address_line1 varchar(100) not null,
    address_line2 varchar(100)
);