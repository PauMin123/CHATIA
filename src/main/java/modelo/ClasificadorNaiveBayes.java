package modelo;

import java.util.*;

public class ClasificadorNaiveBayes {

    private Map<String, Map<String, Integer>> frecuenciaPalabrasPorIntencion = new HashMap<>();
    private Map<String, Integer> totalPalabrasPorIntencion = new HashMap<>();
    private Map<String, Integer> frecuenciaIntenciones = new HashMap<>();
    private int totalMensajes = 0;
    private double ultimaProbabilidad = 0.0;

    // Limpia y tokeniza el texto
    private List<String> limpiarTexto(String texto) {
        texto = texto.toLowerCase().replaceAll("[^a-záéíóúñü\\s]", "").trim();
        return Arrays.asList(texto.split("\\s+"));
    }

    // Entrenamiento
    public void entrenar(String texto, String intencion) {
        List<String> palabras = limpiarTexto(texto);

        frecuenciaIntenciones.put(intencion, frecuenciaIntenciones.getOrDefault(intencion, 0) + 1);
        totalMensajes++;

        for (String palabra : palabras) {
            frecuenciaPalabrasPorIntencion
                .computeIfAbsent(intencion, k -> new HashMap<>())
                .merge(palabra, 1, Integer::sum);

            totalPalabrasPorIntencion.put(intencion, totalPalabrasPorIntencion.getOrDefault(intencion, 0) + 1);
        }
    }

    // Clasificación con umbral
    public String clasificar(String texto) {
        return clasificarConProbabilidad(texto, 0.4); // umbral mínimo de confianza
    }

    // Clasificar con probabilidad y umbral ajustable
    public String clasificarConProbabilidad(String texto, double umbral) {
        List<String> palabras = limpiarTexto(texto);
        String mejorIntencion = null;
        double mejorLogProb = Double.NEGATIVE_INFINITY;
        Map<String, Double> logProbs = new HashMap<>();

        for (String intencion : frecuenciaIntenciones.keySet()) {
            double logProb = Math.log((double) frecuenciaIntenciones.get(intencion) / totalMensajes);
            int totalPalabras = totalPalabrasPorIntencion.getOrDefault(intencion, 0);
            int vocabulario = obtenerVocabulario().size();

            for (String palabra : palabras) {
                int frecuenciaPalabra = frecuenciaPalabrasPorIntencion
                        .getOrDefault(intencion, new HashMap<>())
                        .getOrDefault(palabra, 0);
                double probPalabra = (frecuenciaPalabra + 1.0) / (totalPalabras + vocabulario);
                logProb += Math.log(probPalabra);
            }

            logProbs.put(intencion, logProb);

            if (logProb > mejorLogProb) {
                mejorLogProb = logProb;
                mejorIntencion = intencion;
            }
        }

        // Convertimos log probs a probabilidades (normalización)
        double sumaExp = 0.0;
        for (double lp : logProbs.values()) {
            sumaExp += Math.exp(lp - mejorLogProb); // evitar overflow numérico
        }
        ultimaProbabilidad = 1.0 / sumaExp;

        return (ultimaProbabilidad >= umbral) ? mejorIntencion : null;
    }

    // Obtener vocabulario total
    private Set<String> obtenerVocabulario() {
        Set<String> vocabulario = new HashSet<>();
        for (Map<String, Integer> mapa : frecuenciaPalabrasPorIntencion.values()) {
            vocabulario.addAll(mapa.keySet());
        }
        return vocabulario;
    }

    // Consultar la última probabilidad calculada
    public double getUltimaProbabilidad() {
        return ultimaProbabilidad;
    }
}
