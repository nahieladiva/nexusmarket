package application.adapters.out.persistence.mysql.mappers;

import application.adapters.out.persistence.mysql.entities.UserJpaEntity;
import application.domain.enums.UserRole;
import application.domain.models.Buyer;
import application.domain.models.Seller;
import application.domain.models.User;
import application.domain.valueobjects.Address;
import application.domain.valueobjects.Email;
import application.domain.valueobjects.PhoneNumber;
import application.domain.valueobjects.UserId;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Mapea entre la entidad JPA y el modelo de dominio de usuarios.
 */
@Component
public class UserEntityMapper {

    public UserJpaEntity toEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity(
            user.getId().toString(),
            user.getFullName(),
            user.getEmail().getValue(),
            user.getPhone().getValue(),
            user.getRole().name(),
            user.getPasswordHash(),
            user.getCreatedAt());
        if (user instanceof Seller seller) {
            entity.setBusinessName(seller.getBusinessName());
            entity.setApproved(seller.isApproved());
        } else if (user instanceof Buyer buyer) {
            Address address = buyer.getDefaultShippingAddress();
            entity.setShippingStreet(address.getStreet());
            entity.setShippingCity(address.getCity());
            entity.setShippingState(address.getState());
            entity.setShippingZip(address.getZipCode());
            entity.setShippingCountry(address.getCountry());
        }
        return entity;
    }

    public User toDomain(UserJpaEntity entity) {
        UserRole role = UserRole.valueOf(entity.getRole());
        UserId id = UserId.of(entity.getId());
        return switch (role) {
            case BUYER -> new Buyer(
                id,
                entity.getFullName(),
                Email.of(entity.getEmail()),
                PhoneNumber.of(entity.getPhone()),
                entity.getPasswordHash(),
                Address.of(entity.getShippingStreet(), entity.getShippingCity(),
                    entity.getShippingState(), entity.getShippingZip(),
                    entity.getShippingCountry()),
                List.of(),
                entity.getCreatedAt());
            case SELLER -> new Seller(
                id,
                entity.getFullName(),
                Email.of(entity.getEmail()),
                PhoneNumber.of(entity.getPhone()),
                entity.getPasswordHash(),
                entity.getBusinessName(),
                List.of(),
                entity.isApproved(),
                entity.getCreatedAt());
            default -> new User(
                id,
                entity.getFullName(),
                Email.of(entity.getEmail()),
                PhoneNumber.of(entity.getPhone()),
                role,
                entity.getPasswordHash(),
                List.of(),
                entity.getCreatedAt());
        };
    }
}