package application.services;

import application.domain.exceptions.ResourceNotFoundException;
import application.domain.models.Buyer;
import application.domain.models.Seller;
import application.domain.models.User;
import application.domain.ports.in.UserManagementPort;
import application.domain.ports.out.UserRepository;
import application.domain.valueobjects.Address;
import application.domain.valueobjects.Email;
import application.domain.valueobjects.PhoneNumber;
import application.domain.valueobjects.UserId;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación que implementa los casos de uso de gestión de usuarios.
 */
@Service
public class UserApplicationService implements UserManagementPort {

    private final UserRepository userRepository;

    public UserApplicationService(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository,
            "userRepository es obligatorio");
    }

    @Override
    public User registerBuyer(String fullName, Email email, PhoneNumber phone,
                              String passwordHash, Address defaultShippingAddress) {
        Buyer buyer = Buyer.create(fullName, email, phone, passwordHash, defaultShippingAddress);
        return userRepository.save(buyer);
    }

    @Override
    public User registerSeller(String fullName, Email email, PhoneNumber phone,
                               String passwordHash, String businessName) {
        Seller seller = Seller.create(fullName, email, phone, passwordHash, businessName);
        return userRepository.save(seller);
    }

    @Override
    public User findUserById(UserId id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", id.toString()));
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}