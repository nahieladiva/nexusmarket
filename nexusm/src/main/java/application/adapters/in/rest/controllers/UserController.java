package application.adapters.in.rest.controllers;

import application.adapters.in.rest.mappers.UserMapper;
import application.adapters.in.rest.requests.CreateBuyerRequest;
import application.adapters.in.rest.requests.CreateSellerRequest;
import application.adapters.in.rest.responses.UserResponse;
import application.domain.models.User;
import application.domain.ports.in.UserManagementPort;
import application.domain.valueobjects.Email;
import application.domain.valueobjects.UserId;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone los casos de uso de gestión de usuarios vía REST.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserManagementPort userManagementPort;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserManagementPort userManagementPort, UserMapper userMapper,
                          PasswordEncoder passwordEncoder) {
        this.userManagementPort = userManagementPort;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/buyers")
    public ResponseEntity<UserResponse> registerBuyer(
            @RequestBody CreateBuyerRequest request) {
        User user = userManagementPort.registerBuyer(request.fullName(),
            Email.of(request.email()),
            new application.domain.valueobjects.PhoneNumber(request.phone()),
            passwordEncoder.encode(request.passwordHash()),
            userMapper.toDomain(request.defaultShippingAddress()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponse(user));
    }

    @PostMapping("/sellers")
    public ResponseEntity<UserResponse> registerSeller(
            @RequestBody CreateSellerRequest request) {
        User user = userManagementPort.registerSeller(request.fullName(),
            Email.of(request.email()),
            new application.domain.valueobjects.PhoneNumber(request.phone()),
            passwordEncoder.encode(request.passwordHash()),
            request.businessName());
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponse(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(userMapper.toResponse(
            userManagementPort.findUserById(UserId.of(id))));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(
            @RequestParam(required = false) String email) {
        if (email != null && !email.isBlank()) {
            User user = userManagementPort.findByEmail(Email.of(email))
                .orElse(null);
            return ResponseEntity.ok(user == null ? List.of()
                : List.of(userMapper.toResponse(user)));
        }
        return ResponseEntity.ok(userManagementPort.findAllUsers().stream()
            .map(userMapper::toResponse)
            .toList());
    }
}