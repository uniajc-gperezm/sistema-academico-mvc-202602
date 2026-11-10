-- ==============================================================
-- SCRIPTS DE BASE DE DATOS - SISTEMA ACADÉMICO UNIAJC
-- ==============================================================

-- 1. Crear Esquema
CREATE SCHEMA IF NOT EXISTS "practica-mvc";

-- 2. Tabla de Estudiantes
CREATE TABLE IF NOT EXISTS "practica-mvc".estudiantes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabla de Docentes
CREATE TABLE IF NOT EXISTS "practica-mvc".docentes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialty VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Tabla de Materias
CREATE TABLE IF NOT EXISTS "practica-mvc".materias (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    credits INT NOT NULL CHECK (credits >= 1 AND credits <= 10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Tabla de Grupos
CREATE TABLE IF NOT EXISTS "practica-mvc".grupos (
    id SERIAL PRIMARY KEY,
    id_materia INT NOT NULL REFERENCES "practica-mvc".materias(id) ON DELETE CASCADE,
    id_docente INT NOT NULL REFERENCES "practica-mvc".docentes(id) ON DELETE CASCADE,
    classroom VARCHAR(50) NOT NULL,
    schedule VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Tabla de Inscripciones
CREATE TABLE IF NOT EXISTS "practica-mvc".inscripciones (
    id SERIAL PRIMARY KEY,
    id_estudiante INT NOT NULL REFERENCES "practica-mvc".estudiantes(id) ON DELETE CASCADE,
    id_grupo INT NOT NULL REFERENCES "practica-mvc".grupos(id) ON DELETE CASCADE,
    final_grade DECIMAL(5,2) CHECK (final_grade >= 0 AND final_grade <= 5),
    status VARCHAR(20) DEFAULT 'ACTIVO' CHECK (status IN ('ACTIVO', 'APROBADO', 'REPROBADO', 'CANCELADO')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(id_estudiante, id_grupo)
);

-- Datos iniciales de prueba (Opcionales)
INSERT INTO "practica-mvc".estudiantes (name, lastname, email)
VALUES ('Juan', 'Pérez', 'juan.perez@uniajc.edu.co'),
       ('María', 'Gómez', 'maria.gomez@uniajc.edu.co')
ON CONFLICT (email) DO NOTHING;

INSERT INTO "practica-mvc".docentes (name, specialty, email)
VALUES ('Carlos', 'Ingeniería de Software', 'carlos.docente@uniajc.edu.co'),
       ('Laura', 'Bases de Datos', 'laura.docente@uniajc.edu.co')
ON CONFLICT (email) DO NOTHING;

INSERT INTO "practica-mvc".materias (name, credits)
VALUES ('Programación Orientada a Objetos', 4),
       ('Bases de Datos I', 3)
ON CONFLICT DO NOTHING;
