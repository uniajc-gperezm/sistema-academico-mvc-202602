-- ============================================================
-- Sistema Académico UNIAJC — Rutinas almacenadas (PostgreSQL / Neon)
-- Ejecutar DESPUÉS de esquema.sql
--
-- Este script es idempotente: se puede volver a ejecutar cuantas veces
-- se quiera sin duplicar nada ni fallar.
--
-- Nota de sintaxis: en clase el ejemplo se vio en MySQL. Aquí está
-- traducido a PostgreSQL (el motor que usa Neon). El concepto es
-- idéntico; cambian detalles del cuerpo ($$ ... $$ en lugar de
-- BEGIN ... END delimitado por DELIMITER).
--
-- Nota de tipos: los parámetros de nota se declaran DOUBLE PRECISION y
-- no DECIMAL. Java envía la nota con setFloat() (un REAL) y PostgreSQL
-- no convierte REAL -> NUMERIC de forma implícita, así que una función
-- con parámetro DECIMAL no se encontraría por resolución de tipos.
-- REAL -> DOUBLE PRECISION sí es una conversión implícita válida.
-- ============================================================


-- ============================================================
-- 0. Limpieza previa
-- CREATE OR REPLACE no puede cambiar el tipo de retorno ni la firma de
-- una rutina existente, así que se eliminan primero. Se hace por nombre
-- (sin firma) para no depender de los tipos de una versión anterior.
-- ============================================================
DO $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN
        SELECT p.oid::regprocedure AS firma
          FROM pg_proc p
          JOIN pg_namespace n ON n.oid = p.pronamespace
         WHERE n.nspname = 'public'
           AND p.proname IN ('fn_estado_por_nota', 'fn_promedio_grupo', 'sp_asignar_nota')
    LOOP
        EXECUTE 'DROP ROUTINE public.' || r.firma;
    END LOOP;
END;
$$;


-- ============================================================
-- 1. FUNCIONES  (solo leen, SIEMPRE retornan un valor, se llaman con SELECT)
-- ============================================================

-- ------------------------------------------------------------
-- fn_estado_por_nota
-- Traduce una nota numérica al estado académico correspondiente.
-- ------------------------------------------------------------
CREATE FUNCTION public.fn_estado_por_nota(p_nota DOUBLE PRECISION)
RETURNS VARCHAR
LANGUAGE plpgsql
AS $$
BEGIN
    IF p_nota >= 3.0 THEN
        RETURN 'APROBADO';
    END IF;
    RETURN 'REPROBADO';
END;
$$;


-- ------------------------------------------------------------
-- fn_promedio_grupo
-- Promedio de las notas de un grupo. Retorna 0 si el grupo
-- todavía no tiene inscripciones.
-- ------------------------------------------------------------
CREATE FUNCTION public.fn_promedio_grupo(p_id_grupo INT)
RETURNS DOUBLE PRECISION
LANGUAGE plpgsql
AS $$
DECLARE
    v_promedio DOUBLE PRECISION;
BEGIN
    SELECT COALESCE(AVG(nota_final), 0)
      INTO v_promedio
      FROM public.inscripciones_curso
     WHERE id_grupo = p_id_grupo;

    RETURN v_promedio;
END;
$$;


-- ============================================================
-- 2. PROCEDIMIENTOS ALMACENADOS  (pueden modificar datos, se llaman con CALL)
-- ============================================================

-- ------------------------------------------------------------
-- sp_asignar_nota
-- Asigna la nota final de una inscripción y recalcula su estado
-- usando la función fn_estado_por_nota.
--
-- A diferencia de la función, este procedimiento MODIFICA datos
-- (UPDATE) y no devuelve un valor: devuelve su efecto. Se invoca
-- desde Java con CALL, no con SELECT.
--
-- Una inscripción CANCELADA conserva su estado (no se "revive").
-- ------------------------------------------------------------
CREATE PROCEDURE public.sp_asignar_nota(
    p_id_inscripcion INT,
    p_nota DOUBLE PRECISION
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF p_nota < 0 OR p_nota > 5 THEN
        RAISE EXCEPTION 'La nota debe estar entre 0 y 5 (recibida: %)', p_nota;
    END IF;

    UPDATE public.inscripciones_curso
       SET nota_final = p_nota,
           estado = CASE
                        WHEN estado = 'CANCELADO' THEN 'CANCELADO'
                        ELSE public.fn_estado_por_nota(p_nota)
                    END
     WHERE id_inscripcion = p_id_inscripcion;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'No existe la inscripcion con ID %', p_id_inscripcion;
    END IF;
END;
$$;
