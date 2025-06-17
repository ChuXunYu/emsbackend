package com.dne.ems.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dne.ems.dto.JwtAuthenticationResponse;
import com.dne.ems.dto.LoginRequest;
import com.dne.ems.dto.PasswordResetDto;
import com.dne.ems.dto.PasswordResetRequest;
import com.dne.ems.dto.PasswordResetWithCodeDto;
import com.dne.ems.dto.SignUpRequest;
import com.dne.ems.service.AuthService;
import com.dne.ems.service.VerificationCodeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final VerificationCodeService verificationCodeService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.signIn(request));
    }

    /**
     * 请求发送账号注册验证码到指定邮箱。
     *
     * @param email 接收验证码的邮箱地址
     * @return 成功则返回200 OK
     */
    @PostMapping("/send-verification-code")
    public ResponseEntity<Void> sendVerificationCode(@RequestParam @NotBlank @Email String email) {
        verificationCodeService.sendVerificationCode(email);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 请求发送密码重置验证码到指定邮箱。
     *
     * @param email 接收验证码的邮箱地址
     * @return 成功则返回200 OK
     */
    @PostMapping("/send-password-reset-code")
    public ResponseEntity<Void> sendPasswordResetCode(@RequestParam @NotBlank @Email String email) {
        authService.requestPasswordReset(email);
        return ResponseEntity.ok().build();
    }

    /**
     * 请求密码重置（将发送包含验证码的邮件）
     * 
     * @param request 包含邮箱的请求
     * @return 成功则返回200 OK
     * @deprecated 使用 {@link #sendPasswordResetCode(String)} 代替
     */
    @PostMapping("/request-password-reset")
    @Deprecated
    public ResponseEntity<Void> requestPasswordReset(@Valid @RequestBody PasswordResetRequest request) {
        authService.requestPasswordReset(request.email());
        return ResponseEntity.ok().build();
    }
    
    /**
     * 使用验证码重置密码
     * 
     * @param request 包含邮箱、验证码和新密码的请求
     * @return 成功则返回200 OK
     */
    @PostMapping("/reset-password-with-code")
    public ResponseEntity<Void> resetPasswordWithCode(@Valid @RequestBody PasswordResetWithCodeDto request) {
        authService.resetPasswordWithCode(request.email(), request.code(), request.newPassword());
        return ResponseEntity.ok().build();
    }

    /**
     * 使用令牌重置密码（旧版API，保留以兼容）
     * 
     * @param request 包含令牌和新密码的请求
     * @return 成功则返回200 OK
     * @deprecated 使用基于验证码的 {@link #resetPasswordWithCode(PasswordResetWithCodeDto)} 替代
     */
    @PostMapping("/reset-password")
    @Deprecated
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody PasswordResetDto request) {
        authService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok().build();
    }

    // Internal DTOs have been moved to the com.dne.ems.dto package
}