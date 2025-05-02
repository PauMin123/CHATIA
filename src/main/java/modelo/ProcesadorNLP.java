package modelo;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class ProcesadorNLP {
    private Set<String> stopWords = Set.of("el", "la", "de", "que", "y", "a", "en", "un");

    public List<String> limpiarTexto(String texto) {
        // Normalizar acentos
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Convertir a minúsculas y quitar puntuación
        texto = texto.toLowerCase().replaceAll("[¿?¡!.,;:]", "");

        return Arrays.stream(texto.split("\\s+"))
                .filter(palabra -> !stopWords.contains(palabra))
                .collect(Collectors.toList());
    }

    public Map<String, Integer> contarFrecuencias(List<String> palabras) {
        Map<String, Integer> frecuencias = new HashMap<>();
        for (String palabra : palabras) {
            frecuencias.put(palabra, frecuencias.getOrDefault(palabra, 0) + 1);
        }
        return frecuencias;
    }
}
