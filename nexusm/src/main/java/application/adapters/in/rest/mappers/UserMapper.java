package application.adapters.in.rest.mappers;

import application.adapters.in.rest.requests.AddressRequest;
import application.adapters.in.rest.requests.CreateBuyerRequest;
import application.adapters.in.rest.requests.CreateSellerRequest;
import application.adapters.in.rest.responses.AddressResponse;
import application.adapters.in.rest.responses.UserResponse;
import application.domain.models.Buyer;
import application.domain.models.Seller;
import application.domain.models.User;
import application.domain.valueobjects.Address;
import application.domain.valueobjects.Email;
import application.domain.valueobjects.PhoneNumber;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Mapea entre DTOs REST y el modelo de dominio de usuarios.
 */
@Component
public class UserMapper {

    public Buyer toBuyer(CreateBuyerRequest request, String encodedPasswordHash) {
        Address shipping = toDomain(request.defaultShippingAddress());
        return Buyer.create(request.fullName(), Email.of(request.email()),
            PhoneNumber.of(request.phone()), encodedPasswordHash, shipping);
    }

    public Seller toSeller(CreateSellerRequest request, String encodedPasswordHash) {
        return Seller.create(request.fullName(), Email.of(request.email()),
            PhoneNumber.of(request.phone()), encodedPasswordHash, request.businessName());
    }

    public UserResponse toResponse(User user) {
        String businessName = null;
        AddressResponse defaultShipping = null;
        if (user instanceof Seller seller) {
            businessName = seller.getBusinessName();
        } else if (user instanceof Buyer buyer) {
            defaultShipping = toResponse(buyer.getDefaultShippingAddress());
        }
        List<AddressResponse> addresses = user.getAddresses().stream()
            .map(this::toResponse)
            .toList();
        return new UserResponse(
            user.getId().toString(),
            user.getFullName(),
            user.getEmail().getValue(),
            user.getPhone().getValue(),
            user.getRole().name(),
            businessName,
            defaultShipping,
            addresses,
            user.getCreatedAt());
    }

    public AddressResponse toResponse(Address address) {
        return new AddressResponse(address.getStreet(), address.getCity(),
            address.getState(), address.getZipCode(), address.getCountry());
    }

    public Address toDomain(AddressRequest address) {
        return Address.of(address.street(), address.city(), address.state(),
            address.zipCode(), address.country());
    }
}