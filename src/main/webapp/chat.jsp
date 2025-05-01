<%-- 
    Document   : chat
    Created on : 30 abr 2025, 10:43:36 p.m.
    Author     : paulo
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Chat Autónomo</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f4f4f4;
            padding: 40px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        form {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }

        input[type="text"] {
            padding: 10px;
            width: 300px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        input[type="submit"] {
            padding: 10px 15px;
            background-color: #007bff;
            border: none;
            color: white;
            border-radius: 5px;
            cursor: pointer;
        }

        .respuesta {
            background: white;
            padding: 20px;
            border-radius: 5px;
            width: 350px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    <h2>Chat Autónomo 🤖</h2>
    <form action="procesarChat.jsp" method="post">
        <input type="text" name="mensaje" placeholder="Escribe tu mensaje" required />
        <input type="submit" value="Enviar" />
    </form>
    <div class="respuesta">
        <strong>Respuesta:</strong>
        <p><%= request.getAttribute("respuesta") != null ? request.getAttribute("respuesta") : "" %></p>
    </div>
</body>
</html>

