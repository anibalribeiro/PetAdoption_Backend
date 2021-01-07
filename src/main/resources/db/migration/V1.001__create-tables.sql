CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

CREATE TABLE pets
(
    id           bigint                 NOT NULL,
    created_date timestamp without time zone,
    updated_date timestamp without time zone,
    version      bigint,
    active       boolean,
    age          real,
    category     integer,
    description  character varying(255),
    name         character varying(255),
    photo        character varying(255) NOT NULL,
    user_id      bigint                 NOT NULL
);

CREATE TABLE users
(
    id           bigint NOT NULL,
    created_date timestamp without time zone,
    updated_date timestamp without time zone,
    version      bigint,
    password     character varying(255),
    phone        character varying(255),
    username     character varying(255)
);

ALTER TABLE ONLY pets
    ADD CONSTRAINT pets_pkey PRIMARY KEY (id);


ALTER TABLE ONLY users
    ADD CONSTRAINT unique_users UNIQUE (username);


ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY pets
    ADD CONSTRAINT fk_pets_users FOREIGN KEY (user_id) REFERENCES users(id) ON
DELETE
CASCADE;
