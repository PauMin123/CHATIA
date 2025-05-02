package modelo;

import java.util.*;
import java.sql.*;

public class ChatDAO {

    private static ArbolAVL arbol = new ArbolAVL();
    private static ModeloML modelo = new ModeloML();
    private static ClasificadorNaiveBayes clasificador = new ClasificadorNaiveBayes();
    private static List<Conversacion> conversacionesEnMemoria = new ArrayList<>();
    private static final List<Conversacion> aprendidosTemporalmente = new ArrayList<>();

    // Cargar datos desde la base de datos
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

                List<String> tokens = nlp.limpiarTexto(preguntaNormalizada);
                modelo.entrenar(intencion, tokens);
                clasificador.entrenar(preguntaNormalizada, intencion);

                conversacionesEnMemoria.add(conv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Procesa el mensaje y responde
    public static String procesarMensaje(String mensaje) {
    if (mensaje == null || mensaje.trim().isEmpty()) {
        return "Por favor, escribe algo.";
    }

    String mensajeNormalizado = mensaje.trim().toLowerCase();

    // 1. Buscar coincidencia exacta en AVL
    Conversacion resultado = arbolAVL.buscar(mensajeNormalizado);
    if (resultado != null) {
        return resultado.getRespuesta();
    }

    // 2. Clasificar con Naive Bayes (si hay datos)
    if (clasificador == null || clasificador.getNumeroDeIntenciones() == 0) {
        return "INTENCION_NO_ENTENDIDA";
    }

    String intencion = clasificador.clasificar(mensajeNormalizado);
    double confianza = clasificador.calcularProbabilidad(mensajeNormalizado, intencion);

    if (intencion == null || confianza < 0.7) { // <= IMPORTANTE: subimos el umbral
        return "INTENCION_NO_ENTENDIDA";
    }

    // 3. Buscar una respuesta para esa intención (si existe en memoria)
    for (Conversacion c : conversacionesEnMemoria) {
        if (c.getIntencion().equalsIgnoreCase(intencion)) {
            return c.getRespuesta(); // ejemplo representativo
        }
    }

    // 4. No se encontró una respuesta para la intención
    return "INTENCION_NO_ENTENDIDA";
}


    // Registra una nueva conversación aprendida en BD y memoria
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

            conversacionesEnMemoria.add(nueva);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Entrenamiento en memoria solamente
    public static void entrenarTemporal(String pregunta, String respuesta, String intencion) {
        String preguntaNormalizada = normalizarTexto(pregunta);
        Conversacion nueva = new Conversacion(preguntaNormalizada, respuesta, intencion);

        for (Conversacion c : conversacionesEnMemoria) {
    if (c.getPregunta().equalsIgnoreCase(preguntaNormalizada.trim()) &&
        c.getIntencion().equalsIgnoreCase(intencion)) {
        System.out.println("❌ Ya existe esta pregunta con esa intención.");
        return;
    }
}


        conversacionesEnMemoria.add(nueva);
        aprendidosTemporalmente.add(nueva);
        arbol.insertar(nueva);
        clasificador.entrenar(preguntaNormalizada, intencion);

        System.out.println("🧠 Aprendido temporalmente: " + preguntaNormalizada);
    }

    // Guarda los aprendizajes temporales en la BD
    public static void guardarConversaciones() {
        if (aprendidosTemporalmente.isEmpty()) return;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement("INSERT INTO conversaciones (pregunta, respuesta, intencion) VALUES (?, ?, ?)")) {

            for (Conversacion conv : aprendidosTemporalmente) {
                ps.setString(1, conv.getPregunta());
                ps.setString(2, conv.getRespuesta());
                ps.setString(3, conv.getIntencion());
                ps.addBatch();
            }

            ps.executeBatch();
            aprendidosTemporalmente.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Conversacion> obtenerConversaciones() {
        return new ArrayList<>(conversacionesEnMemoria);
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

    // ✅ Nuevo método para verificar si no se entendió
    public static boolean esIntencionNoEntendida(String respuesta) {
        return "INTENCION_NO_ENTENDIDA".equalsIgnoreCase(respuesta);
    }
}
