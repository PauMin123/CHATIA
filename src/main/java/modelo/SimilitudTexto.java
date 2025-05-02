/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author paulo
 */

public class SimilitudTexto {
    public static double calcularSimilitud(String texto1, String texto2) {
        texto1 = texto1.toLowerCase().replaceAll("[^a-z0-9]", "");
        texto2 = texto2.toLowerCase().replaceAll("[^a-z0-9]", "");
        
        int distancia = distanciaLevenshtein(texto1, texto2);
        int longitudMaxima = Math.max(texto1.length(), texto2.length());
        return 1.0 - ((double) distancia / longitudMaxima);
    }

    private static int distanciaLevenshtein(String s1, String s2) {
        int[] prev = new int[s2.length() + 1];
        int[] curr = new int[s2.length() + 1];
        
        for (int j = 0; j <= s2.length(); j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            curr[0] = i;
            for (int j = 1; j <= s2.length(); j++) {
                int costo = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + costo);
            }
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[s2.length()];
    }
}

