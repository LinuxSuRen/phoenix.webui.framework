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

package org.suren.autotest.web.framework.selenium.locator;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 元素扩展属性定位器
 * @author suren
 * @since 2016年7月29日 下午2:59:24
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeleniumXAttrLocator extends AbstractSeleniumAttrLocator
{
	private String xAttr;

	@Override
	public String getType()
	{
		return "byXAttr";
	}

	@Override
	public String getAttrName()
	{
		return this.xAttr;
	}

	@Override
	public void setExtend(String extend)
	{
		this.xAttr = extend;
	}

}
