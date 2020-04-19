package com.github.prgrms.social.api.service.user;

import com.github.prgrms.social.api.model.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

// Todo 시스템 에러 발생. 메모리 관련 에러라서 원인은 알 수 없음. 추후검토
@Profile("prod")
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    @Value("${app.host}")
    private String host;

    @Override
    public void sendEmailCertificationMessage(User user) {
        EmailHtmlMessage emailHtmlMessage = EmailHtmlMessage.builder()
                .link("/check-email-token?token=" + user.getEmailCertificationToken() + "&email=" + user.getEmail().getAddress())
                .name(user.getName())
                .linkName("인증하기")
                .message("회원가입 인증을 위해 링크를 클릭하세요.")
                .build();


        sendEamil(emailHtmlMessage, user, "SNS service, 회원가입 인증");
    }

    @Override
    public void sendEmailLoginLinkMessage(User user, String apiToken) {
        EmailHtmlMessage emailHtmlMessage = EmailHtmlMessage.builder()
                .link("/login-link?token=" + user.getEmailCertificationToken() + "&email=" + user.getEmail().getAddress())
                .name(user.getName())
                .linkName("이메일 로그인")
                .message("이메일 로그인을 위해 링크를 클릭하세요.")
                .build();

        sendEamil(emailHtmlMessage, user, "SNS service, 이메일 로그인");
    }

    private String getHtmlMessage(EmailHtmlMessage emailHtmlMessage) {
        Context context = new Context();
        context.setVariable("link", emailHtmlMessage.getLink());
        context.setVariable("name", emailHtmlMessage.getName());
        context.setVariable("linkName", emailHtmlMessage.getLinkName());
        context.setVariable("message", emailHtmlMessage.getMessage());
        context.setVariable("host", host);

        return templateEngine.process("mail/email-link", context);
    }

    private void sendEamil(EmailHtmlMessage emailHtmlMessage, User user, String subject) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(user.getEmail().getAddress());
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(getHtmlMessage(emailHtmlMessage), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("fail to send email", e);
            throw new RuntimeException(e);
        }
    }
}
