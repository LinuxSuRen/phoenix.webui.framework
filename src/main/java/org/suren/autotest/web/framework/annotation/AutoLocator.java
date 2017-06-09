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

package org.suren.autotest.web.framework.annotation;

import org.suren.autotest.web.framework.core.LocatorType;

import java.lang.annotation.*;

/**
 * 元素定位符
 * @author suren
 * @date 2017年6月7日 下午6:58:44
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AutoField
public @interface AutoLocator
{
	/**
	 * @return 定位方法
	 */
	LocatorType locator();
	
	/**
	 * @return 定位的具体内容
	 */
	String value();
	
	/**
	 * @return 显式的查找超时时间（单位：毫秒）
	 */
	long timeout() default 0;
}