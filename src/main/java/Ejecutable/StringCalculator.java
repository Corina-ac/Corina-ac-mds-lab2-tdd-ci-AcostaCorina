package Ejecutable;

import java.util.regex.Pattern;

public class StringCalculator {
    
    public int add(String numbers) {
        // Caso 1: Cadena nula o vacía
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        
        // Detectar y configurar delimitador personalizado
        String regex;
        if (numbers.startsWith("//")) {
            int delimiterEndIndex = numbers.indexOf("\n");
            if (delimiterEndIndex != -1) {
                // Extraer delimitador personalizado (puede ser de varios caracteres)
                String custom = numbers.substring(2, delimiterEndIndex);
                // Usar Pattern.quote para que caracteres especiales no rompan el regex
                regex = Pattern.quote(custom);
                // Extraer los números después del delimitador
                numbers = numbers.substring(delimiterEndIndex + 1);
            } else {
                // Formato inválido, tratar como sin delimitador personalizado
                regex = ",|\\n";
            }
        } else {
            // Delimitadores por defecto: coma o salto de línea
            regex = ",|\\n";
        }
        
        // Dividir usando el/los delimitadores
        String[] nums = numbers.split(regex);
        
        // Validar números negativos
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
            
            // Verificar si es negativo
            if (currentNumber < 0) {
                if (negativeNumbers.length() > 0) {
                    negativeNumbers.append(", ");
                }
                negativeNumbers.append(currentNumber);
            }
            
            // Ignorar números mayores a 1000
            if (currentNumber <= 1000) {
                sum += currentNumber;
            }
        }
        
        // Lanzar excepción si hay negativos
        if (negativeNumbers.length() > 0) {
            throw new IllegalArgumentException("negativos no permitidos: " + negativeNumbers.toString());
        }
        
        return sum;
    }
}