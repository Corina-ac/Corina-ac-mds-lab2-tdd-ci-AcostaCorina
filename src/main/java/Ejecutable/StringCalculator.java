package Ejecutable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Necesario para el refactor de delimitadores

public class StringCalculator {
    
    /**
     * Clase auxiliar (record) que encapsula el regex del delimitador y la cadena de números restante.
     */
    private static class DelimiterConfig {
        final String regex;
        final String numbers;

        public DelimiterConfig(String regex, String numbers) {
            this.regex = regex;
            this.numbers = numbers;
        }
    }
    
   
    public int add(String numbers) {
        // 1. Caso base: Cadena nula o vacía
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        
        // 2. CONFIGURACIÓN: Obtiene el delimitador y la cadena de números (Refactor).
        DelimiterConfig config = getDelimiterConfig(numbers);
        
        // Divide los números usando el regex configurado.
        String[] nums = splitNumbers(config.numbers, config.regex);
        
        // 3. PROCESAMIENTO: Recorre y suma los números.
        StringBuilder negativeNumbers = new StringBuilder();
        int sum = 0;
        
        for (String num : nums) {
            // La lógica de suma, negativos e ignorar > 1000 está en el auxiliar (Refactor).
            sum += processNumber(num, negativeNumbers);
        }
        
        // 4. VALIDACIÓN FINAL: Lanza excepción si hay negativos.
        if (negativeNumbers.length() > 0) {
            throw new IllegalArgumentException("negativos no permitidos: " + negativeNumbers.toString());
        }
        
        return sum;
    }

    private DelimiterConfig getDelimiterConfig(String numbers) {
        if (numbers.startsWith("//")) {
            int delimiterEndIndex = numbers.indexOf("\n");
            
            if (delimiterEndIndex != -1) {
                String delimiterSection = numbers.substring(2, delimiterEndIndex);
                
                if (delimiterSection.startsWith("[")) {
                    List<String> delimiters = new ArrayList<>();
                    Pattern p = Pattern.compile("\\[(.*?)\\]");
                    Matcher m = p.matcher(delimiterSection);
                    
                    while (m.find()) {
                        delimiters.add(m.group(1));
                    }
                    
                    if (!delimiters.isEmpty()) {
                        // Construir el regex final: Delim1|Delim2|Delim3
                        String regex = delimiters.stream()
                            .map(Pattern::quote) 
                            .collect(Collectors.joining("|")); 
                        
                        String remainingNumbers = numbers.substring(delimiterEndIndex + 1);
                        return new DelimiterConfig(regex, remainingNumbers);
                    }
                } else {
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

    private String[] splitNumbers(String numbers, String regex) {
        if (numbers == null) {
            return new String[0];
        }
        return numbers.split(regex);
    }
}