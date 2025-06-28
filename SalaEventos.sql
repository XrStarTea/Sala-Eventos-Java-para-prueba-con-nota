-- Eliminar base de datos
use master
drop database SalaEventos
-- Creacion de la base de datos
create database SalaEventos
use SalaEventos
-- Crear tablas
CREATE PROCEDURE CrearTablasSalaEventos
AS
BEGIN
    PRINT 'Creando tabla Usuarios...';
    CREATE TABLE Usuarios (
        id INT PRIMARY KEY IDENTITY(1,1),
        nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
        contrasena_hash VARCHAR(60) NOT NULL,
        tipo_usuario VARCHAR(20) NOT NULL,
		rut VARCHAR(20),
		telefono VARCHAR(20),
	    email VARCHAR(100),
        CONSTRAINT CHK_TipoUsuario CHECK (tipo_usuario IN ('Cliente', 'Administrador'))
    );
    PRINT 'Tabla Usuarios creada.';
	--------------------------------------
    PRINT 'Creando tabla Eventos...';
    CREATE TABLE Eventos (
        id INT PRIMARY KEY IDENTITY(1,1),
        nombre VARCHAR(255) NOT NULL,
        fecha DATE NOT NULL,
        hora TIME NOT NULL,
        lugar VARCHAR(255) NOT NULL,
        descripcion VARCHAR(MAX),
        capacidad_maxima INT NOT NULL,
        precio_entrada DECIMAL(10, 2) NOT NULL,
        id_usuario_creador INT NOT NULL,
        FOREIGN KEY (id_usuario_creador) REFERENCES Usuarios(id),
        CONSTRAINT UQ_FechaLugar UNIQUE (fecha, lugar)
    );
    PRINT 'Tabla Eventos creada.';
	--------------------------------------
    PRINT 'Creando tabla Clientes...';
    CREATE TABLE Clientes (
        id_cliente INT PRIMARY KEY IDENTITY(1,1),
        nombre VARCHAR(100) NOT NULL,
        rut VARCHAR(12) UNIQUE NOT NULL,
        telefono VARCHAR(20),
        email VARCHAR(100)
    );
    PRINT 'Tabla Clientes creada.';
	--------------------------------------
    PRINT 'Creando tabla TiposEventos...';
    CREATE TABLE TiposEventos (
        id_tipo_evento INT PRIMARY KEY IDENTITY(1,1),
        nombre_tipo VARCHAR(50) UNIQUE NOT NULL
    );
    PRINT 'Tabla TiposEventos creada.';
	--------------------------------------
    PRINT 'Creando tabla Reservas...';
    CREATE TABLE Reservas (
        id_reserva INT PRIMARY KEY IDENTITY(1,1),
        id_cliente INT NOT NULL,
        id_tipo_evento INT NOT NULL,
        fecha DATE NOT NULL,
        hora_inicio TIME NOT NULL,
        duracion_horas DECIMAL(4,2) NOT NULL,
        
        CONSTRAINT FK_Reservas_Clientes FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente),
        CONSTRAINT FK_Reservas_TiposEventos FOREIGN KEY (id_tipo_evento) REFERENCES TiposEventos(id_tipo_evento)
    );
    PRINT 'Tabla Reservas creada.';
	--------------------------------------
    PRINT 'Tablas creadas exitosamente en SalaEventosDB.';
END;
GO

-- Ejecutar el procedimiento almacenado para crear las tablas
EXEC CrearTablasSalaEventos;
-- Ver las tablas principales
select * from Usuarios;
select * from Eventos;
-- datos de prueba dentro de tabla eventos
INSERT INTO Eventos (nombre, fecha, hora, lugar, descripcion, capacidad_maxima, precio_entrada, id_usuario_creador) VALUES
('Conferencia de Tecnología', '2025-07-15', '09:00:00', 'Centro de Convenciones', 'Charla sobre nuevas tendencias en IA', 200, 25.00, 1),
('Taller de Programación', '2025-07-20', '14:30:00', 'Sala de Capacitación', 'Introducción a Java y Spring Boot', 50, 10.00, 1);