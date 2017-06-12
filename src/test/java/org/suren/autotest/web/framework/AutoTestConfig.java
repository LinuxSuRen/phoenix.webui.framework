package org.suren.autotest.web.framework;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Created by zhaoxj on 2017/6/12.
 */
@Configuration
public class AutoTestConfig {

    @Bean
    public JavaMailSender mailBean() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("361981269");
        mailSender.setPassword("walkMAN227");
        return mailSender;
    }
}
