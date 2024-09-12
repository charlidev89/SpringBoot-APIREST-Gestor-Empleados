-- Limpiamos tablas
DELETE FROM jornada_laboral;
DELETE FROM empleados;
DELETE FROM concepto_laboral;

-- Reiniciamos secuencias
ALTER TABLE concepto_laboral ALTER COLUMN id RESTART WITH 1;
ALTER TABLE empleados ALTER COLUMN id RESTART WITH 1;
ALTER TABLE jornada_laboral ALTER COLUMN id RESTART WITH 1;

-- Insertamos Conceptos Laborales
INSERT INTO concepto_laboral (nombre, hs_minimo, hs_maximo, laborable)
VALUES ('Turno Normal', 6, 8, true);

INSERT INTO concepto_laboral (nombre, hs_minimo, hs_maximo, laborable)
VALUES ('Turno Extra', 2, 6, true);

INSERT INTO concepto_laboral (nombre, hs_minimo, hs_maximo, laborable)
VALUES ('Día Libre', null, null, false);

-- Creacion de empleados , inserts
INSERT INTO empleados (nombre, apellido, email, nro_documento, fecha_nacimiento, fecha_ingreso, fecha_creacion)
VALUES ('John', 'Doe', 'john.doe@gmail.com', 12345678, '1990-05-15', '2022-01-10', CURRENT_DATE());

INSERT INTO empleados (nombre, apellido, email, nro_documento, fecha_nacimiento, fecha_ingreso, fecha_creacion)
VALUES ('Emily', 'Smith', 'emily.smith@gmail.com', 87654321, '1985-11-30', '2021-06-25', CURRENT_DATE());

INSERT INTO empleados (nombre, apellido, email, nro_documento, fecha_nacimiento, fecha_ingreso, fecha_creacion)
VALUES ('Michael', 'Johnson', 'michael.johnson@gmail.com', 11223344, '1995-02-20', '2020-09-15', CURRENT_DATE());

INSERT INTO empleados (nombre, apellido, email, nro_documento, fecha_nacimiento, fecha_ingreso, fecha_creacion)
VALUES ('Sophia', 'Williams', 'sophia.williams@gmail.com', 44556677, '1992-07-08', '2019-03-18', CURRENT_DATE());

INSERT INTO empleados (nombre, apellido, email, nro_documento, fecha_nacimiento, fecha_ingreso, fecha_creacion)
VALUES ('James', 'Brown', 'james.brown@gmail.com', 99887766, '1988-04-12', '2018-10-22', CURRENT_DATE());

-- Inserts Jornadas Laborales
-- Asumimos que los IDs de empleados son del 1 al 5 y los IDs de conceptos son 1, 2, 3
INSERT INTO jornada_laboral (fecha, hs_trabajadas, id_empleado, id_concepto)
VALUES ('2024-09-05', 8, 1, 1);  -- John Doe, Turno Normal

INSERT INTO jornada_laboral (fecha, hs_trabajadas, id_empleado, id_concepto)
VALUES ('2024-09-05', 4, 2, 2);  -- Emily Smith, Turno Extra

INSERT INTO jornada_laboral (fecha, hs_trabajadas, id_empleado, id_concepto)
VALUES ('2024-09-05', null, 3, 3);  -- Michael Johnson, Día Libre

INSERT INTO jornada_laboral (fecha, hs_trabajadas, id_empleado, id_concepto)
VALUES ('2024-09-06', 8, 4, 1);  -- Sophia Williams, Turno Normal

INSERT INTO jornada_laboral (fecha, hs_trabajadas, id_empleado, id_concepto)
VALUES ('2024-09-06', 8, 5, 1);  -- James Brown, Turno Normal