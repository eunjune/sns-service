package com.github.prgrms.social.api.service.user;

import com.github.prgrms.social.api.model.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Profile("!prod")
@Slf4j
@Service
@RequiredArgsConstructor
public class ConsoleEmailServiceImpl implements EmailService{

    @Override
    public void sendEmailCertificationMessage(User user) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail().getAddress());
        message.setSubject("SNS service, 회원가입 인증");
        message.setText("/check-email-token?token=" + user.getEmailCertificationToken() + "&email=" +user.getEmail().getAddress());

        log.info(message.getText());
    }

    @Override
    public void sendEmailLoginLinkMessage(User user, String apiToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail().getAddress());
        message.setSubject("SNS service, 이메일 로그인 링크");
        message.setText("/login-link?apiToken=" + apiToken);

        log.info(message.getText());
    }
}
