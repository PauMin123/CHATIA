<%@ page import="modelo.ChatDAO" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
    request.setCharacterEncoding("UTF-8");
    String mensaje = request.getParameter("mensaje");
    String respuesta = ChatDAO.procesarMensaje(mensaje);

    if ("INTENCION_NO_ENTENDIDA".equals(respuesta)) {
        response.sendRedirect("aprender.jsp?pregunta=" + URLEncoder.encode(mensaje, "UTF-8"));
    } else {
        response.sendRedirect("chat.jsp?respuesta=" + URLEncoder.encode(respuesta, "UTF-8"));
    }
%>
