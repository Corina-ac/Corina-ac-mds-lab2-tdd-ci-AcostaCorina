package Ejecutable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class StringCalculator {
    
    /**
     * Clase auxiliar para encapsular el regex del delimitador y la cadena de números restante.
     */
    private static class DelimiterConfig {
        final String regex;
        final String numbers;

        public DelimiterConfig(String regex, String numbers) {
            this.regex = regex;
            this.numbers = numbers;
        }
    }
    
    /**
     * Procesa la cadena de entrada, calcula la suma y valida los números negativos.
     */
    public int add(String numbers) {
        // Caso 1: Cadena nula o vacía
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        /*
         // REFACTOR: Extraer la lógica de configuración del delimitador
        DelimiterConfig config = getDelimiterConfig(numbers);
        String regex = config.regex;
        numbers = config.numbers;
        
        // Dividir usando el/los delimitadores
        String[] nums = splitNumbers(numbers, regex);
        
        // Validar números negativos y sumar
        StringBuilder negativeNumbers = new StringBuilder();
        int sum = 0;
        
        for (String num : nums) {
            // Saltar cadenas vacías
            if (num == null || num.trim().isEmpty()) {
                continue;
            }
            
            int currentNumber;
            try {
                currentNumber = Integer.parseInt(num.trim());
            } catch (NumberFormatException e) {
                // Si no es número, continuar
                continue;
            }
            
            // Verificar si es negativo (ITERACIÓN 7)
            if (currentNumber < 0) {
                if (negativeNumbers.length() > 0) {
                    negativeNumbers.append(", ");
                }
                negativeNumbers.append(currentNumber);
            }
            
            // Ignorar números mayores a 1000 (EXTRA)
            if (currentNumber <= 1000) {
                sum += currentNumber;
            }
        }
        
        // Lanzar excepción si hay negativos
        if (negativeNumbers.length() > 0) {
            throw new IllegalArgumentException("negativos no permitidos: " + negativeNumbers.toString());
        }
        
        return sum;
      */
        return Integer.parseInt(numbers);
    }

    /**
     * Método auxiliar para extraer los delimitadores y la cadena de números.
     */
    private DelimiterConfig getDelimiterConfig(String numbers) {
        // Detección de delimitador personalizado (//...)
        if (numbers.startsWith("//")) {
            int delimiterEndIndex = numbers.indexOf("\n");
            
            if (delimiterEndIndex != -1) {
                String delimiterSection = numbers.substring(2, delimiterEndIndex);
                
                // Lógica para manejar delimitadores MÚLTIPLES o simples entre corchetes
                if (delimiterSection.startsWith("[")) {
                    List<String> delimiters = new ArrayList<>();
                    // Patrón para encontrar todos los [delimitadores] (regex no codicioso)
                    Pattern p = Pattern.compile("\\[(.*?)\\]");
                    Matcher m = p.matcher(delimiterSection);
                    
                    while (m.find()) {
                        delimiters.add(m.group(1));
                    }
                    
                    if (!delimiters.isEmpty()) {
                        // Construir el regex final: Delim1|Delim2|Delim3
                        StringBuilder sb = new StringBuilder();
                        for (String delim : delimiters) {
                            if (sb.length() > 0) sb.append("|");
                            sb.append(Pattern.quote(delim));
                        }
                        String regex = sb.toString();
                        String remainingNumbers = numbers.substring(delimiterEndIndex + 1);
                        return new DelimiterConfig(regex, remainingNumbers);
                    }
                } else {
                    // Lógica para un solo delimitador sin corchetes (ej: //;\n)
                    String custom = delimiterSection;
                    String regex = Pattern.quote(custom);
                    String remainingNumbers = numbers.substring(delimiterEndIndex + 1);
                    return new DelimiterConfig(regex, remainingNumbers);
                }
            }
        }
        
        // Configuración por defecto (coma o salto de línea)
        return new DelimiterConfig(",|\\n", numbers);
    }

    private String[] splitNumbers(String numbers, String regex) {
        if (numbers == null) {
            return new String[0];
        }
        return numbers.split(regex);
    }
}