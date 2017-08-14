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

package org.suren.autotest.web.framework.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.surenpi.autotest.webui.core.StrategyType;

/**
 * 允许包含多个元素定位，并且指定一个定位策略.
 * @author <a href="http://surenpi.com">suren</a>
 * @since 2.0.0
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@AutoField
public @interface AutoLocators
{
	/**
	 * @return 普通定位集合
	 */
	AutoLocator[] locators() default {};
	
	/**
	 * @return 根据标签文本定位的集合
	 */
	AutoTextLocator[] textLocators() default {};
    
	/**
	 * @return 根据标签属性定位的集合
	 */
	AutoAttrLocator[] attrLocators() default {};
	
	/**
	 * 默认的定位策略为“优先级定位”
	 * @return 定位策略
	 */
	StrategyType strategy() default StrategyType.PRIORITY;
}
