package com.dne.ems.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dne.ems.dto.UserRegistrationRequest;
import com.dne.ems.dto.UserRoleUpdateRequest;
import com.dne.ems.dto.UserSummaryDTO;
//import com.dne.ems.exception.InvalidOperationException;
import com.dne.ems.exception.ResourceNotFoundException;
import com.dne.ems.exception.UserAlreadyExistsException;
import com.dne.ems.model.UserAccount;
import com.dne.ems.model.enums.Role;
import com.dne.ems.model.enums.UserStatus;
import com.dne.ems.repository.UserAccountRepository;
import com.dne.ems.service.UserAccountService;

//import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Implementation of the {@link UserAccountService} interface.
 * Handles the business logic for user account management.
 */
@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserAccount registerUser(UserRegistrationRequest request) {
        // Check if a user with the given email or phone number already exists.
        userAccountRepository.findByEmail(request.email()).ifPresent(user -> {
            throw new UserAlreadyExistsException("邮箱 " + request.email() + " 已被注册。");
        });
        userAccountRepository.findByPhone(request.phone()).ifPresent(user -> {
            throw new UserAlreadyExistsException("手机号 " + request.phone() + " 已被注册。");
        });

        // Create a new UserAccount entity from the request DTO.
        UserAccount newUser = new UserAccount();
        newUser.setName(request.name());
        newUser.setEmail(request.email());
        newUser.setPhone(request.phone());
        // Encode the password before saving.
        newUser.setPassword(passwordEncoder.encode(request.password()));
        // Set default values for new users.
        newUser.setRole(Role.PUBLIC_SUPERVISOR);
        newUser.setStatus(UserStatus.ACTIVE);

        // Save the new user to the database.
        return userAccountRepository.save(newUser);
    }

    @Override
    @Transactional
    public UserAccount updateUserRole(Long userId, UserRoleUpdateRequest request) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setRole(request.getRole());

        // If the user is a grid worker, update their grid location.
        if (request.getRole() == Role.GRID_WORKER) {
            user.setGridX(request.getGridX());
            user.setGridY(request.getGridY());
        }

        return userAccountRepository.save(user);
    }

    @Override
    public UserAccount getUserById(Long userId) {
        return userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    @Override
    public Page<UserAccount> getUsersByRole(Role role, Pageable pageable) {
        return userAccountRepository.findByRole(role, pageable);
    }

    @Override
    public void updateUserLocation(Long userId, Double latitude, Double longitude) {
        UserAccount user = getUserById(userId);
        user.setCurrentLatitude(latitude);
        user.setCurrentLongitude(longitude);
        userAccountRepository.save(user);
    }

    @Override
    public Page<UserSummaryDTO> getAllUsers(Role role, UserStatus status, Pageable pageable) {
        Specification<UserAccount> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new java.util.ArrayList<>();
            if (role != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), role));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };

        Page<UserAccount> userPage = userAccountRepository.findAll(spec, pageable);
        return userPage.map(this::convertToSummaryDto);
    }

    private UserSummaryDTO convertToSummaryDto(UserAccount user) {
        return new UserSummaryDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getStatus()
        );
    }
} 