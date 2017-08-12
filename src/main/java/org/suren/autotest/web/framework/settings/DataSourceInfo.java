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

package org.suren.autotest.web.framework.settings;

/**
 * @author suren
 * @date Aug 12, 2017 8:55:59 PM
 */
class DataSourceInfo
{
    private String type;
    private String resource;
    public DataSourceInfo(String type, String resource)
    {
        this.type = type;
        this.resource = resource;
    }
    public String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        this.type = type;
    }
    public String getResource()
    {
        return resource;
    }
    public void setResource(String resource)
    {
        this.resource = resource;
    }
}
