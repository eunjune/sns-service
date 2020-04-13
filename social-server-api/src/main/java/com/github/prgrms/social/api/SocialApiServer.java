package com.github.prgrms.social.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SocialApiServer {
    public static void main(String[] args) {
        SpringApplication.run(SocialApiServer.class, args);
    }
}
