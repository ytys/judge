package com.github.zhanhb.judge.config.metrics;

import java.util.Objects;
import javax.mail.MessagingException;
import javax.mail.Transport;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * SpringBoot Actuator HealthIndicator check for JavaMail.
 */
@Slf4j
public class JavaMailHealthIndicator extends AbstractHealthIndicator {

    private final JavaMailSenderImpl javaMailSender;

    public JavaMailHealthIndicator(@NonNull JavaMailSenderImpl javaMailSender) {
        this.javaMailSender = Objects.requireNonNull(javaMailSender, "mailSender");
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        log.debug("Initializing JavaMail health indicator");
        try {
            Transport transport = javaMailSender.getSession().getTransport();
            transport.connect(javaMailSender.getHost(),
                    javaMailSender.getPort(),
                    javaMailSender.getUsername(),
                    javaMailSender.getPassword());
            transport.close();

            builder.up();

        } catch (MessagingException e) {
            log.debug("Cannot connect to e-mail server. Error: {}", e.getMessage());
            builder.down(e);
        }
    }
}
