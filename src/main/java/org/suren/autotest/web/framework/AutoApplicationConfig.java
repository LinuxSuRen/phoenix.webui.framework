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

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.suren.autotest.web.framework.log.Image4SearchLog;
import org.suren.autotest.web.framework.mail.MailConfig;

/**
 * Spring零配置
 * @author suren
 * @date 2017年6月8日 上午8:21:10
 */
@Configuration
@ComponentScan
@ImportResource({"autoTestContext.xml", "beanScope.xml"})
@Import({
	Image4SearchLog.class,
	MailConfig.class
})
public class AutoApplicationConfig
{
}
