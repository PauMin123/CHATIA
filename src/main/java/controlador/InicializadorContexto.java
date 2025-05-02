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
import modelo.ChatDAO;

@WebListener
public class InicializadorContexto implements ServletContextListener {

@Override
public void contextInitialized(ServletContextEvent sce) {
    ChatDAO chatDAO = new ChatDAO();
    sce.getServletContext().setAttribute("chatDAO", chatDAO);
    
    ChatDAO.cargarConversacionesDesdeBD(); // <---- Agregado
    System.out.println("Conversaciones cargadas desde BD.");
}



    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ChatDAO chatDAO = (ChatDAO) sce.getServletContext().getAttribute("chatDAO");
        if (chatDAO != null) {
            ChatDAO.guardarConversaciones();
        }
    }
}

