package SalaEventos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GestorEventos {
    public GestorEventos() {
    }
    // Método para obtener todos los eventos de la base de datos
    public List<Evento> obtenerTodosLosEventos() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT id, nombre, fecha, hora, lugar, descripcion,"
                + " capacidad_maxima, precio_entrada,"
                + " id_usuario_creador FROM Eventos";

        try (Connection conn = Conexion.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                LocalTime hora = rs.getTime("hora").toLocalTime();
                String lugar = rs.getString("lugar");
                String descripcion = rs.getString("descripcion");
                int capacidadMaxima = rs.getInt("capacidad_maxima");
                double precioEntrada = rs.getDouble("precio_entrada");
                int idUsuarioCreador = rs.getInt("id_usuario_creador");

                Evento evento = new Evento(id, nombre, fecha, hora, lugar,
                        descripcion, capacidadMaxima, precioEntrada,
                        idUsuarioCreador);
                eventos.add(evento);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los eventos: " + e.getMessage());
            e.printStackTrace();
        }
        return eventos;
    }

    // Método para obtener un evento por su ID
    public Evento obtenerEventoPorId(int idEvento) {
        String sql = "SELECT id, nombre, fecha, hora, lugar, descripcion,"
                + " capacidad_maxima, precio_entrada,"
                + " id_usuario_creador FROM Eventos WHERE id = ?";
        Evento evento = null;
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEvento);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                LocalTime hora = rs.getTime("hora").toLocalTime();
                String lugar = rs.getString("lugar");
                String descripcion = rs.getString("descripcion");
                int capacidadMaxima = rs.getInt("capacidad_maxima");
                double precioEntrada = rs.getDouble("precio_entrada");
                int idUsuarioCreador = rs.getInt("id_usuario_creador");

                evento = new Evento(idEvento, nombre, fecha, hora, lugar,
                        descripcion, capacidadMaxima, precioEntrada, idUsuarioCreador);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener evento por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return evento;
    }
    
    public boolean existeEventoEnMismaFechaHoraLugar(LocalDate fecha, LocalTime hora,
            String lugar, int idEventoAExcluir) {
        String sql = "SELECT COUNT(*) FROM Eventos WHERE CAST(fecha AS DATE) = ? AND lugar = ?";
        if (idEventoAExcluir != 0) {
            sql += " AND id != ?";
        }
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, java.sql.Date.valueOf(fecha));
            pstmt.setString(2, lugar);
            if (idEventoAExcluir != 0) {
                pstmt.setInt(3, idEventoAExcluir);
            }
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar evento existente por fecha y lugar: "
                    + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Método para crear un nuevo evento
    public boolean crearEvento(Evento evento) {
        // verificar si ya existe un evento en la misma fecha y lugar
        if (existeEventoEnMismaFechaHoraLugar(evento.getFecha(), evento.getHora(), evento.getLugar(), 0)) {
            System.out.println("Error: Ya existe un evento en la misma fecha y lugar. No se puede crear.");
            return false; // No se puede crear el evento
        }
        String sql = "INSERT INTO Eventos (nombre, fecha, hora, lugar,"
                + " descripcion, capacidad_maxima, precio_entrada,"
                + " id_usuario_creador) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, evento.getNombre());
            pstmt.setDate(2, java.sql.Date.valueOf(evento.getFecha()));
            pstmt.setTime(3, java.sql.Time.valueOf(evento.getHora()));
            pstmt.setString(4, evento.getLugar());
            pstmt.setString(5, evento.getDescripcion());
            pstmt.setInt(6, evento.getCapacidadMaxima());
            pstmt.setDouble(7, evento.getPrecioEntrada());
            pstmt.setInt(8, evento.getIdUsuarioCreador());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        evento.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al crear evento: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
     // Método para actualizar un evento existente
    public boolean actualizarEvento(Evento evento, int idUsuarioQueActualiza) {
        if (existeEventoEnMismaFechaHoraLugar(evento.getFecha(), evento.getHora(), evento.getLugar(), evento.getId())) {
            System.out.println("Error: La fecha y lugar del evento se superponen con otro evento existente."
                    + " No se puede actualizar.");
            return false;
        }
        String sql = "UPDATE Eventos SET nombre = ?, fecha = ?, hora = ?, lugar = ?, "
                   + "descripcion = ?, capacidad_maxima = ?, precio_entrada = ? "
                   + "WHERE id = ? AND id_usuario_creador = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, evento.getNombre());
            pstmt.setDate(2, java.sql.Date.valueOf(evento.getFecha()));
            pstmt.setTime(3, java.sql.Time.valueOf(evento.getHora()));
            pstmt.setString(4, evento.getLugar());
            pstmt.setString(5, evento.getDescripcion());
            pstmt.setInt(6, evento.getCapacidadMaxima());
            pstmt.setDouble(7, evento.getPrecioEntrada());
            
            pstmt.setInt(8, evento.getId());
            pstmt.setInt(9, idUsuarioQueActualiza);

            int affectedRows = pstmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar evento: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
     // Método para que un Administrador actualice cualquier evento
    public boolean actualizarEventoComoAdmin(Evento evento) {
        // La comprobación de superposición de fecha/lugar sigue siendo importante
        if (existeEventoEnMismaFechaHoraLugar(evento.getFecha(), evento.getHora(), evento.getLugar(), evento.getId())) {
            System.out.println("Error: La fecha y lugar del evento se superponen con otro evento existente. No se puede actualizar.");
            return false; 
        }

        // Esta consulta UPDATE NO tiene la restricción "AND id_usuario_creador = ?"
        String sql = "UPDATE Eventos SET nombre = ?, fecha = ?, hora = ?, lugar = ?, descripcion = ?, capacidad_maxima = ?, precio_entrada = ?, id_usuario_creador = ? WHERE id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, evento.getNombre());
            pstmt.setDate(2, java.sql.Date.valueOf(evento.getFecha()));
            pstmt.setTime(3, java.sql.Time.valueOf(evento.getHora()));
            pstmt.setString(4, evento.getLugar());
            pstmt.setString(5, evento.getDescripcion());
            pstmt.setInt(6, evento.getCapacidadMaxima());
            pstmt.setDouble(7, evento.getPrecioEntrada());
            pstmt.setInt(8, evento.getIdUsuarioCreador()); // El admin puede cambiar el creador
            pstmt.setInt(9, evento.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar evento como admin: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    // Método para eliminar un evento por su ID
    public boolean eliminarEvento(int idEvento) {
        String sql = "DELETE FROM Eventos WHERE id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEvento);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar evento. Asegúrese de que no tenga reservas asociadas: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}