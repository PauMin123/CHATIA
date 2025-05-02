/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author paulo
 */

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import modelo.ChatDAO;

public class CerrarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ChatDAO chatDAO = (ChatDAO) getServletContext().getAttribute("chatDAO");
        if (chatDAO != null) {
            ChatDAO.guardarConversaciones();
        }
        getServletContext().removeAttribute("chatDAO");
        resp.getWriter().write("Conversaciones guardadas y chat cerrado.");
    }
}




