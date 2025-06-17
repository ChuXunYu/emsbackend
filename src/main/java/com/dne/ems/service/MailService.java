package com.dne.ems.service;

/**
 * 邮件服务接口，提供发送各类邮件的功能。
 */
public interface MailService {

    /**
     * 发送密码重置验证码邮件
     *
     * @param to 目标邮箱
     * @param code 6位验证码
     */
    void sendPasswordResetEmail(String to, String code);

    /**
     * 发送账号注册验证码邮件
     *
     * @param to 目标邮箱
     * @param code 6位验证码
     */
    void sendVerificationCodeEmail(String to, String code);
} 