package modelo;

import java.util.*;

public class ArbolAVL {
    private NodoAVL raiz;
    private ProcesadorNLP nlp = new ProcesadorNLP();

    public void insertar(Conversacion dato) {
        raiz = insertar(raiz, dato);
    }

    private NodoAVL insertar(NodoAVL nodo, Conversacion dato) {
        if (nodo == null) return new NodoAVL(dato);

        if (dato.getPregunta().compareToIgnoreCase(nodo.dato.getPregunta()) < 0)
            nodo.izquierdo = insertar(nodo.izquierdo, dato);
        else
            nodo.derecho = insertar(nodo.derecho, dato);

        actualizarAltura(nodo);
        return balancear(nodo);
    }

    private void actualizarAltura(NodoAVL nodo) {
        int altIzq = altura(nodo.izquierdo);
        int altDer = altura(nodo.derecho);
        nodo.altura = Math.max(altIzq, altDer) + 1;
    }

    private int altura(NodoAVL nodo) {
        return nodo == null ? 0 : nodo.altura;
    }

    private int factorBalance(NodoAVL nodo) {
        return altura(nodo.izquierdo) - altura(nodo.derecho);
    }

    private NodoAVL balancear(NodoAVL nodo) {
        int balance = factorBalance(nodo);

        if (balance > 1) {
            if (factorBalance(nodo.izquierdo) < 0)
                nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }

        if (balance < -1) {
            if (factorBalance(nodo.derecho) > 0)
                nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    private NodoAVL rotarDerecha(NodoAVL y) {
        NodoAVL x = y.izquierdo;
        NodoAVL T2 = x.derecho;

        x.derecho = y;
        y.izquierdo = T2;

        actualizarAltura(y);
        actualizarAltura(x);

        return x;
    }

    private NodoAVL rotarIzquierda(NodoAVL x) {
        NodoAVL y = x.derecho;
        NodoAVL T2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = T2;

        actualizarAltura(x);
        actualizarAltura(y);

        return y;
    }

    // Buscar conversación más similar con umbral de similitud
    public Conversacion buscarSimilar(List<String> tokensEntrada) {
        ResultadoBusqueda resultado = buscarSimilarRec(raiz, tokensEntrada, null, -1.0);

        if (resultado.similitud >= 0.5) {
            System.out.println("✅ Similitud aceptada: " + resultado.similitud);
            return resultado.conversacion;
        } else {
            System.out.println("⚠️ Similitud demasiado baja: " + resultado.similitud);
            return null;
        }
    }

    private ResultadoBusqueda buscarSimilarRec(NodoAVL nodo, List<String> entrada, Conversacion mejor, double mejorSim) {
        if (nodo == null) return new ResultadoBusqueda(mejor, mejorSim);

        List<String> tokensNodo = nlp.limpiarTexto(nodo.dato.getPregunta());

        Map<String, Integer> entradaMap = Similaridad.aFrecuencias(entrada);
        Map<String, Integer> nodoMap = Similaridad.aFrecuencias(tokensNodo);

        double sim = Similaridad.coseno(entradaMap, nodoMap);

        System.out.println("🔍 Comparando con: \"" + nodo.dato.getPregunta() + "\" => Similitud: " + sim);

        if (sim > mejorSim) {
            mejorSim = sim;
            mejor = nodo.dato;
        }

        ResultadoBusqueda izq = buscarSimilarRec(nodo.izquierdo, entrada, mejor, mejorSim);
        ResultadoBusqueda der = buscarSimilarRec(nodo.derecho, entrada, izq.conversacion, izq.similitud);

        return der.similitud > izq.similitud ? der : izq;
    }

    private static class ResultadoBusqueda {
        Conversacion conversacion;
        double similitud;

        ResultadoBusqueda(Conversacion conversacion, double similitud) {
            this.conversacion = conversacion;
            this.similitud = similitud;
        }
    }

   public List<Conversacion> obtenerConversaciones() {
    List<Conversacion> lista = new ArrayList<>();
    inorden(raiz, lista);
    return lista;
}

private void inorden(NodoAVL nodo, List<Conversacion> lista) {
    if (nodo != null) {
        inorden(nodo.izquierdo, lista);
        lista.add(nodo.dato); // ✅ corregido
        inorden(nodo.derecho, lista);
    }
}

    // Buscar una conversación por intención (para uso con ML)
public Conversacion buscarPorIntencion(String intencion) {
    return buscarPorIntencionRec(raiz, intencion);
}

private Conversacion buscarPorIntencionRec(NodoAVL nodo, String intencion) {
    if (nodo == null) return null;

    if (nodo.dato.getIntencion().equalsIgnoreCase(intencion)) {
        return nodo.dato;
    }

    Conversacion izq = buscarPorIntencionRec(nodo.izquierdo, intencion);
    if (izq != null) return izq;

    return buscarPorIntencionRec(nodo.derecho, intencion);
}

}
