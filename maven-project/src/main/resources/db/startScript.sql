create table users(username varchar(255) primary key, password varchar(255), name varchar(255));

create table items(
    itemid int primary key,
    description varchar(255),
    itemname varchar(255)
);

create table exemplar(
    exemplarid int primary key,
    itemid int,
    FOREIGN KEY (itemid) REFERENCES items(itemid)
);

create table leihe(
    leihid int primary key,
    username varchar(255),
    exemplarid int,
    FOREIGN KEY (username) REFERENCES users(username),
    FOREIGN KEY (exemplarid) REFERENCES exemplar(exemplarid)
);

insert into users values('stefanleithenmayr', '12345', 'Stefan Leithenmayr');
insert into users values('renedeicker', '12345', 'Rene Deicker');
insert into users values('maxhofer', '12345', 'Maximillian Hofer');
insert into users values('stuetz', '12345', 'Stuetz');
