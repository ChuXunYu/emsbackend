package com.dne.ems.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.dne.ems.service.MailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮件服务实现类，负责通过 JavaMailSender 发送各类邮件
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendPasswordResetEmail(String to, String code) {
        String subject = "【EMS系统】您的密码重置验证码";
        String content = "<html><body>"
                + "<p>您好,</p>"
                + "<p>我们收到了您的密码重置请求。以下是您的验证码：</p>"
                + "<div style='background-color: #f4f4f4; padding: 10px; text-align: center; font-size: 24px; font-weight: bold; letter-spacing: 5px; margin: 20px 0;'>"
                + code
                + "</div>"
                + "<p>验证码有效期为5分钟，请勿将验证码泄露给他人。</p>"
                + "<p>如果您没有请求重置密码，请忽略此邮件。</p>"
                + "<p>谢谢,<br/>EMS系统团队</p>"
                + "</body></html>";

        sendHtmlEmail(to, subject, content);
        log.info("密码重置验证码已发送到邮箱: {}", to);
    }
    
    @Override
    public void sendVerificationCodeEmail(String to, String code) {
        String subject = "【EMS系统】您的注册验证码";
        String content = "<html><body>"
                + "<h3>欢迎注册EMS系统</h3>"
                + "<p>您的邮箱验证码是:</p>"
                + "<div style='background-color: #f4f4f4; padding: 10px; text-align: center; font-size: 24px; font-weight: bold; letter-spacing: 5px; margin: 20px 0;'>"
                + code
                + "</div>"
                + "<p>该验证码5分钟内有效，请勿泄露给他人。</p>"
                + "</body></html>";
        
        sendHtmlEmail(to, subject, content);
        log.info("注册验证码已发送到邮箱: {}", to);
    }

    /**
     * 发送HTML格式的邮件
     *
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param htmlContent HTML格式的邮件内容
     */
    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true表示支持HTML内容
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("发送邮件失败: {}", e.getMessage(), e);
            throw new RuntimeException("发送邮件失败", e);
        }
    }
} 