-- ============================================================
-- Sistema Académico UNIAJC — Esquema de Base de Datos (PostgreSQL)
-- Ejecutar en Neon (SQL Editor): https://console.neon.tech
-- ============================================================

-- Tabla de usuarios (login con contraseña encriptada MD5/SHA)
CREATE TABLE IF NOT EXISTS public.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(64) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'USUARIO' CHECK (rol IN ('ADMIN', 'USUARIO')),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de estudiantes
CREATE TABLE IF NOT EXISTS public.estudiantes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de docentes
CREATE TABLE IF NOT EXISTS public.docentes (
    id_docente SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL
);

-- Tabla de materias
CREATE TABLE IF NOT EXISTS public.materias (
    id_materia SERIAL PRIMARY KEY,
    nombre_materia VARCHAR(100) NOT NULL,
    creditos INT NOT NULL CHECK (creditos >= 1 AND creditos <= 10)
);

-- Tabla de grupos
CREATE TABLE IF NOT EXISTS public.grupos (
    id_grupo SERIAL PRIMARY KEY,
    id_materia INT NOT NULL REFERENCES public.materias(id_materia),
    id_docente INT NOT NULL REFERENCES public.docentes(id_docente),
    aula VARCHAR(50) NOT NULL,
    horario VARCHAR(100) NOT NULL
);

-- Tabla de inscripciones
CREATE TABLE IF NOT EXISTS public.inscripciones_curso (
    id_inscripcion SERIAL PRIMARY KEY,
    id_estudiante INT NOT NULL REFERENCES public.estudiantes(id),
    id_grupo INT NOT NULL REFERENCES public.grupos(id_grupo),
    nota_final DECIMAL(5,2) DEFAULT 0 CHECK (nota_final >= 0 AND nota_final <= 5),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
        CHECK (estado IN ('ACTIVO', 'APROBADO', 'REPROBADO', 'CANCELADO')),
    UNIQUE(id_estudiante, id_grupo)
);

-- ============================================================
-- Datos iniciales
-- ============================================================

-- Usuario administrador: admin / admin123 (hash MD5)
-- MD5('admin123') = 0192023a7bbd73250516f069df18b500
INSERT INTO public.users (username, password, nombre, rol) VALUES
('admin', '0192023a7bbd73250516f069df18b500', 'Administrador del Sistema', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

-- Usuario normal: usuario / usuario123 (hash MD5)
-- MD5('usuario123') = 401cec94d3ed586d8cb895c10c0f7db6
INSERT INTO public.users (username, password, nombre, rol) VALUES
('usuario', '401cec94d3ed586d8cb895c10c0f7db6', 'Usuario Normal', 'USUARIO')
ON CONFLICT (username) DO NOTHING;

-- Datos de ejemplo
INSERT INTO public.estudiantes (name, lastname, email) VALUES
('Juan', 'Pérez', 'juan.perez@uniajc.edu.co'),
('María', 'Gómez', 'maria.gomez@uniajc.edu.co'),
('Carlos', 'Rodríguez', 'carlos.rodriguez@uniajc.edu.co')
ON CONFLICT (email) DO NOTHING;

INSERT INTO public.docentes (nombre, especialidad) VALUES
('Gabriel', 'Programación'),
('Ana', 'Matemáticas'),
('Luis', 'Bases de Datos');

INSERT INTO public.materias (nombre_materia, creditos) VALUES
('Programación 2', 4),
('Bases de Datos', 3),
('Matemáticas Discretas', 3);

INSERT INTO public.grupos (id_materia, id_docente, aula, horario) VALUES
(1, 1, 'A101', 'Lunes 7:00 - 9:00'),
(1, 1, 'A102', 'Miércoles 7:00 - 9:00'),
(2, 3, 'B201', 'Martes 10:00 - 12:00');

INSERT INTO public.inscripciones_curso (id_estudiante, id_grupo, nota_final, estado) VALUES
(1, 1, 4.5, 'APROBADO'),
(2, 1, 3.2, 'ACTIVO'),
(3, 2, 0, 'ACTIVO');
