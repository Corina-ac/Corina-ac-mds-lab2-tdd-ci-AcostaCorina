package Ejecutable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Necesario para el refactor de delimitadores

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
    
    // ==========================================================
    // MÉTODO PRINCIPAL
    // ==========================================================
    
    public int add(String numbers) {
        // Caso 1: Cadena nula o vacía
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        
        // 1. CONFIGURACIÓN: Obtiene el delimitador (coma, \n, o custom) y la cadena de números.
        DelimiterConfig config = getDelimiterConfig(numbers);
        String[] nums = splitNumbers(config.numbers, config.regex);
        
        // 2. PROCESAMIENTO: Recorre y suma los números, registrando negativos.
        StringBuilder negativeNumbers = new StringBuilder();
        int sum = 0;
        
        for (String num : nums) {
            // Lógica delegada al método auxiliar para limpieza del loop
            sum += processNumber(num, negativeNumbers);
        }
        
        // 3. VALIDACIÓN: Lanza excepción si hay negativos.
        if (negativeNumbers.length() > 0) {
            throw new IllegalArgumentException("negativos no permitidos: " + negativeNumbers.toString());
        }
        
        return sum;
    }

    // ==========================================================
    // MÉTODOS AUXILIARES (Refactorización)
    // ==========================================================
    
    /**
     * Extrae el patrón de delimitador (regex) y la parte de la cadena que contiene los números.
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
                        // Construir el regex final: Delim1|Delim2|Delim3. 
                        // Uso de Stream/Collectors para una construcción concisa del string.
                        String regex = delimiters.stream()
                            .map(Pattern::quote) // Escapar para regex
                            .collect(Collectors.joining("|")); // Unir con OR lógico
                        
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
    
    /**
     * Procesa un solo número: lo suma si es <= 1000 y registra si es negativo.
     * @param num La subcadena del número a procesar.
     * @param negativeNumbers Builder para registrar los números negativos encontrados.
     * @return El valor del número (o 0 si es inválido o > 1000).
     */
    private int processNumber(String num, StringBuilder negativeNumbers) {
        if (num == null || num.trim().isEmpty()) {
            return 0;
        }
        
        int currentNumber;
        try {
            currentNumber = Integer.parseInt(num.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
        
        // Registrar Negativos
        if (currentNumber < 0) {
            if (negativeNumbers.length() > 0) {
                negativeNumbers.append(", ");
            }
            negativeNumbers.append(currentNumber);
        }
        
        // Regla de ignorar > 1000
        if (currentNumber <= 1000) {
            return currentNumber;
        }
        
        return 0;
    }

    /**
     * Wrapper simple para el método split.
     */
    private String[] splitNumbers(String numbers, String regex) {
        if (numbers == null) {
            return new String[0];
        }
        return numbers.split(regex);
    }
}