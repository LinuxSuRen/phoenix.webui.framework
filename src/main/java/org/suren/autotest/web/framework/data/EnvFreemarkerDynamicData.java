/**
 * 
 */
package org.suren.autotest.web.framework.data;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.surenpi.autotest.datasource.DynamicData;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author surenpi
 *
 */
@Component
public class EnvFreemarkerDynamicData implements DynamicData
{

	private static final String TMP_NAME = "freemarker";
	
	@Override
	public String getType()
	{
		return "env-freemarker";
	}

	@Override
	public String getValue(String value)
	{
		StringTemplateLoader templateLoader = new StringTemplateLoader();
		templateLoader.putTemplate(TMP_NAME, value);
		
		Configuration configuration = new Configuration();
		configuration.setTemplateLoader(templateLoader);
		configuration.setObjectWrapper(new DefaultObjectWrapper()); 
		configuration.setDefaultEncoding("UTF-8");

		try
		{
			Template template = configuration.getTemplate(TMP_NAME);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Writer writer = new BufferedWriter(new OutputStreamWriter(out));
			
			Map<Object, Object> data = new HashMap<Object, Object>();
			data.putAll(System.getenv());
			data.putAll(System.getProperties());
			
			template.process(data, writer);
			
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
	public void setData(Map<String, Object> arg0)
	{
	}

}
