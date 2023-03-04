--liquibase formatted sql

--changeset tsurakov:1
CREATE TABLE author
(
    id         SERIAL PRIMARY KEY,
    firstname  VARCHAR(128) NOT NULL,
    lastname   VARCHAR(128) NOT NULL,
    patronymic VARCHAR(128),
    birthday   DATE         NOT NULL,
    UNIQUE (firstname, lastname, patronymic, birthday)
);

--changeset tsurakov:2
CREATE TABLE book
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(128) NOT NULL,
    description VARCHAR(255),
    price       NUMERIC      NOT NULL,
    quantity    SMALLINT     NOT NULL,
    issue_year  SMALLINT     NOT NULL
);

--changeset tsurakov:3
CREATE TABLE book_author
(
    author_id INT    NOT NULL REFERENCES author (id) ON DELETE CASCADE,
    book_id   BIGINT NOT NULL REFERENCES book (id) ON DELETE CASCADE,
    PRIMARY KEY (book_id, author_id)
);

--changeset tsurakov:4
CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    email    VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    role     VARCHAR(32)  NOT NULL
);

--changeset tsurakov:5
CREATE TABLE user_details
(
    id         SERIAL PRIMARY KEY,
    user_id    INT UNIQUE   NOT NULL REFERENCES users (id),
    firstname  VARCHAR(128) NOT NULL,
    lastname   VARCHAR(128) NOT NULL,
    patronymic VARCHAR(128),
    phone      VARCHAR(32)
);

--changeset tsurakov:6
CREATE TABLE user_address
(
    user_id           INT PRIMARY KEY REFERENCES users (id),
    region            VARCHAR(128) NOT NULL,
    district          VARCHAR(128) NOT NULL,
    population_center VARCHAR(128) NOT NULL,
    street            VARCHAR(128) NOT NULL,
    house             VARCHAR(128) NOT NULL,
    is_private        BOOLEAN,
    front_door        INT,
    floor             INT,
    flat              INT
);

--changeset tsurakov:7
CREATE TABLE orders
(
    id         BIGSERIAL PRIMARY KEY,
    CREATEd_at TIMESTAMP UNIQUE NOT NULL,
    end_at     TIMESTAMP,
    status     VARCHAR(50)      NOT NULL,
    price      NUMERIC          NOT NULL,
    client_id  INT              NOT NULL REFERENCES users (id) ON DELETE CASCADE
);

--changeset tsurakov:8
CREATE TABLE order_product
(
    id          BIGSERIAL PRIMARY KEY,
    order_id    BIGINT     NOT NULL REFERENCES orders (id),
    book_id     BIGINT  NOT NULL REFERENCES book (id),
    quantity    INT     NOT NULL,
    total_price NUMERIC NOT NULL
);