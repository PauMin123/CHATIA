/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author paulo
 */
import java.sql.*;

public class GestorBaseDatos {
    private static final String URL = "jdbc:mysql://localhost:3306/chat";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void cargarConversaciones(ArbolAVL arbol) {
        try (Connection conn = conectar()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM conversaciones");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                arbol.insertar(rs.getString("pregunta"), rs.getString("respuesta"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertarConversacion(String pregunta, String respuesta) {
        try (Connection conn = conectar()) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO conversaciones (pregunta, respuesta) VALUES (?, ?) ON DUPLICATE KEY UPDATE respuesta = ?");
            ps.setString(1, pregunta);
            ps.setString(2, respuesta);
            ps.setString(3, respuesta);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

