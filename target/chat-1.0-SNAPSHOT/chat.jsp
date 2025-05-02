<%@page import="modelo.ChatDAO"%>
<%@page import="modelo.Conversacion"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chat Autónomo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .chat-container {
            display: flex;
            margin-top: 30px;
        }
        .sidebar {
            width: 300px;
            margin-right: 20px;
        }
        .chat-box {
            flex: 1;
        }
    </style>
</head>
<body>
<div class="container chat-container">
    <!-- Historial de conversaciones como menú lateral -->
    <div class="sidebar">
        <div class="accordion" id="historialAccordion">
            <div class="accordion-item">
                <h2 class="accordion-header" id="headingHistorial">
                    <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseHistorial" aria-expanded="true" aria-controls="collapseHistorial">
                        Historial de Conversaciones
                    </button>
                </h2>
                <div id="collapseHistorial" class="accordion-collapse collapse show" aria-labelledby="headingHistorial">
                    <div class="accordion-body">
                        <table class="table table-sm table-bordered">
                            <thead>
                                <tr>
                                    <th>Pregunta</th>
                                    <th>Intención</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% 
                                    for (Conversacion c : ChatDAO.obtenerConversaciones()) { 
                                %>
                                <tr>
                                    <td><%= c.getPregunta() %></td>
                                    <td><%= c.getIntencion() %></td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Chat principal -->
    <div class="chat-box">
        <h3 class="text-primary">Chat Autónomo</h3>
        <form action="procesarChat.jsp" method="post" class="mb-3">
            <div class="mb-2">
                <label for="mensaje">Tu mensaje:</label>
                <input type="text" id="mensaje" name="mensaje" class="form-control" required>
            </div>
            <button id="enviarBtn" type="submit" class="btn btn-success">Enviar</button>
            <div id="spinner" class="spinner-border text-primary ms-2" role="status" style="display: none;">
                <span class="visually-hidden">Procesando...</span>
            </div>

        </form>

        <% if (request.getParameter("respuesta") != null) { %>
        <div class="alert alert-info">
            <strong>Respuesta del chat:</strong> <%= request.getParameter("respuesta") %>
        </div>
        <% } %>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const form = document.querySelector("form");
    const spinner = document.getElementById("spinner");
    const enviarBtn = document.getElementById("enviarBtn");

    form.addEventListener("submit", () => {
        spinner.style.display = "inline-block";
        enviarBtn.disabled = true;
    });
</script>

</body>
</html>
