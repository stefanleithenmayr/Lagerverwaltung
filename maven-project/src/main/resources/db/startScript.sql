create table users(username varchar(255) primary key, password varchar(255), name varchar(255));

create table products(
    productid int primary key,
    description varchar(255),
    productname varchar(255),
    ean varchar(255),
    isAvailable char
);

create table items(
    itemid int primary key,
    productid int,
    FOREIGN KEY (productid) REFERENCES products(productid)
);

create table lend(
    lendid int primary key,
    username varchar(255),
    itemid int,
    lend_from date,
    lend_until date,
    FOREIGN KEY (username) REFERENCES users(username),
    FOREIGN KEY (itemid) REFERENCES items(itemid)
);

insert into users values('stefanleithenmayr', '12345', 'Stefan Leithenmayr');
insert into users values('renedeicker', '12345', 'Rene Deicker');
insert into users values('maxhofer', '12345', 'Maximilian Hofer');
insert into users values('stuetz', '12345', 'Stuetz');
