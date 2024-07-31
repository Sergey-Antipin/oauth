-- liquibase formatted sql

-- changeset antipin:1722359673946-1
CREATE SEQUENCE role_seq START WITH 100 INCREMENT BY 1;

-- changeset antipin:1722359673946-2
CREATE SEQUENCE user_seq START WITH 100 INCREMENT BY 1;

-- changeset antipin:1722359673946-3
CREATE TABLE roles
(
    id   BIGINT              NOT NULL DEFAULT nextval('role_seq'),
    role VARCHAR(255) UNIQUE NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

-- changeset antipin:1722359673946-4
CREATE TABLE users
(
    id       BIGINT              NOT NULL DEFAULT nextval('user_seq'),
    username VARCHAR(255)        NOT NULL,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

-- changeset antipin:1722359673946-5
CREATE TABLE users_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT pk_users_roles PRIMARY KEY (user_id, role_id)
);

-- changeset antipin:1722359673946-6
ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

-- changeset antipin:1722359673946-7
ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);

INSERT INTO roles(id, role)
VALUES (1, 'USER'),
       (2, 'ADMIN'),
       (3, 'OWNER');

INSERT INTO users(id, username, email, password)
VALUES (1, 'Sergey Antipin', 'mailantipin@gmail.com', null);

INSERT INTO users_roles(user_id, role_id)
VALUES (1, 1),
       (1, 2),
       (1, 3);


