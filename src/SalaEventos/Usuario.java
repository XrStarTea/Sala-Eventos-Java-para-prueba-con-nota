package SalaEventos;

public class Usuario {
    private int id;
    private String nombreUsuario;
    private String contrasenaHash;
    private String tipoUsuario;
    private String rut;
    private String telefono;
    private String email;

    public Usuario(int id, String nombreUsuario, String contrasenaHash, String tipoUsuario,
                   String rut, String telefono, String email) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasenaHash = contrasenaHash;
        this.tipoUsuario = tipoUsuario;
        this.rut = rut;
        this.telefono = telefono;
        this.email = email;
    }

    // Constructor para crear un NUEVO usuario (con contraseña plana, se hasheará en GestorUsuarios)
    public Usuario(String nombreUsuario, String contrasenaPlana, String tipoUsuario) {
        this(0, nombreUsuario, contrasenaPlana, tipoUsuario, null, null, null);
    }
    // Constructor para crear un NUEVO usuario de tipo "Cliente" con todos sus datos
    public Usuario(String nombreUsuario, String contrasenaPlana, String tipoUsuario,
                   String rut, String telefono, String email) {
        this(0, nombreUsuario, contrasenaPlana, tipoUsuario, rut, telefono, email);
    }
    public int getId() {
        return id;
    }
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public String getContrasenaHash() {
        return contrasenaHash;
    }
    public String getTipoUsuario() {
        return tipoUsuario;
    }
    public String getRut() {
        return rut;
    }
    public String getTelefono() {
        return telefono;
    }
    public String getEmail() {
        return email;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }
    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    public void setRut(String rut) {
        this.rut = rut;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}