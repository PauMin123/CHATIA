/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author paulo
 */


public class Conversacion {
    private String pregunta;
    private String respuesta;
    private String intencion;

    public Conversacion(String pregunta, String respuesta, String intencion) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.intencion = intencion;
    }

    public String getPregunta() {
        return pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public String getIntencion() {
        return intencion;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public void setIntencion(String intencion) {
        this.intencion = intencion;
    }

    @Override
    public String toString() {
        return "[" + pregunta + " -> " + respuesta + " (" + intencion + ")]";
    }
}
