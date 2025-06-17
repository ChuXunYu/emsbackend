package com.dne.ems.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dne.ems.dto.UserCreationRequest;
import com.dne.ems.dto.UserUpdateRequest;
import com.dne.ems.exception.UserAlreadyExistsException;
import com.dne.ems.model.UserAccount;
import com.dne.ems.model.enums.Role;
import com.dne.ems.model.enums.UserStatus;
import com.dne.ems.repository.UserAccountRepository;
import com.dne.ems.service.PersonnelService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonnelServiceImpl implements PersonnelService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserAccount> getUsers(Role role, String name, Pageable pageable) {
        Specification<UserAccount> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (role != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), role));
            }

            if (StringUtils.hasText(name)) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };

        return userAccountRepository.findAll(spec, pageable);
    }

    @Override
    public UserAccount createUser(UserCreationRequest request) {
        if (userAccountRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException("Account with email " + request.email() + " already exists.");
        }
        if (userAccountRepository.findByPhone(request.phone()).isPresent()) {
            throw new UserAlreadyExistsException("Account with phone " + request.phone() + " already exists.");
        }

        UserAccount user = new UserAccount();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        user.setRegion(request.region());
        user.setEnabled(true); // Or based on some logic, default to active
        user.setStatus(UserStatus.ACTIVE); // Default to active status

        return userAccountRepository.save(user);
    }

    @Override
    public UserAccount updateUser(Long userId, UserUpdateRequest request) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (StringUtils.hasText(request.name())) {
            user.setName(request.name());
        }

        if (StringUtils.hasText(request.phone())) {
            // Optional: Add validation to ensure new phone isn't already taken by another user
            user.setPhone(request.phone());
        }

        if (StringUtils.hasText(request.region())) {
            user.setRegion(request.region());
        }

        if (request.role() != null) {
            user.setRole(request.role());
        }

        if (request.status() != null) {
            user.setStatus(request.status());
        }

        if (request.gender() != null) {
            user.setGender(request.gender());
        }

        if (request.gridX() != null) {
            user.setGridX(request.gridX());
        }

        if (request.gridY() != null) {
            user.setGridY(request.gridY());
        }

        if (request.level() != null) {
            user.setLevel(request.level());
        }

        if (request.skills() != null && !request.skills().isEmpty()) {
            user.setSkills(request.skills());
        }

        if (request.currentLatitude() != null) {
            user.setCurrentLatitude(request.currentLatitude());
        }

        if (request.currentLongitude() != null) {
            user.setCurrentLongitude(request.currentLongitude());
        }

        return userAccountRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        user.setStatus(UserStatus.INACTIVE);
        userAccountRepository.save(user);
    }

    @Override
    public UserAccount updateOwnProfile(String currentUserEmail, UserUpdateRequest request) {
        UserAccount user = userAccountRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + currentUserEmail));

        // User can update their own name, phone, and region.
        // Role and status changes are ignored.
        if (StringUtils.hasText(request.name())) {
            user.setName(request.name());
        }

        if (StringUtils.hasText(request.phone())) {
            user.setPhone(request.phone());
        }

        if (StringUtils.hasText(request.region())) {
            user.setRegion(request.region());
        }

        return userAccountRepository.save(user);
    }
} 