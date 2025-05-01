<%-- 
    Document   : procesarChat
    Created on : 30 abr 2025, 10:44:23?p.m.
    Author     : paulo
--%>

<%@ page import="modelo.ChatDAO" %>
<%@ page import="javax.servlet.http.*" %>
<%
    String mensaje = request.getParameter("mensaje");
    ChatDAO chat = (ChatDAO) application.getAttribute("chat");

    if (chat == null) {
        chat = new ChatDAO();
        application.setAttribute("chat", chat);
    }

    String respuesta = chat.responder(mensaje);

    if (respuesta == null) {
        // No se encontró, pedir aprendizaje
        String nuevaRespuesta = request.getParameter("nuevaRespuesta");

        if (nuevaRespuesta == null) {
            %>
            <form action="procesarChat.jsp" method="post">
                <input type="hidden" name="mensaje" value="<%= mensaje %>" />
                <label>El sistema no sabe responder. ¿Cuál sería una buena respuesta?</label><br/>
                <input type="text" name="nuevaRespuesta" required />
                <input type="submit" value="Aprender" />
            </form>
            <%
            return;
        } else {
            chat.aprender(mensaje, nuevaRespuesta);
            respuesta = "¡Gracias! He aprendido algo nuevo. ?";
        }
    }

    request.setAttribute("respuesta", respuesta);
    request.getRequestDispatcher("chat.jsp").forward(request, response);
%>

