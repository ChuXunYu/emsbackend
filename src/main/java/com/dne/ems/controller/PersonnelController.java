package com.dne.ems.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dne.ems.dto.UserCreationRequest;
import com.dne.ems.dto.UserRoleUpdateRequest;
import com.dne.ems.dto.UserUpdateRequest;
import com.dne.ems.model.UserAccount;
import com.dne.ems.model.enums.Role;
import com.dne.ems.service.PersonnelService;
import com.dne.ems.service.UserAccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/personnel")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PersonnelController {

    private final PersonnelService personnelService;
    private final UserAccountService userAccountService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserAccount createUser(@RequestBody @Valid UserCreationRequest request) {
        return personnelService.createUser(request);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserAccount>> getUsers(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) String name,
            Pageable pageable) {
        Page<UserAccount> users = personnelService.getUsers(role, name, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserAccount> getUserById(@PathVariable Long userId) {
        UserAccount user = userAccountService.getUserById(userId);
        user.setPassword(null); // Do not expose password
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<UserAccount> updateUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequest request) {
        UserAccount updatedUser = personnelService.updateUser(userId, request);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<UserAccount> updateUserRole(
            @PathVariable Long userId,
            @RequestBody @Valid UserRoleUpdateRequest request) {
        UserAccount updatedUser = userAccountService.updateUserRole(userId, request);
        updatedUser.setPassword(null); // Do not expose password
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        personnelService.deleteUser(userId);
    }
} 