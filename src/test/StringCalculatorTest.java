package Ejecutable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StringCalculatorTest {
    
    private StringCalculator calculator;

    @BeforeEach
    void setUp() {
        // Inicializa la calculadora antes de cada prueba
        calculator = new StringCalculator();
    }
    
    // ========== CASOS ANTERIORES VERIFICADOS ==========
    
    // ITERACIÓN 1: Cadena vacía
    @Test
    public void testEmptyStringReturnsZero() {
        assertEquals(0, calculator.add(""));
    }
    
    // ITERACIÓN 2: Un número (add("1") retorna 1)
    @Test
    public void testSingleNumberReturnsSameNumber() {
        assertEquals(1, calculator.add("1"));
        assertEquals(5, calculator.add("5"));
    }
    
    // ========== REQUISITO ACTUAL ==========
    
    // ITERACIÓN 3: Dos números (add("1,2") retorna 3)
    @Test
    public void testTwoNumbersCommaSeparatedReturnsSum() {
        assertEquals(3, calculator.add("1,2")); // Verifica que retorna 3
        assertEquals(10, calculator.add("4,6"));
    }
    
    // ========== CASOS POSTERIORES (Aseguran que el código es robusto) ==========

    @Test
    public void testMultipleNumbersReturnsSum() {
        assertEquals(6, calculator.add("1,2,3"));
    }
    
    @Test
    public void testNewLineAsSeparator() {
        assertEquals(6, calculator.add("1\n2,3"));
    }
    
    @Test
    public void testCustomDelimiter() {
        assertEquals(3, calculator.add("//;\n1;2"));
    }

    @Test
    public void testMultipleCustomDelimiters() {
        assertEquals(6, calculator.add("//[*][%]\n1*2%3"));
    }
    
    @Test
    public void testNegativeNumbersThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.add("2,-4,3,-5");
        });
        assertTrue(exception.getMessage().contains("negativos no permitidos: -4, -5"));
    }
    
    @Test
    public void testIgnoreNumbersGreaterThan1000() {
        assertEquals(2, calculator.add("2,1001"));
    }
    
    @Test
    public void testNullInputReturnsZero() {
        assertEquals(0, calculator.add(null));
    }
}