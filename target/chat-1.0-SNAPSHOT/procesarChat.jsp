<%@ page import="modelo.ChatDAO" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
    String mensaje = request.getParameter("mensaje");
    String respuesta = null;
    boolean sinRespuesta = false;

    // Solo si hay mensaje, procesar con Naive Bayes o árbol
    if (mensaje != null && !mensaje.trim().isEmpty()) {
        respuesta = ChatDAO.procesarMensaje(mensaje);

        if (respuesta == null) {
            sinRespuesta = true; // No se encontró una coincidencia o intención
        }
    }
%>

<head>
    <meta charset="UTF-8">
    <title>Procesar Chat</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<%-- Mostrar formulario solo si el mensaje no fue comprendido --%>
<% if (sinRespuesta) { %>
    <div class="container mt-5">
        <div class="card shadow">
            <div class="card-header bg-warning">
                <strong>Ayuda al sistema a aprender</strong>
            </div>
            <div class="card-body">
                <form action="aprender.jsp" method="post">
                    <input type="hidden" name="pregunta" value="<%= mensaje %>">

                    <div class="mb-3">
                        <label for="respuestaAprendida" class="form-label">Respuesta correcta:</label>
                        <input type="text" name="respuestaAprendida" id="respuestaAprendida" class="form-control" placeholder="Escribe la respuesta correcta" required>
                    </div>

                    <div class="mb-3">
                        <label for="intencion" class="form-label">Intención de la pregunta:</label>
                        <input type="text" name="intencion" id="intencion" class="form-control" placeholder="Ej: saludo, información, despedida" required>
                    </div>

                    <div class="d-flex justify-content-between">
                        <button type="submit" class="btn btn-success">Guardar y Aprender</button>
                        <a href="chat.jsp" class="btn btn-secondary">Cancelar</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
<% } else { %>
    <%-- Redirigir si se entendió el mensaje --%>
    <script>
        window.location.href = "chat.jsp?respuesta=<%= URLEncoder.encode(respuesta, "UTF-8") %>";
    </script>
<% } %>
