package controlador;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import modelo.ChatDAO;
import modelo.Conversacion;
import modelo.ArbolAVL;

import java.util.List;

@WebListener
public class InicializadorContexto implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        List<Conversacion> historial = ChatDAO.obtenerConversaciones();
        ArbolAVL arbol = new ArbolAVL();
        for (Conversacion c : historial) {
            arbol.insertar(c);
        }

        // Guardar el árbol en el contexto para que esté disponible globalmente
        sce.getServletContext().setAttribute("arbolConversaciones", arbol);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ChatDAO.guardarConversaciones();
    }
}
