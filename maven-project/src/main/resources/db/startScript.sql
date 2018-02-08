CREATE TABLE users(username varchar(255),password varchar(255));

insert into users values('stefanleithenmayr', '12345');
insert into users values('renedeicker', '12345');
insert into users values('stuetz', '12345');

CREATE TABLE Items(productID int, productName varchar(255),description VARCHAR(255), quantity int);

--create SEQUENCE itemID start with 1 INCREMENT BY 1;