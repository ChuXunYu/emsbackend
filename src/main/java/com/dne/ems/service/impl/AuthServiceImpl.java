package com.dne.ems.service.impl;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dne.ems.dto.JwtAuthenticationResponse;
import com.dne.ems.dto.LoginRequest;
import com.dne.ems.dto.SignUpRequest;
import com.dne.ems.exception.UserAlreadyExistsException;
import com.dne.ems.model.PasswordResetToken;
import com.dne.ems.model.UserAccount;
import com.dne.ems.repository.PasswordResetTokenRepository;
import com.dne.ems.repository.UserAccountRepository;
import com.dne.ems.security.CustomUserDetails;
import com.dne.ems.service.AuthService;
import com.dne.ems.service.JwtService;

import com.dne.ems.service.VerificationCodeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final VerificationCodeService verificationCodeService;


    @Override
    @Transactional
    public void registerUser(SignUpRequest signUpRequest) {
        // 1. 校验验证码
        if (!verificationCodeService.verifyCode(signUpRequest.email(), signUpRequest.verificationCode())) {
            throw new IllegalArgumentException("无效或已过期的验证码。");
        }

        // 2. 检查用户是否已存在
        if (userAccountRepository.findByEmail(signUpRequest.email()).isPresent() ||
            userAccountRepository.findByPhone(signUpRequest.phone()).isPresent()) {
            throw new UserAlreadyExistsException("该邮箱或手机号已被注册。");
        }

        // 3. 创建并保存用户
        UserAccount user = new UserAccount();
        user.setName(signUpRequest.name());
        user.setEmail(signUpRequest.email());
        user.setPhone(signUpRequest.phone());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setRole(signUpRequest.role());
        user.setEnabled(true);

        userAccountRepository.save(user);
    }
    
    @Override
    public JwtAuthenticationResponse signIn(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        var user = userAccountRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var userDetails = new CustomUserDetails(user);
        var jwt = jwtService.generateToken(userDetails);
        return new JwtAuthenticationResponse(jwt);
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        Optional<UserAccount> userOptional = userAccountRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            // 为防止用户枚举，我们不透露用户是否存在
            log.info("尝试为不存在的用户发送密码重置验证码: {}", email);
            return;
        }

        // 生成并发送验证码
        try {
            verificationCodeService.sendVerificationCode(email);
            log.info("密码重置验证码已发送: {}", email);
        } catch (Exception e) {
            log.error("发送密码重置验证码失败: {}", e.getMessage(), e);
            throw new RuntimeException("发送密码重置验证码失败", e);
        }
    }

    @Override
    @Transactional
    @Deprecated
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("无效的密码重置令牌。"));

        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            throw new IllegalArgumentException("密码重置令牌已过期。");
        }

        UserAccount user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userAccountRepository.save(user);

        tokenRepository.delete(resetToken);
    }
    
    @Override
    @Transactional
    public void resetPasswordWithCode(String email, String code, String newPassword) {
        // 1. 验证验证码
        if (!verificationCodeService.verifyCode(email, code)) {
            log.warn("密码重置验证码无效: {}", email);
            throw new IllegalArgumentException("验证码无效或已过期。");
        }
        
        // 2. 查找用户
        UserAccount user = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在。"));
        
        // 3. 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userAccountRepository.save(user);
        
        log.info("用户密码重置成功: {}", email);
    }
}