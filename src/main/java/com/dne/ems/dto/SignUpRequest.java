package com.dne.ems.dto;

import com.dne.ems.model.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "姓名不能为空")
        String name,

        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        String email,

        @NotBlank(message = "手机号不能为空")
        String phone,

        @NotBlank(message = "密码不能为空")
        @Size(min = 8, max = 20, message = "密码长度必须在8到20个字符之间")
        String password,

        @NotNull(message = "角色不能为空")
        Role role,

        @NotBlank(message = "验证码不能为空")
        String verificationCode
) {}