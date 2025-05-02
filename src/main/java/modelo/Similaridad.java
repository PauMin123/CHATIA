package modelo;

import java.util.*;

public class Similaridad {

    public static double coseno(Map<String, Integer> v1, Map<String, Integer> v2) {
        Set<String> todas = new HashSet<>();
        todas.addAll(v1.keySet());
        todas.addAll(v2.keySet());

        double dot = 0.0, mag1 = 0.0, mag2 = 0.0;
        for (String palabra : todas) {
            int f1 = v1.getOrDefault(palabra, 0);
            int f2 = v2.getOrDefault(palabra, 0);
            dot += f1 * f2;
            mag1 += Math.pow(f1, 2);
            mag2 += Math.pow(f2, 2);
        }

        return dot / (Math.sqrt(mag1) * Math.sqrt(mag2) + 1e-10);
    }

    // ✅ Método para convertir lista de tokens a mapa de frecuencias
    public static Map<String, Integer> aFrecuencias(List<String> tokens) {
        Map<String, Integer> frecuencias = new HashMap<>();
        for (String token : tokens) {
            frecuencias.put(token, frecuencias.getOrDefault(token, 0) + 1);
        }
        return frecuencias;
    }
}
