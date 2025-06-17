package com.dne.ems.dto;

import com.dne.ems.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
// import jakarta.validation.constraints.Pattern;
// import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for user registration requests.
 * Carries data from the controller to the service layer.
 * Includes validation annotations to ensure data integrity.
 *
 * @param name     The user's real name.
 * @param phone    The user's unique phone number.
 * @param email    The user's unique email address.
 * @param password The user's password, which must meet complexity requirements.
 */
public record UserRegistrationRequest(
    @NotEmpty(message = "姓名不能为空")
    String name,

    @NotEmpty(message = "手机号不能为空")
    String phone,

    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "无效的邮箱格式")
    String email,

    @NotEmpty(message = "密码不能为空")
    @ValidPassword
    String password
) {} 