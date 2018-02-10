create table users(
    username varchar2(255) primary key,
    password varchar2(255),
    name varchar2(255)
);

create table leihe(
    leihid int primary key,
    username varchar2(255),
    exemplarid int,
    FOREIGN KEY (username) REFERENCES users(username),
    FOREIGN KEY (exemplarid) REFERENCES exemplar(exemplarid)
);

create table items(
    itemid int primary key,
    description varchar2(255),
    itemname varchar2(255)
);

create table exemplar(
    exemplarid int primary key,
    itemid int,
    FOREIGN KEY (itemid) REFERENCES items(itemid)
);

insert into users values('stefanleithenmayr', '12345', 'Stefan Leithenmayr');
insert into users values('renedeicker', '12345', 'Rene Deicker');
insert into users values('maxhofer', '12345', 'Maximillian Hofer');