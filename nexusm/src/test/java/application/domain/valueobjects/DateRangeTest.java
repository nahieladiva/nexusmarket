package application.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del value object {@link DateRange}.
 */
class DateRangeTest {

    private final LocalDate jun1 = LocalDate.of(2026, 6, 1);
    private final LocalDate jun30 = LocalDate.of(2026, 6, 30);

    @Test
    void containsDateInsideRange() {
        DateRange range = DateRange.of(jun1, jun30);
        assertTrue(range.contains(LocalDate.of(2026, 6, 15)));
    }

    @Test
    void doesNotContainDateOutsideRange() {
        DateRange range = DateRange.of(jun1, jun30);
        assertFalse(range.contains(LocalDate.of(2026, 5, 31)));
        assertFalse(range.contains(jun30.plusDays(1)));
    }

    @Test
    void detectsOverlap() {
        DateRange a = DateRange.of(jun1, jun30);
        DateRange b = DateRange.of(LocalDate.of(2026, 6, 29), LocalDate.of(2026, 7, 5));
        assertTrue(a.overlaps(b));
    }

    @Test
    void rejectsEndBeforeStart() {
        assertThrows(IllegalArgumentException.class, () -> DateRange.of(jun30, jun1));
    }

    @Test
    void computesInclusiveDuration() {
        assertEquals(30, DateRange.of(jun1, jun30).durationInDays());
    }
}