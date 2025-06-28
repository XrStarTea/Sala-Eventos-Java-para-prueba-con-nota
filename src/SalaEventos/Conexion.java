package SalaEventos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Conexion {
//Conexion a la base de datos, recordatorio de cambiar datos aqui
    public static Connection conectar() throws SQLException {
        String connectionUrl =
            "jdbc:sqlserver://<host>\\SQLEXPRESS:1433;" +
            "database=SalaEventos;" +
            "user=<user>;" +
            "password=<password>;" +
            "encrypt=true;" +
            "trustServerCertificate=true;" +
            "loginTimeout=30;";
        return DriverManager.getConnection(connectionUrl);
    }
}