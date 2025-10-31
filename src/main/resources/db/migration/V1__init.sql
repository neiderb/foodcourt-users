CREATE TABLE foodcourt_role (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE foodcourt_user (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    document_number VARCHAR(50) NOT NULL UNIQUE,
    phone VARCHAR(20),
    birthdate DATE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    id_role BIGINT REFERENCES foodcourt_role (id) NOT NULL
);

INSERT INTO foodcourt_role (name, description) VALUES
('ADMIN', 'Administrator with full access'),
('OWNER', 'Restaurant owner with management access'),
('EMPLOYEE', 'Restaurant employee with limited access'),
('CLIENT', 'Client with access to order food');