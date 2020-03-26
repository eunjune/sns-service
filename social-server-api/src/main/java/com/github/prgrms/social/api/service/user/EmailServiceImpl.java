package com.github.prgrms.social.api.service.user;

import com.github.prgrms.social.api.model.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

// Todo 시스템 에러 발생. 메모리 관련 에러라서 원인은 알 수 없음. 추후검토
//@Primary
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    @Value("${app.host}")
    private String host;

    @Override
    public void sendMessage(User user) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(user.getEmail().getAddress());
            mimeMessageHelper.setSubject("SNS service, 회원가입 인증");
            mimeMessageHelper.setText(getHtmlMessage(user), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("fail to send email", e);
        }

    }

    private String getHtmlMessage(User user) {
        Context context = new Context();
        context.setVariable("link", "/check-email-token?token=" + user.getEmailCertificationToken() + "&email=" +user.getEmail().getAddress());
        context.setVariable("name", user.getName());
        context.setVariable("linkName", "인증하기");
        context.setVariable("message", "회원가입 인증을 위해 링크를 클릭하세요.");
        context.setVariable("host", host);

        return templateEngine.process("mail/email-link", context);
    }
}
