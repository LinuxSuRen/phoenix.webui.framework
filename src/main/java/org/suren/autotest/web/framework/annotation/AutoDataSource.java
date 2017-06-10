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

import java.lang.annotation.*;

/**
 * 数据源注解类
 * @author suren
 * @date 2017年6月10日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AutoPage
public @interface AutoDataSource {
    /**
     * @return 数据呀un名称
     */
    String name();

    /**
     * @return 数据源文件
     */
    String resource();

    /**
     * 内置的类型包括：xml、yaml、excel
     * @return 数据源类型
     */
    String type() default "xml_data_source";
}
