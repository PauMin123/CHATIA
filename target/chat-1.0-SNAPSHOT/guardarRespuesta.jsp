<%-- 
    Document   : guardarRespuesta
    Created on : 1 may 2025, 12:01:46?a.m.
    Author     : paulo
--%>

<%@ page import="modelo.ArbolAVL" %>
<%
    String nuevaRespuesta = request.getParameter("respuesta");
    String preguntaPendiente = (String) session.getAttribute("preguntaPendiente");
    ArbolAVL arbol = (ArbolAVL) session.getAttribute("arbol");

    if (preguntaPendiente != null && nuevaRespuesta != null && !nuevaRespuesta.isEmpty()) {
        arbol.insertar(preguntaPendiente, nuevaRespuesta);
        session.removeAttribute("preguntaPendiente");
    }

    request.setAttribute("respuesta", "¡Gracias! Aprendí algo nuevo.");
    request.getRequestDispatcher("chat.jsp").forward(request, response);
%>



