package application.domain.valueobjects;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Value Object de rango de fechas.
 *
 * <p>Operaciones: contención, traslape (overlap) y duración en días.</p>
 */
public final class DateRange {

    private final LocalDate startDate;
    private final LocalDate endDate;

    public DateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                "La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static DateRange of(LocalDate startDate, LocalDate endDate) {
        return new DateRange(startDate, endDate);
    }

    /**
     * Rango inclusivo que comienza en {@code start} y dura {@code days} días.
     */
    public static DateRange startingAt(LocalDate start, long days) {
        if (days < 1) {
            throw new IllegalArgumentException("La duración debe ser de al menos 1 día");
        }
        return new DateRange(start, start.plusDays(days - 1));
    }

    public boolean contains(LocalDate date) {
        return date != null && !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public boolean overlaps(DateRange other) {
        if (other == null) {
            throw new IllegalArgumentException("El rango a comparar no puede ser nulo");
        }
        return !this.endDate.isBefore(other.startDate) && !other.endDate.isBefore(this.startDate);
    }

    /**
     * Número de días del rango (inclusivo).
     */
    public long durationInDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DateRange dateRange = (DateRange) o;
        return startDate.equals(dateRange.startDate) && endDate.equals(dateRange.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }

    @Override
    public String toString() {
        return "[" + startDate + ", " + endDate + "]";
    }
}