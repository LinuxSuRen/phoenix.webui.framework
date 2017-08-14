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

package org.suren.autotest.web.framework.selenium;

import org.openqa.selenium.WebDriver;

/**
 * 通过该接口可以获取webdriver的实例
 * @author <a href="http://surenpi.com">suren</a>
 */
public interface WebDriverAware
{
    /**
     * 子类需要把该实例
     * @param webDriver selenium驱动对象
     */
    void setWebDriver(WebDriver webDriver);
}
