
package modelo;

import java.util.*;

public class ModeloML {
    private Map<String, Map<String, Integer>> entrenamiento = new HashMap<>();

    public void entrenar(String clase, List<String> tokens) {
        Map<String, Integer> claseData = entrenamiento.getOrDefault(clase, new HashMap<>());
        for (String token : tokens) {
            claseData.put(token, claseData.getOrDefault(token, 0) + 1);
        }
        entrenamiento.put(clase, claseData);
    }

    public String predecir(List<String> tokens) {
        String mejorClase = null;
        double mejorPuntaje = Double.NEGATIVE_INFINITY;

        for (var entrada : entrenamiento.entrySet()) {
            String clase = entrada.getKey();
            Map<String, Integer> frecuencias = entrada.getValue();
            double puntaje = 0.0;
            for (String token : tokens) {
                puntaje += Math.log(frecuencias.getOrDefault(token, 1));
            }
            if (puntaje > mejorPuntaje) {
                mejorPuntaje = puntaje;
                mejorClase = clase;
            }
        }

        return mejorClase != null ? mejorClase : "desconocido";
    }

    public void limpiarModelo() {
        entrenamiento.clear();
    }
}
