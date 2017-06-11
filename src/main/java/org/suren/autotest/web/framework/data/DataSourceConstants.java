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

package org.suren.autotest.web.framework.data;

/**
 * 数据源常量
 * @author suren
 */
public interface DataSourceConstants
{
    /** xml格式 */
    String DS_TYPE_XML = "xml_data_source";
    /** yaml格式 */
    String DS_TYPE_YAML = "yaml_data_source";
    /** excel格式 */
    String DS_TYPE_EXCEL = "excel_data_source";

    String DS_NAME = null;
}
