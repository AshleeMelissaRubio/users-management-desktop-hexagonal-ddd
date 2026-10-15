-- =============================================
-- Script de creación de la base de datos
-- Gestión de Usuarios y TFC - Arquitectura Hexagonal
-- =============================================

CREATE DATABASE IF NOT EXISTS crud_usuarios
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE crud_usuarios;

-- ---------------------------------------------
-- Tabla: users
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS users (
                                     id          VARCHAR(36)  NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        ENUM('ADMIN', 'MEMBER', 'REVIEWER') NOT NULL,
    status      ENUM('ACTIVE', 'INACTIVE', 'PENDING', 'BLOCKED') NOT NULL DEFAULT 'PENDING',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------
-- Tabla: teachers
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS teachers (
                                        id          VARCHAR(36)  NOT NULL,
    dni         VARCHAR(20)  NOT NULL,
    name        VARCHAR(100) NOT NULL,
    address     VARCHAR(255) NOT NULL,
    status      VARCHAR(20)  NOT NULL,
    created_at  TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_teachers_dni (dni)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ---------------------------------------------
-- Tabla: tfcs
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS tfcs (
                                    id          VARCHAR(36)  NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    advisor_id  VARCHAR(36)  NOT NULL,
    status      VARCHAR(20)  NOT NULL,
    created_at  TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY fk_tfcs_teachers (advisor_id),
    CONSTRAINT fk_tfcs_teachers FOREIGN KEY (advisor_id) REFERENCES teachers (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ---------------------------------------------
-- Datos iniciales
-- ---------------------------------------------
-- Usuario administrador inicial (password: Admin1234!)
INSERT INTO users (id, name, email, password, role, status)
VALUES (
           '00000000-0000-0000-0000-000000000001',
           'Administrador',
           'admin@example.com',
           '$2a$12$placeholderHashReplaceWithRealBCryptHash',
           'ADMIN',
           'ACTIVE'
       ) ON DUPLICATE KEY UPDATE id=id;