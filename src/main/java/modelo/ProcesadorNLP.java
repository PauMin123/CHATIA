/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author paulo
 */
import java.util.*;

public class ProcesadorNLP {

    private static final Map<String, String> respuestas = new HashMap<>();

    static {
        respuestas.put("hola", "¡Hola! ¿En qué puedo ayudarte?");
        respuestas.put("adios", "¡Hasta luego!");
        respuestas.put("nombre", "Soy un chatbot inteligente.");
        respuestas.put("hora", "No tengo reloj, pero siempre es buen momento para conversar.");
    }

    public static String procesar(String mensaje) {
        mensaje = mensaje.toLowerCase();
        for (String clave : respuestas.keySet()) {
            if (mensaje.contains(clave)) {
                return respuestas.get(clave);
            }
        }
        return "No entendí muy bien, ¿puedes reformularlo?";
    }
}

