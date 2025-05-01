/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author paulo
 */


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletContext;
import modelo.ArbolAVL;
import modelo.ChatDAO;

import java.io.IOException;

@WebServlet("/cerrar")
public class CerrarServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext contexto = getServletContext();
        ArbolAVL arbol = (ArbolAVL) contexto.getAttribute("arbol");

        if (arbol != null) {
            ChatDAO chat = new ChatDAO();
            chat.guardarTodo(arbol);  // ahora sí se le pasa el árbol
        }

        response.sendRedirect("index.jsp");
    }
}



