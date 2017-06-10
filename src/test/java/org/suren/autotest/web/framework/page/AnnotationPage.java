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

package org.suren.autotest.web.framework.page;

import org.suren.autotest.web.framework.annotation.AutoDataSource;
import org.suren.autotest.web.framework.annotation.AutoLocator;
import org.suren.autotest.web.framework.annotation.AutoPage;
import org.suren.autotest.web.framework.annotation.AutoStrategy;
import org.suren.autotest.web.framework.core.LocatorType;
import org.suren.autotest.web.framework.core.StrategyType;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Text;

/**
 * 使用注解的示例Page类
 * @author suren
 * @date 2017年6月7日 下午7:10:40
 */
@AutoPage(url = "http://maimai.cn/")
@AutoDataSource(name = "data", resource = "dataSource/xml/user_data_anno.xml")
public class AnnotationPage extends Page
{
	@AutoStrategy(type = StrategyType.PRIORITY)
	@AutoLocator(locator = LocatorType.BY_PARTIAL_LINK_TEXT, value = "实名动态")
	private Button toLoginBut;

	@AutoLocator(locator = LocatorType.BY_XPATH, value = "//input[@placeholder='请输入手机号码/脉脉号']")
	private Text phoneText;

	public Button getToLoginBut() {
		return toLoginBut;
	}

	public void setToLoginBut(Button toLoginBut) {
		this.toLoginBut = toLoginBut;
	}

	public Text getPhoneText() {
		return phoneText;
	}

	public void setPhoneText(Text phoneText) {
		this.phoneText = phoneText;
	}
}
