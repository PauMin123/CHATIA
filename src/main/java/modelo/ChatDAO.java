package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

public class ChatDAO {

    private ArbolAVL arbol;

    // Constructor
    public ChatDAO() {
        this.arbol = new ArbolAVL();  // Si ya tienes un árbol cargado, puedes usarlo en lugar de crear uno nuevo
        cargarConversaciones(arbol); // Cargar las conversaciones desde la base de datos
    }

    // Método para responder a una pregunta
    public String responder(String mensaje) {
        return arbol.buscar(mensaje);  // Llamar al método de búsqueda en el árbol AVL
    }

    // Método para aprender (guardar una nueva pregunta y respuesta)
    public void aprender(String pregunta, String respuesta) {
        arbol.insertar(pregunta, respuesta);  // Insertar en el árbol AVL
        guardarTodo(arbol);  // Guardar el árbol actualizado en la base de datos
    }

    // Método para guardar todo el árbol en la base de datos
    public static void guardarTodo(ArbolAVL arbol) {
        if (arbol != null && arbol.getRaiz() != null) {
            guardarDesdeNodo(arbol.getRaiz());
        }
    }

    // Método recursivo para guardar los nodos del árbol
    private static void guardarDesdeNodo(NodoAVL nodo) {
        if (nodo == null) return;
        try (Connection con = Conexion.conectar()) {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO conversaciones (pregunta, respuesta) VALUES (?, ?) ON DUPLICATE KEY UPDATE respuesta=?"
            );
            ps.setString(1, nodo.pregunta);
            ps.setString(2, nodo.respuesta);
            ps.setString(3, nodo.respuesta);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        guardarDesdeNodo(nodo.izquierda);
        guardarDesdeNodo(nodo.derecha);
    }

    // Método para cargar las conversaciones desde la base de datos
    public static void cargarConversaciones(ArbolAVL arbol) {
        try (Connection con = Conexion.conectar()) {
            PreparedStatement ps = con.prepareStatement("SELECT pregunta, respuesta FROM conversaciones");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                arbol.insertar(rs.getString("pregunta"), rs.getString("respuesta"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para obtener el árbol
    public ArbolAVL getArbol() {
        return arbol;
    }

    // Método para establecer el árbol
    public void setArbol(ArbolAVL arbol) {
        this.arbol = arbol;
    }
    
    public List<Conversacion> obtenerTodasLasConversaciones() {
    List<Conversacion> lista = new ArrayList<>();
    String sql = "SELECT id, usuario, mensaje, respuesta FROM conversaciones";

    try (Connection conn = Conexion.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Conversacion conv = new Conversacion(
                rs.getInt("id"),
                rs.getString("usuario"),
                rs.getString("mensaje"),
                rs.getString("respuesta")
            );
            lista.add(conv);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;
}

}
