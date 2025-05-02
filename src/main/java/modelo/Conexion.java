/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author paulo
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
        private static final String URL = "jdbc:mysql://localhost:3306/chat";  // Cambia el puerto a 3306 si usas MySQL, yo use docker
    private static final String USER = "root";  // Cambia 'root' a tu usuario de MySQL
    private static final String PASSWORD = "";  // Pon tu contraseña de MySQL
    public static Connection conectar() {
    Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Asegúrate de tener el driver JDBC de MySQL
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Error de driver: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Error de conexión: " + e.getMessage());
        }
        return connection;
    }
}

