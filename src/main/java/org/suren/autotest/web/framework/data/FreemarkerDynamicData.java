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

package org.suren.autotest.web.framework.data;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.surenpi.autotest.datasource.DynamicData;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author suren
 * @date 2017年3月25日 上午9:28:47
 */
@Component
public class FreemarkerDynamicData implements DynamicData
{
	private Map<String, Object> globalData;
	
	private static final String TMP_NAME = "freemarker";
	
	@Override
	public String getValue(String orginData)
	{
		StringTemplateLoader templateLoader = new StringTemplateLoader();
		templateLoader.putTemplate(TMP_NAME, orginData);
		
		Configuration configuration = new Configuration();
		configuration.setTemplateLoader(templateLoader);
		configuration.setObjectWrapper(new DefaultObjectWrapper()); 
		configuration.setDefaultEncoding("UTF-8");

		try
		{
			Template template = configuration.getTemplate(TMP_NAME);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Writer writer = new BufferedWriter(new OutputStreamWriter(out)) ;
			template.process(globalData, writer);
			
			return out.toString("utf-8");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (TemplateException e)
		{
			e.printStackTrace();
		}
	
		return null;
	}

	@Override
	public String getType()
	{
		return TMP_NAME;
	}

	@Override
	public void setData(Map<String, Object> data)
	{
		this.globalData = data;
	}

}
