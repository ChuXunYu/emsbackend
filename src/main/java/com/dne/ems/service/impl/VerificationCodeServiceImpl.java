package com.dne.ems.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.dne.ems.service.MailService;
import com.dne.ems.service.VerificationCodeService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 验证码服务实现类，负责生成、发送和验证验证码
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {
    
    // 使用Guava Cache存储验证码，5分钟后自动过期
    private final Cache<String, String> verificationCodeCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();
    
    // 使用Guava Cache存储最后一次发送时间，限制发送频率
    private final Cache<String, LocalDateTime> lastSendTimeCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();
    
    private final MailService mailService;

    @Override
    public void sendVerificationCode(String email) {
        // 检查发送频率限制
        LocalDateTime lastSendTime = lastSendTimeCache.getIfPresent(email);
        if (lastSendTime != null) {
            LocalDateTime now = LocalDateTime.now();
            // 60秒内只能发送一次验证码
            if (lastSendTime.plusSeconds(60).isAfter(now)) {
                long secondsLeft = 60 - java.time.Duration.between(lastSendTime, now).getSeconds();
                log.warn("验证码请求过于频繁：{}, 剩余等待时间: {}秒", email, secondsLeft);
                throw new IllegalStateException("请求过于频繁，请在" + secondsLeft + "秒后再试。");
            }
        }
        
        // 生成6位随机验证码
        String code = generateCode();
        
        // 存储验证码和发送时间
        verificationCodeCache.put(email, code);
        lastSendTimeCache.put(email, LocalDateTime.now());
        
        // 发送邮件
        mailService.sendVerificationCodeEmail(email, code);
        log.info("验证码已发送到: {}", email);
    }

    @Override
    public boolean verifyCode(String email, String code) {
        if (email == null || code == null) {
            return false;
        }
        
        // 从缓存中获取验证码
        String storedCode = verificationCodeCache.getIfPresent(email);
        
        if (storedCode == null) {
            log.warn("验证码不存在或已过期: {}", email);
            return false;
        }
        
        // 验证成功后，从缓存中移除验证码，防止重复使用
        if (storedCode.equals(code)) {
            verificationCodeCache.invalidate(email);
            log.info("验证码验证成功: {}", email);
            return true;
        }
        
        log.warn("验证码不匹配: {}", email);
        return false;
    }
    
    /**
     * 生成6位随机数字验证码
     */
    private String generateCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000); // 生成100000-999999之间的随机数
        return String.valueOf(code);
    }
} 