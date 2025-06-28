package SalaEventos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class GestorUsuarios {
    public boolean registrarUsuario(Usuario usuario) {
        if (existeUsuario(usuario.getNombreUsuario())) {
            System.err.println("Error (GestorUsuarios): El nombre de usuario '" 
                    + usuario.getNombreUsuario() + "' ya existe.");
            return false;
        }

        String contrasenaPlana = usuario.getContrasenaHash();
        String hashedPassword = BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt());
        // SQL para insertar un nuevo usuario (incluyendo campos de cliente)
        String sql = "INSERT INTO Usuarios (nombre_usuario, contrasena_hash,"
                + " tipo_usuario, rut, telefono, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombreUsuario());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, usuario.getTipoUsuario());
            // Los campos de cliente pueden ser nulos si el usuario no es de tipo "Cliente" sino "Admin"
            pstmt.setString(4, usuario.getRut());
            pstmt.setString(5, usuario.getTelefono());
            pstmt.setString(6, usuario.getEmail());
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error (GestorUsuarios) al registrar usuario: " 
                    + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public Usuario autenticarUsuario(String nombreUsuario, String contrasenaPlana) {
        String sql = "SELECT id, contrasena_hash, tipo_usuario, rut, telefono, email FROM Usuarios WHERE nombre_usuario = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("contrasena_hash");
                    // Verificar la contraseña plana contra el hash almacenado
                    if (BCrypt.checkpw(contrasenaPlana, storedHash)) {
                        // Contraseña correcta, creamos y devolvemos el objeto Usuario completo
                        int id = rs.getInt("id");
                        String tipoUsuario = rs.getString("tipo_usuario");
                        String rut = rs.getString("rut");
                        String telefono = rs.getString("telefono");
                        String email = rs.getString("email");
                        return new Usuario(id, nombreUsuario, storedHash, tipoUsuario, rut, telefono, email);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error (GestorUsuarios) al autenticar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public Usuario obtenerUsuarioPorId(int idUsuario) {
        String sql = "SELECT id, nombre_usuario, contrasena_hash, tipo_usuario, rut, telefono, email FROM Usuarios WHERE id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nombreUsuario = rs.getString("nombre_usuario");
                    String contrasenaHash = rs.getString("contrasena_hash");
                    String tipoUsuario = rs.getString("tipo_usuario");
                    String rut = rs.getString("rut");
                    String telefono = rs.getString("telefono");
                    String email = rs.getString("email");
                    return new Usuario(id, nombreUsuario, contrasenaHash, tipoUsuario, rut, telefono, email);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error (GestorUsuarios) al obtener usuario por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean existeUsuario(String nombreUsuario) {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE nombre_usuario = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombreUsuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error (GestorUsuarios) al verificar existencia de usuario: "
                    + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Obtiene una lista de todos los usuarios (clientes y administradores).
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, nombre_usuario, contrasena_hash, tipo_usuario,"
                + " rut, telefono, email FROM Usuarios";
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombreUsuario = rs.getString("nombre_usuario");
                String contrasenaHash = rs.getString("contrasena_hash");
                String tipoUsuario = rs.getString("tipo_usuario");
                String rut = rs.getString("rut");
                String telefono = rs.getString("telefono");
                String email = rs.getString("email");
                usuarios.add(new Usuario(id, nombreUsuario, contrasenaHash,
                        tipoUsuario, rut, telefono, email));
            }
        } catch (SQLException e) {
            System.err.println("Error (GestorUsuarios) al obtener todos los usuarios: "
                    + e.getMessage());
            e.printStackTrace();
        }
        return usuarios;
    }
    /**
     * Obtiene una lista de todos los usuarios de tipo "Cliente".
     */
    public List<Usuario> obtenerTodosLosClientes() {
        List<Usuario> clientes = new ArrayList<>();
        String sql = "SELECT id, nombre_usuario, contrasena_hash, tipo_usuario,"
                + " rut, telefono, email FROM Usuarios WHERE tipo_usuario = 'Cliente'";
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombreUsuario = rs.getString("nombre_usuario");
                String contrasenaHash = rs.getString("contrasena_hash");
                String tipoUsuario = rs.getString("tipo_usuario");
                String rut = rs.getString("rut");
                String telefono = rs.getString("telefono");
                String email = rs.getString("email");
                clientes.add(new Usuario(id, nombreUsuario, contrasenaHash,
                        tipoUsuario, rut, telefono, email));
            }
        } catch (SQLException e) {
            System.err.println("Error (GestorUsuarios) al obtener todos los clientes: " + e.getMessage());
            e.printStackTrace();
        }
        return clientes;
    }
    public List<Usuario> buscarClientes(String terminoBusqueda) {
        List<Usuario> clientes = new ArrayList<>();
        String sql = "SELECT id, nombre_usuario, contrasena_hash, tipo_usuario,"
                + " rut, telefono, email FROM Usuarios WHERE tipo_usuario = 'Cliente' AND (nombre_usuario LIKE ? OR rut LIKE ?)";
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + terminoBusqueda + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombreUsuario = rs.getString("nombre_usuario");
                    String contrasenaHash = rs.getString("contrasena_hash");
                    String tipoUsuario = rs.getString("tipo_usuario");
                    String rut = rs.getString("rut");
                    String telefono = rs.getString("telefono");
                    String email = rs.getString("email");
                    clientes.add(new Usuario(id, nombreUsuario, contrasenaHash,
                            tipoUsuario, rut, telefono, email));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error (GestorUsuarios) al buscar clientes: "
                    + e.getMessage());
            e.printStackTrace();
        }
        return clientes;
    }
    /**
     * Actualiza los datos de un usuario (incluyendo campos de cliente).
     */
    public boolean actualizarUsuario(Usuario usuario, boolean contrasenaCambio) {
        String sql;
        String hashedPassword = usuario.getContrasenaHash();
        if (contrasenaCambio) {
            hashedPassword = BCrypt.hashpw(usuario.getContrasenaHash(), BCrypt.gensalt());
            sql = "UPDATE Usuarios SET nombre_usuario = ?, contrasena_hash = ?, tipo_usuario = ?, rut = ?, telefono = ?, email = ? WHERE id = ?";
        } else {
            sql = "UPDATE Usuarios SET nombre_usuario = ?, tipo_usuario = ?, rut = ?, telefono = ?, email = ? WHERE id = ?";
        }

        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombreUsuario());
            int paramIndex = 2;
            if (contrasenaCambio) {
                pstmt.setString(paramIndex++, hashedPassword);
            }
            
            pstmt.setString(paramIndex++, usuario.getTipoUsuario());
            pstmt.setString(paramIndex++, usuario.getRut());
            pstmt.setString(paramIndex++, usuario.getTelefono());
            pstmt.setString(paramIndex++, usuario.getEmail());
            pstmt.setInt(paramIndex++, usuario.getId());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error (GestorUsuarios) al actualizar usuario: " 
                    + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Elimina un usuario de la base de datos por su ID.
     */
    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM Usuarios WHERE id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error (GestorUsuarios) al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}