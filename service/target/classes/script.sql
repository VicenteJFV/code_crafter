-- Crear la base de datos (si no existe)
CREATE DATABASE IF NOT EXISTS auth_db ,
  DEFAULT CHARACTER SET utf8mb4 
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE auth_db;

-- Crear la tabla auth_user
CREATE TABLE IF NOT EXISTS auth_user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL,
  role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
  email VARCHAR(100) UNIQUE,
  enabled BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--CREATE INDEX idx_username ON auth_user(username);
--CREATE INDEX idx_email ON auth_user(email);