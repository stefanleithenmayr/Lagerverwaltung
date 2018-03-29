CREATE TABLE rent_item (
    rentnr   DECIMAL(4,0) NOT NULL,
    itemnr   DECIMAL(4,0) NOT NULL
);

CREATE TABLE st_item (
    itemnr      DECIMAL(4,0) NOT NULL,
    productnr   DECIMAL(4,0) NOT NULL,
    eancode     VARCHAR(100) NOT NULL,
    refitemnr   DECIMAL(4,0)
);

ALTER TABLE st_item ADD CONSTRAINT st_item_pk PRIMARY KEY ( itemnr );

CREATE TABLE st_product (
    productnr   DECIMAL(4,0) NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(100)
);

ALTER TABLE st_product ADD CONSTRAINT st_product_pk PRIMARY KEY ( productnr );

CREATE TABLE st_rent (
    rentnr     DECIMAL(4,0) NOT NULL,
    "From"     DATE NOT NULL,
    until      DATE NOT NULL,
    username   VARCHAR(100) NOT NULL
);

ALTER TABLE st_rent ADD CONSTRAINT st_rent_pk PRIMARY KEY ( rentnr );

CREATE TABLE st_user (
    username   VARCHAR(100) NOT NULL,
    password   VARCHAR(100) NOT NULL,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(100),
    klasse     VARCHAR(100)
);

ALTER TABLE st_user ADD CONSTRAINT st_user_pk PRIMARY KEY ( username );

ALTER TABLE rent_item
    ADD CONSTRAINT rentitems_st_item_fk FOREIGN KEY ( itemnr )
REFERENCES st_item ( itemnr );

ALTER TABLE rent_item
    ADD CONSTRAINT rentitems_st_rent_fk FOREIGN KEY ( rentnr )
REFERENCES st_rent ( rentnr );

ALTER TABLE st_item
    ADD CONSTRAINT st_item_st_item_fk FOREIGN KEY ( itemnr )
REFERENCES st_item ( itemnr );

ALTER TABLE st_rent
    ADD CONSTRAINT st_rent_st_user_fk FOREIGN KEY ( username )
REFERENCES st_user ( username );


INSERT INTO st_user VALUES ('stuetz', '12345', 'Stütz', NULL, NULL);
