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

import com.surenpi.autotest.webui.core.Locator;

/**
 * 根据标签属性来定位。例如下面的标签：
 * <pre>{@code
 * <button data="simple" data-color="red">按钮</button>
 * }</pre>
 * 标签使用方法为：
 * <pre>
 * &#064;AutoAttrLocator(tagName = "button", name = "data-color", value = "red")
 * private Button But;
 * </pre>
 * 或者：
 * <pre>
 * &#064;AutoAttrLocator(tagName = "button", name = "data", value = "simple")
 * private Button But;
 * </pre>
 * @author <a href="http://surenpi.com">suren</a>
 * @since Aug 10, 2017 10:11:10 PM
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@AutoField
public @interface AutoAttrLocator 
{
    /**
     * 例如下面的标签名称为button
     * <pre>{@code
     * <button data="simple" data-color="red">按钮</button>
     * }</pre>
     * @return 标签名称
     */
    String tagName() default "input";
    
    /**
     * 例如下面标签的属性名称有：data、
     * <pre>{@code
     * <button data="simple" data-color="red">按钮</button>
     * }</pre>
     * @return 属性名称
     */
    String name();
    
    /**
     * 例如下面标签属性data-color的值为red
     * <pre>{@code
     * <button data="simple" data-color="red">按钮</button>
     * }</pre>
     * @return 属性值
     */
    String value();
    
    /**
     * @return 条件
     */
    int condition() default Locator.EQUAL;
    
    /**
     * @return 显式的查找超时时间（单位：毫秒）
     */
    long timeout() default 0;

    /**
     * @return 定位方法的优先级，数字越大优先级越高
     */
    int order() default 0;
}
