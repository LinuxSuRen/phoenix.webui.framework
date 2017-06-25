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

import org.suren.autotest.web.framework.page.Page;

import java.lang.annotation.*;

/**
 * 用在登录方法上，第一次登录时，保存session信息；之后登录方法被调用时则会跳过
 * @author suren
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoSessionStorage
{
    /**
     * @return 用户登录Page类
     */
    Class<? extends Page> accountPage();

    /**
     * @return Page类中代表用户名的属性名称
     */
    String accountName() default "userName";
}
