package Ejecutable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class StringCalculatorTest {
    
    private final StringCalculator calculator = new StringCalculator();
    
    // ========== ITERACIÓN 1: Cadena vacía ==========
    @Test
    public void testEmptyStringReturnsZero() {
        assertEquals(0, calculator.add(""));
    }
    
    // ========== ITERACIÓN 2: Un número ==========
    @Test
    public void testSingleNumberReturnsSameNumber() {
        assertEquals(1, calculator.add("1"));
        assertEquals(5, calculator.add("5"));
    }
    
    // ========== ITERACIÓN 3: Dos números ==========
    @Test
    public void testTwoNumbersCommaSeparatedReturnsSum() {
        assertEquals(3, calculator.add("1,2"));
        assertEquals(10, calculator.add("4,6"));
    }
    
    // ========== ITERACIÓN 4: N números ==========
    @Test
    public void testMultipleNumbersReturnsSum() {
        assertEquals(6, calculator.add("1,2,3"));
        assertEquals(10, calculator.add("1,2,3,4"));
        assertEquals(15, calculator.add("1,2,3,4,5"));
    }
    
    // ========== ITERACIÓN 5: Saltos de línea ==========
    @Test
    public void testNewLineAsSeparator() {
        assertEquals(6, calculator.add("1\n2,3"));
        assertEquals(10, calculator.add("1\n2\n3\n4"));
        assertEquals(15, calculator.add("1,2\n3,4\n5"));
    }
    
    // ========== ITERACIÓN 6: Delimitador personalizado ==========
    @Test
    public void testCustomDelimiter() {
        assertEquals(3, calculator.add("//;\n1;2"));
        assertEquals(6, calculator.add("//|\n1|2|3"));
        assertEquals(7, calculator.add("//sep\n1sep2sep4"));
    }
    
    // ========== ITERACIÓN 7: Números negativos ==========
    @Test
    public void testNegativeNumbersThrowException() {
        // Un solo negativo
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.add("-1,2");
        });
        assertTrue(exception.getMessage().contains("negativos no permitidos: -1"));
        
        // Múltiples negativos
        exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.add("2,-4,3,-5");
        });
        assertTrue(exception.getMessage().contains("negativos no permitidos: -4, -5"));
        
        // Todos negativos
        exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.add("-1,-2,-3");
        });
        assertTrue(exception.getMessage().contains("negativos no permitidos: -1, -2, -3"));
    }
    
    // ========== EXTRA: Ignorar números > 1000 ==========
    @Test
    public void testIgnoreNumbersGreaterThan1000() {
        assertEquals(2, calculator.add("2,1001"));
        assertEquals(1002, calculator.add("2,1000"));
        assertEquals(6, calculator.add("1001,2,4,2000"));
    }
    
    // ========== EXTRA: Cadena nula ==========
    @Test
    public void testNullInputReturnsZero() {
        assertEquals(0, calculator.add(null));
    }
}
