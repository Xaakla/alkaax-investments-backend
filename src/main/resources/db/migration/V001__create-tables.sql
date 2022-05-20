CREATE SEQUENCE IF NOT EXISTS user_id_seq;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY DEFAULT nextval('user_id_seq'),
    firstname VARCHAR(12) NOT NULL,
    lastname VARCHAR(12) NOT NULL,
    username VARCHAR(12) NOT NULL,
    email VARCHAR(64) NOT NULL,
    password VARCHAR NOT NULL
);