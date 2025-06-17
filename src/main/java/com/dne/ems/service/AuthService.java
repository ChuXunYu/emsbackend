package com.dne.ems.service;

import com.dne.ems.dto.JwtAuthenticationResponse;
import com.dne.ems.dto.LoginRequest;
import com.dne.ems.dto.SignUpRequest;

/**
 * 认证服务接口，提供用户认证和安全相关的核心功能
 */
public interface AuthService {

    /**
     * 请求密码重置，发送重置验证码到用户邮箱
     * 
     * @param email 用户邮箱
     */
    void requestPasswordReset(String email);

    /**
     * 使用令牌重置密码（旧方法，保留以兼容历史代码）
     * 
     * @param token 密码重置令牌
     * @param newPassword 新密码
     * @deprecated 使用基于验证码的 {@link #resetPasswordWithCode(String, String, String)} 替代
     */
    @Deprecated
    void resetPassword(String token, String newPassword);
    
    /**
     * 使用验证码重置密码
     * 
     * @param email 用户邮箱
     * @param code 验证码
     * @param newPassword 新密码
     */
    void resetPasswordWithCode(String email, String code, String newPassword);

    /**
     * 注册新用户
     * 
     * @param signUpRequest 用户注册信息
     */
    void registerUser(SignUpRequest signUpRequest);

    /**
     * 用户登录
     * 
     * @param request 登录请求
     * @return JWT认证响应
     */
    JwtAuthenticationResponse signIn(LoginRequest request);
}