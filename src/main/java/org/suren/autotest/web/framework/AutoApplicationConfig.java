/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.suren.autotest.web.framework;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Spring零配置
 * @author suren
 * @date 2017年6月8日 上午8:21:10
 */
@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class AutoApplicationConfig
{
    @Value("${mail.host}")
    private String mailHost;
    @Value("${mail.username}")
    private String mailUserName;
    @Value("${mail.password}")
    private String mailPassword;
    @Value("${mail.protocol}")
    private String mailProtocol;
    @Value("${mail.port}")
    private int mailPort;

    @Bean(autowire = Autowire.BY_TYPE)
    public MailSender mailBean()
    {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setUsername(mailUserName);
        mailSender.setPassword(mailPassword);
        mailSender.setProtocol(mailProtocol);
        mailSender.setPort(mailPort);
        setUpMailPro(mailSender);

        return mailSender;
    }

    /**
     * 设置邮箱服务器配置
     * @param mailSender
     */
    private void setUpMailPro(JavaMailSenderImpl mailSender)
    {
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", true);
        javaMailProperties.put("mail.smtp.starttls.enable", true);
        javaMailProperties.put("mail.smtp.timeout", 5000);

        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
