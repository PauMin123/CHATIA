/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author paulo
 */

public class ArbolAVL {
    private NodoAVL raiz;

    public NodoAVL getRaiz() {
        return raiz;
    }

    public void insertar(String pregunta, String respuesta) {
        raiz = insertar(raiz, pregunta, respuesta);
    }

    private NodoAVL insertar(NodoAVL nodo, String pregunta, String respuesta) {
        if (nodo == null) return new NodoAVL(pregunta, respuesta);
        if (pregunta.compareTo(nodo.pregunta) < 0)
            nodo.izquierda = insertar(nodo.izquierda, pregunta, respuesta);
        else if (pregunta.compareTo(nodo.pregunta) > 0)
            nodo.derecha = insertar(nodo.derecha, pregunta, respuesta);
        return nodo;
    }

    // Método para buscar una pregunta en el árbol
    public String buscar(String pregunta) {
        return buscarEnNodo(raiz, pregunta);  // Llamada al método recursivo
    }

    private String buscarEnNodo(NodoAVL nodo, String pregunta) {
        if (nodo == null) {
            return null;  // No encontrado
        }

        int comparacion = pregunta.compareTo(nodo.pregunta);

        if (comparacion == 0) {
            return nodo.respuesta;  // Encontrado, devolver respuesta
        } else if (comparacion < 0) {
            return buscarEnNodo(nodo.izquierda, pregunta);  // Buscar en la izquierda
        } else {
            return buscarEnNodo(nodo.derecha, pregunta);  // Buscar en la derecha
        }
    }
}
