package application.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del value object {@link Percentage}.
 */
class PercentageTest {

    @Test
    void convertsToFraction() {
        assertEquals(0, Percentage.of(15).asFraction().compareTo(BigDecimal.valueOf(0.15)));
    }

    @Test
    void rejectsValuesOutsideRange() {
        assertThrows(IllegalArgumentException.class, () -> Percentage.of(-1));
        assertThrows(IllegalArgumentException.class, () -> Percentage.of(101));
    }

    @Test
    void acceptsBoundaries() {
        assertTrue(Percentage.of(0).getValue().signum() == 0);
        assertEquals(0, Percentage.of(100).getValue().compareTo(BigDecimal.valueOf(100)));
    }

    @Test
    void equalityIsByValue() {
        assertEquals(Percentage.of(15), Percentage.of(15.00));
    }
}