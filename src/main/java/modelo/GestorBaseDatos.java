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
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class GestorBaseDatos {

    private Connection conexion;

    public GestorBaseDatos(Connection conexion) {
        this.conexion = conexion;
    }

    // Normaliza texto (quita acentos, pasa a minúsculas, elimina signos)
    private String normalizar(String texto) {
        if (texto == null) return "";
        String nfdNormalizedString = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return nfdNormalizedString.replaceAll("[^\\p{ASCII}]", "").replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().trim();
    }

    public void guardarConversacion(Conversacion conversacion) {
        try {
            // Comprobar si ya existe una pregunta similar
            String preguntaNormalizada = normalizar(conversacion.getPregunta());
            String sqlCheck = "SELECT * FROM conversaciones WHERE LOWER(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(pregunta, '¿', ''), '?', ''), 'á', 'a'), 'é', 'e'), 'í', 'i')) LIKE ?";
            PreparedStatement stmtCheck = conexion.prepareStatement(sqlCheck);
            stmtCheck.setString(1, "%" + preguntaNormalizada + "%");
            ResultSet rs = stmtCheck.executeQuery();

            boolean existe = false;
            while (rs.next()) {
                String preguntaDB = rs.getString("pregunta");
                if (normalizar(preguntaDB).equals(preguntaNormalizada)) {
                    existe = true;
                    break;
                }
            }

            rs.close();
            stmtCheck.close();

            if (!existe) {
                String sql = "INSERT INTO conversaciones (pregunta, respuesta, intencion) VALUES (?, ?, ?)";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                stmt.setString(1, conversacion.getPregunta());
                stmt.setString(2, conversacion.getRespuesta());
                stmt.setString(3, conversacion.getIntencion());
                stmt.executeUpdate();
                stmt.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Conversacion> cargarConversaciones() {
        List<Conversacion> conversaciones = new ArrayList<>();
        try {
            String sql = "SELECT * FROM conversaciones";
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String pregunta = rs.getString("pregunta");
                String respuesta = rs.getString("respuesta");
                String intencion = rs.getString("intencion");
                conversaciones.add(new Conversacion(pregunta, respuesta, intencion));
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conversaciones;
    }
}

