CREATE TABLE item (
    rentnr      DECIMAL(4,0) NOT NULL,
    productnr   DECIMAL(4,0) NOT NULL
);

CREATE TABLE product (
    productnr        DECIMAL(4,0) NOT NULL,
    producttypenr    DECIMAL(4,0) NOT NULL,
    picture          BLOB,
    productean       VARCHAR(100),
    superproductnr   DECIMAL(4,0),
    status           DECIMAL(4,0) NOT NULL
);

ALTER TABLE product ADD CONSTRAINT product_pk PRIMARY KEY ( productnr );

CREATE TABLE producttype (
    producttypenr     DECIMAL(4,0) NOT NULL,
    typename          VARCHAR(100),
    typedescription   VARCHAR(100)
);

ALTER TABLE producttype ADD CONSTRAINT producttype_pk PRIMARY KEY ( producttypenr );

CREATE TABLE rent (
    rentnr      DECIMAL(4,0) NOT NULL,
    username    VARCHAR(100) NOT NULL,
    rentfrom    DATE,
    rentuntil   DATE
);

ALTER TABLE rent ADD CONSTRAINT rent_pk PRIMARY KEY ( rentnr );

CREATE TABLE st_user (
    username     VARCHAR(100) NOT NULL,
    password     VARCHAR(100),
    name         VARCHAR(100) NOT NULL,
    email        VARCHAR(100),
    klasse       VARCHAR(100),
    userrolenr   DECIMAL(4,0) NOT NULL
);

ALTER TABLE st_user ADD CONSTRAINT user_pk PRIMARY KEY ( username );

CREATE TABLE status (
    statusnr            DECIMAL(4,0) NOT NULL,
    statusbezeichnung   VARCHAR(100) NOT NULL
);

ALTER TABLE status ADD CONSTRAINT status_pk PRIMARY KEY ( statusnr );

CREATE TABLE userrolle (
    userrollenr     DECIMAL(4,0) NOT NULL,
    userrollename   VARCHAR(100) NOT NULL
);

ALTER TABLE userrolle ADD CONSTRAINT userrole_pk PRIMARY KEY ( userrollenr );

ALTER TABLE item
    ADD CONSTRAINT item_product_fk FOREIGN KEY ( productnr )
        REFERENCES product ( productnr );

ALTER TABLE item
    ADD CONSTRAINT item_rent_fk FOREIGN KEY ( rentnr )
        REFERENCES rent ( rentnr );

ALTER TABLE product
    ADD CONSTRAINT product_product_fk FOREIGN KEY ( superproductnr )
        REFERENCES product ( productnr );

ALTER TABLE product
    ADD CONSTRAINT product_producttype_fk FOREIGN KEY ( producttypenr )
        REFERENCES producttype ( producttypenr );

ALTER TABLE product
    ADD CONSTRAINT product_status_fk FOREIGN KEY ( status )
        REFERENCES status ( statusnr );

ALTER TABLE rent
    ADD CONSTRAINT rent_user_fk FOREIGN KEY ( username )
        REFERENCES st_user ( username );

ALTER TABLE st_user
    ADD CONSTRAINT user_userrole_fk FOREIGN KEY ( userrolenr )
        REFERENCES userrolle ( userrollenr );
		
insert into userrolle values (1, 'Schueler');
insert into userrolle values (2, 'Lehrer');

insert into st_user values('stefanleithenmayr', '12345', 'Stefan Leithenmayr', NULL, NULL, 1);
insert into st_user values('stuetz', '12345', 'Stütz', NULL, NULL, 2);


commit;