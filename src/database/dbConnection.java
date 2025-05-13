package database;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.*;

public class dbConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/SportB_2";
    private static final String USER = "root";
    private static final String PASS = "MiamiSQL05.";
    
    // Pool de conexiones básico (alternativa: usar HikariCP)
    private static Connection connection;

    public static Connection conectar() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASS);
            connection.setAutoCommit(true); // Asegura que cada operación se confirme automáticamente
        }
        return connection;
    }

    public static void cerrar() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}