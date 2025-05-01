/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author paulo
 */

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import modelo.ArbolAVL;
import modelo.ChatDAO;
import modelo.Conversacion;

import java.util.List;

@WebListener
public class InicializadorContexto implements ServletContextListener {
    public static ArbolAVL arbolGlobal = new ArbolAVL();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ChatDAO dao = new ChatDAO();
        List<Conversacion> conversaciones = dao.obtenerTodasLasConversaciones(); // hace el SELECT único
        for (Conversacion conv : conversaciones) {
           arbolGlobal.insertar(conv.getMensaje(), conv.getRespuesta());

        }
        sce.getServletContext().setAttribute("arbol", arbolGlobal);
        System.out.println("Árbol cargado desde la base de datos.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Aquí no se guarda. Lo hará el CerrarServlet
    }
}
