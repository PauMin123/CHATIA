package modelo;

import java.util.*;

public class ClasificadorNaiveBayes {

    private Map<String, Map<String, Integer>> frecuenciaPalabrasPorIntencion = new HashMap<>();
    private Map<String, Integer> totalPalabrasPorIntencion = new HashMap<>();
    private Map<String, Integer> frecuenciaIntenciones = new HashMap<>();
    private int totalMensajes = 0;

    // Limpia texto y lo tokeniza
    private List<String> limpiarTexto(String texto) {
        texto = texto.toLowerCase().replaceAll("[^a-záéíóúñ\\s]", "").trim();
        return Arrays.asList(texto.split("\\s+"));
    }

    // Entrenar con un mensaje y su intención
    public void entrenar(String texto, String intencion) {
        List<String> palabras = limpiarTexto(texto);

        frecuenciaIntenciones.put(intencion, frecuenciaIntenciones.getOrDefault(intencion, 0) + 1);
        totalMensajes++;

        for (String palabra : palabras) {
            frecuenciaPalabrasPorIntencion
                .computeIfAbsent(intencion, k -> new HashMap<>())
                .put(palabra, frecuenciaPalabrasPorIntencion.get(intencion).getOrDefault(palabra, 0) + 1);

            totalPalabrasPorIntencion.put(intencion, totalPalabrasPorIntencion.getOrDefault(intencion, 0) + 1);
        }
    }

    // Clasificar una nueva frase
    public String clasificar(String texto) {
        List<String> palabras = limpiarTexto(texto);
        String mejorIntencion = null;
        double mejorProb = Double.NEGATIVE_INFINITY;

        for (String intencion : frecuenciaIntenciones.keySet()) {
            double logProb = Math.log((double) frecuenciaIntenciones.get(intencion) / totalMensajes);
            int totalPalabras = totalPalabrasPorIntencion.getOrDefault(intencion, 0);
            int vocabulario = obtenerVocabulario().size();

            for (String palabra : palabras) {
                int frecuenciaPalabra = frecuenciaPalabrasPorIntencion
                        .getOrDefault(intencion, new HashMap<>())
                        .getOrDefault(palabra, 0);

                // Suavizado de Laplace
                double probPalabra = (frecuenciaPalabra + 1.0) / (totalPalabras + vocabulario);
                logProb += Math.log(probPalabra);
            }

            if (logProb > mejorProb) {
                mejorProb = logProb;
                mejorIntencion = intencion;
            }
        }

        return mejorIntencion;
    }

    // Obtener todas las palabras únicas
    private Set<String> obtenerVocabulario() {
        Set<String> vocabulario = new HashSet<>();
        for (Map<String, Integer> mapa : frecuenciaPalabrasPorIntencion.values()) {
            vocabulario.addAll(mapa.keySet());
        }
        return vocabulario;
    }
}
