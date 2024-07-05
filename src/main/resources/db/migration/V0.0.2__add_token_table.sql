-- Create sequence for Token id
CREATE SEQUENCE auth.token_id_seq
    START WITH 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

-- Create Token table
CREATE TABLE auth.token
(
    id         BIGINT       NOT NULL DEFAULT nextval('auth.token_id_seq'),
    token      VARCHAR(255) NOT NULL UNIQUE,
    token_type VARCHAR(255) NOT NULL DEFAULT 'BEARER',
    revoked    BOOLEAN      NOT NULL,
    expired    BOOLEAN      NOT NULL,
    user_id    BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES auth.user (id)
);

-- Add index to user_id for better performance (optional but recommended)
CREATE INDEX idx_token_user_id ON auth.token(user_id);
