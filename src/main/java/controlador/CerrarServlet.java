package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.ChatDAO;
import modelo.Conversacion;
import modelo.ArbolAVL;

import java.io.IOException;
import java.util.List;

@WebServlet("/cerrar")
public class CerrarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArbolAVL arbol = (ArbolAVL) getServletContext().getAttribute("arbolConversaciones");

        if (arbol != null) {
            ChatDAO dao = new ChatDAO();
            List<Conversacion> conversaciones = arbol.obtenerConversaciones();
            for (Conversacion c : conversaciones) {
                dao.guardarConversaciones(); // Evitar duplicados si ya está
            }
        }

        response.getWriter().println("Conversaciones guardadas exitosamente.");
    }
}
