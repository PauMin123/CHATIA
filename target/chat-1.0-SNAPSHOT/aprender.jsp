<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.ChatDAO" %>
<%
    String pregunta = request.getParameter("pregunta");
    String respuesta = request.getParameter("respuestaAprendida");
    String intencion = request.getParameter("intencion");

    if (pregunta != null && respuesta != null && intencion != null) {
        ChatDAO.registrarConversacion(pregunta, respuesta, intencion);
        session.setAttribute("respuestaChat", respuesta);
        response.sendRedirect("chat.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Aprender del Usuario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow">
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0">Ayuda al sistema a aprender</h5>
        </div>
        <div class="card-body">
            <form method="post">
                <input type="hidden" name="pregunta" value="<%= request.getParameter("pregunta") %>">

                <div class="mb-3">
                    <label class="form-label">Respuesta correcta:</label>
                    <input type="text" name="respuestaAprendida" class="form-control" placeholder="Escribe la respuesta correcta" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Intención de la pregunta:</label>
                    <input type="text" name="intencion" class="form-control" placeholder="Ej: saludo, tecnología, información..." required>
                </div>

                <div class="d-flex justify-content-between">
                    <button type="submit" class="btn btn-success">Guardar y Aprender</button>
                    <a href="chat.jsp" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
