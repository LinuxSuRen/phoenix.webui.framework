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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.surenpi.autotest.webui.core.LocatorType;

/**
 * 元素定位符
 * @author suren
 * @since 2017年6月7日 下午6:58:44
 */
@Target(value = {
        ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AutoField
public @interface AutoLocator
{
	/**
	 * 默认为根据标签名称定位
	 * @return 定位方法
	 */
	LocatorType locator() default LocatorType.BY_NAME;
	
	/**
	 * @return 定位的具体内容
	 */
	String value();
	
	/**
	 * @return 扩展字段
	 */
	String extend() default "";
	
	/**
	 * 当通过当期定位方法可以定位到，并且元素是可见的超时时间
	 * @return 显式的查找超时时间（单位：毫秒）
	 */
	long timeout() default 0;
	
	/**
	 * @return 定位方法的优先级
	 */
	int order() default 0;
}