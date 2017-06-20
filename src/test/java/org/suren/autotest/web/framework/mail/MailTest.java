/*
 *
 *  * Copyright 2002-2007 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.suren.autotest.web.framework.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.suren.autotest.web.framework.AutoApplicationConfig;
import org.suren.autotest.web.framework.AutoTestConfig;

/**
 * @author suren
 */
@ContextConfiguration(classes = {AutoTestConfig.class, AutoApplicationConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class MailTest
{
    @Autowired
    private MailSender mailSender;

    @Test
    public void mailSend()
    {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("123456@qq.com");
        msg.setTo("123456@qq.com");
        msg.setText("context");
        msg.setSubject("subject");
        mailSender.send(msg);
    }
}
