SET NAMES utf8mb4;
CREATE DATABASE IF NOT EXISTS eventdb CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE eventdb;

-- UTILIZATORI (IDM)
CREATE TABLE utilizatori (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    parola VARCHAR(255) NOT NULL,
    rol ENUM('admin', 'owner-event', 'client') NOT NULL
);

INSERT INTO utilizatori(email, parola, rol)
VALUES
('admin@local', 'admin', 'admin'),
('owner@local', 'owner', 'owner-event'),
('client@local', 'client', 'client');

-- EVENIMENTE
CREATE TABLE evenimente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_owner INT NOT NULL,
    nume VARCHAR(255) NOT NULL UNIQUE,
    locatie VARCHAR(255),
    descriere VARCHAR(255),
    numarLocuri INT,
    FOREIGN KEY (id_owner) REFERENCES utilizatori(id)
);

-- PACHETE
CREATE TABLE pachete (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_owner INT NOT NULL,
    nume VARCHAR(255) NOT NULL UNIQUE,
    locatie VARCHAR(255),
    descriere VARCHAR(255),
    FOREIGN KEY (id_owner) REFERENCES utilizatori(id)
);

-- PACHETE - EVENIMENTE
CREATE TABLE pachete_evenimente (
    pachet_id INT NOT NULL,
    eveniment_id INT NOT NULL,
    numarLocuri INT,
    PRIMARY KEY (pachet_id, eveniment_id),
    FOREIGN KEY (pachet_id) REFERENCES pachete(id) ON DELETE CASCADE,
    FOREIGN KEY (eveniment_id) REFERENCES evenimente(id) ON DELETE CASCADE
);

-- BILETE
CREATE TABLE bilete (
    cod VARCHAR(64) PRIMARY KEY,
    pachet_id INT NULL,
    eveniment_id INT NULL,
    CHECK ((pachet_id IS NULL) <> (eveniment_id IS NULL)),
    FOREIGN KEY (pachet_id) REFERENCES pachete(id),
    FOREIGN KEY (eveniment_id) REFERENCES evenimente(id)
);

CREATE INDEX idx_evenimente_nume_locatie ON evenimente(nume, locatie);
CREATE INDEX idx_pachete_nume_locatie ON pachete(nume, locatie);