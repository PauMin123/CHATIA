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
    private int id;
    private String usuario;
    private String mensaje;
    private String respuesta;

    // Constructor
    public Conversacion(int id, String usuario, String mensaje, String respuesta) {
        this.id = id;
        this.usuario = usuario;
        this.mensaje = mensaje;
        this.respuesta = respuesta;
    }

    // Getters y setters
    public int getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getMensaje() { return mensaje; }
    public String getRespuesta() { return respuesta; }

    public void setId(int id) { this.id = id; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
    
}
