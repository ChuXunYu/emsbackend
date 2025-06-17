package com.dne.ems.service;

/**
 * 验证码服务接口，定义了生成、发送和校验验证码的核心功能。
 */
public interface VerificationCodeService {

    /**
     * 生成验证码，并通过邮件发送给指定用户。
     * 该方法会处理发送频率的限制（例如60秒冷却）。
     *
     * @param email 接收验证码的电子邮箱地址。
     * @throws IllegalStateException 如果请求过于频繁（处于冷却期内）。
     */
    void sendVerificationCode(String email);

    /**
     * 校验用户提交的验证码是否正确且有效。
     *
     * @param email 电子邮箱地址。
     * @param code  用户提交的6位验证码。
     * @return 如果验证码正确且在有效期内，则返回 true；否则返回 false。
     */
    boolean verifyCode(String email, String code);
} 