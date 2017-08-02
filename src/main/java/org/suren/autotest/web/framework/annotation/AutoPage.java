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
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.settings.DriverConstants;

/**
 * 用于标记类为Page.
 * @author suren
 * @since 2017年6月7日 下午6:53:43
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Component
public @interface AutoPage
{
    /**
     * @return 关联配置路径
     */
    String path() default "";
    
    /**
     * @return 当前页面的url地址
     */
    String url() default "";
    
    /**
     * @return 浏览器类型
     */
    String browser() default DriverConstants.DRIVER_CHROME;
    
    /**
     * @return 远程地址
     */
    String remote() default "";
    
    /**
     * @return 是否为启动页面
     */
    boolean startPage() default false;
    
    /**
     * @return 是否最大化，默认将会最大化浏览器窗口
     */
    boolean maximize() default true;
    
    /**
     * @return 浏览器宽度
     */
    int width() default -1;
    
    /**
     * @return 浏览器高度
     */
    int height() default -1;
}
