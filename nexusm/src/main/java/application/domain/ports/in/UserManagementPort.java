package application.domain.ports.in;

import application.domain.models.User;
import application.domain.valueobjects.Address;
import application.domain.valueobjects.Email;
import application.domain.valueobjects.PhoneNumber;
import application.domain.valueobjects.UserId;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada (casos de uso) de gestión de usuarios.
 */
public interface UserManagementPort {

    User registerBuyer(String fullName, Email email, PhoneNumber phone,
                       String passwordHash, Address defaultShippingAddress);

    User registerSeller(String fullName, Email email, PhoneNumber phone,
                        String passwordHash, String businessName);

    User findUserById(UserId id);

    Optional<User> findByEmail(Email email);

    List<User> findAllUsers();
}