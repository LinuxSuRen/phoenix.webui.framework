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

import com.surenpi.autotest.datasource.DataSourceConstants;

/**
 * 数据源注解类
 * @author <a href="http://surenpi.com">suren</a>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@AutoPage
public @interface AutoDataSource {
    /**
     * 数据源名称，必须保证系统中唯一不重复
     * @return 数据源名称
     */
    String name() default "";

    /**
     * 数据源文件路径
     * @return 数据源文件
     */
    String resource();

    /**
     * 内置的类型包括：xml、yaml、excel
     * @see DataSourceConstants
     * @return 数据源类型
     */
    String type() default DataSourceConstants.DS_TYPE_XML;
}
