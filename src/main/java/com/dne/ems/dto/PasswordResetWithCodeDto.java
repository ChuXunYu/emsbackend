package com.dne.ems.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 基于验证码的密码重置DTO
 */
public record PasswordResetWithCodeDto(
        @NotEmpty(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        String email,

        @NotEmpty(message = "验证码不能为空")
        String code,

        @NotEmpty(message = "新密码不能为空")
        @Size(min = 8, message = "密码长度至少为8个字符")
        String newPassword
) {} 