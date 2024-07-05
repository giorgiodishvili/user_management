-- Create sequence
CREATE SEQUENCE auth.user_id_seq;

-- Create table with explicit sequence usage
CREATE TABLE auth.user
(
    id       BIGINT PRIMARY KEY DEFAULT nextval('auth.user_id_seq'),
    username VARCHAR(50)  NOT NULL,
    firstname VARCHAR(50)  NOT NULL,
    lastname VARCHAR(50)  NOT NULL,
    role VARCHAR(50)  NOT NULL,
    email    VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- Set the sequence owner to the table's id column
ALTER SEQUENCE auth.user_id_seq OWNED BY auth.user.id;
