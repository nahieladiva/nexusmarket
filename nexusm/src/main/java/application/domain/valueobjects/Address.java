package application.domain.valueobjects;

import java.util.Objects;

/**
 * Value Object de dirección postal.
 *
 * <p>Componentes: calle, ciudad, estado, código postal y país.</p>
 */
public final class Address {

    private final String street;
    private final String city;
    private final String state;
    private final String zipCode;
    private final String country;

    public Address(String street, String city, String state, String zipCode, String country) {
        this.street = requireNotBlank(street, "La calle es obligatoria");
        this.city = requireNotBlank(city, "La ciudad es obligatoria");
        this.state = requireNotBlank(state, "El estado es obligatorio");
        this.zipCode = requireNotBlank(zipCode, "El código postal es obligatorio");
        this.country = requireNotBlank(country, "El país es obligatorio");
    }

    public static Address of(String street, String city, String state, String zipCode, String country) {
        return new Address(street, city, state, zipCode, country);
    }

    private static String requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    public String fullAddress() {
        return street + ", " + city + ", " + state + " " + zipCode + ", " + country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return street.equals(address.street)
            && city.equals(address.city)
            && state.equals(address.state)
            && zipCode.equals(address.zipCode)
            && country.equals(address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, zipCode, country);
    }

    @Override
    public String toString() {
        return fullAddress();
    }
}