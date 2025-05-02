package modelo;

import java.util.*;
import java.sql.*;

public class ChatDAO {

    private static ArbolAVL arbol = new ArbolAVL();
    private static ModeloML modelo = new ModeloML();
    private static ClasificadorNaiveBayes clasificador = new ClasificadorNaiveBayes();
    private static List<Conversacion> nuevasConversaciones = new ArrayList<>();

    // Cargar datos desde base de datos
    public static void cargarConversacionesDesdeBD() {
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT pregunta, respuesta, intencion FROM conversaciones");
             ResultSet rs = ps.executeQuery()) {

            ProcesadorNLP nlp = new ProcesadorNLP();

            while (rs.next()) {
                String pregunta = rs.getString("pregunta");
                String respuesta = rs.getString("respuesta");
                String intencion = rs.getString("intencion");

                String preguntaNormalizada = ChatDAO.normalizarTexto(pregunta);
                Conversacion conv = new Conversacion(preguntaNormalizada, respuesta, intencion);
                arbol.insertar(conv);

                // Entrenar modelo clásico (tokens) y Naive Bayes
                List<String> tokens = nlp.limpiarTexto(preguntaNormalizada);
                modelo.entrenar(intencion, tokens);
                clasificador.entrenar(preguntaNormalizada, intencion);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Procesar entrada del usuario
    public static String procesarMensaje(String mensaje) {
    ProcesadorNLP nlp = new ProcesadorNLP();
    String mensajeNormalizado = normalizarTexto(mensaje);
    List<String> tokens = nlp.limpiarTexto(mensajeNormalizado);
    Conversacion mejorCoincidencia = arbol.buscarSimilar(tokens);


    if (mejorCoincidencia != null) {
        return mejorCoincidencia.getRespuesta();
    }

    // Clasificación con Naive Bayes
    String intencion = clasificador.clasificar(ChatDAO.normalizarTexto(mensaje));
    System.out.println("Intención predicha: " + intencion);  // 👈 útil para debug

    // Buscar una conversación por esa intención
    Conversacion porIntencion = arbol.buscarPorIntencion(intencion);
    if (porIntencion != null) {
        return porIntencion.getRespuesta();
    }

    // No se encontró nada
    return null;
}


    // Registrar nueva conversación aprendida
    public static void registrarConversacion(String pregunta, String respuesta, String intencion) {
        try (Connection conn = Conexion.conectar()) {
            String sql = "INSERT INTO conversaciones (pregunta, respuesta, intencion) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pregunta);
            ps.setString(2, respuesta);
            ps.setString(3, intencion);
            ps.executeUpdate();

            String preguntaNormalizada = ChatDAO.normalizarTexto(pregunta);
            Conversacion nueva = new Conversacion(preguntaNormalizada, respuesta, intencion);
            arbol.insertar(nueva);
            clasificador.entrenar(preguntaNormalizada, intencion);

            List<String> tokens = new ProcesadorNLP().limpiarTexto(preguntaNormalizada);
            modelo.entrenar(intencion, tokens);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Guardar nuevas conversaciones (por si se maneja batch en memoria)
    public static void guardarConversaciones() {
        if (nuevasConversaciones.isEmpty()) return;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO conversaciones (pregunta, respuesta, intencion) VALUES (?, ?, ?)")) {

            for (Conversacion conv : nuevasConversaciones) {
                ps.setString(1, conv.getPregunta());
                ps.setString(2, conv.getRespuesta());
                ps.setString(3, conv.getIntencion());
                ps.addBatch();
            }

            ps.executeBatch();
            nuevasConversaciones.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Conversacion> obtenerConversaciones() {
        List<Conversacion> lista = new ArrayList<>();
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT pregunta, respuesta, intencion FROM conversaciones");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Conversacion(rs.getString("pregunta"),
                                           rs.getString("respuesta"),
                                           rs.getString("intencion")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static String normalizarTexto(String texto) {
        texto = texto.toLowerCase();
        texto = java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD);
        texto = texto.replaceAll("[^\\p{ASCII}]", "");
        texto = texto.replaceAll("[^a-z0-9\\s]", "");
        return texto.trim();
    }

    public static List<Conversacion> getHistorialConversaciones() {
        return obtenerConversaciones();
    }
}
