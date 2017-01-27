/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.AbstractElement;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.Page;
import org.suren.autotest.web.framework.util.StringUtils;

/**
 * Page对象之间的属性数据引用
 * @author suren
 * @date 2017年1月11日 上午11:36:21
 */
@Component
public class PageRefDynamicData implements DynamicData, ApplicationContextAware
{
	private static final Logger LOGGER = LoggerFactory.getLogger(PageRefDynamicData.class);
	
	private ApplicationContext context;
	
	@Override
	public String getValue(String orginData)
	{
		if(StringUtils.isBlank(orginData))
		{
			return null;
		}
		
		String[] orginArrayData = orginData.split("\\.", 2);
		
		String clsName = orginArrayData[0];
		String fieldName = orginArrayData[1];
		
		clsName = StringUtils.uncapitalize(clsName); //spring的bean名称是首字母小写的规则
		
		Page page = null;
		
		try
		{
			page = context.getBean(clsName, Page.class);
		}
		catch(NoSuchBeanDefinitionException e)
		{
			LOGGER.error("Can not found page class by name [{}].", clsName);
		}
		catch(BeanNotOfRequiredTypeException e)
		{
			LOGGER.error("The class [{}] is not a Page type.", clsName);
		}
		
		if(page == null)
		{
			throw new RuntimeException("Can not found page class!");
		}

		try
		{
			Method readMethod = new PropertyDescriptor(
					fieldName, page.getClass()).getReadMethod();
			
			Object result = readMethod.invoke(page);
			if(result != null && AbstractElement.class.isAssignableFrom(result.getClass()))
			{
				if(result instanceof Text)
				{
					return ((Text) result).getValue();
				}
				else
				{
					throw new RuntimeException("Not support field type [" + result.getClass() + "].");
				}
			}
		}
		catch (IntrospectionException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String getType()
	{
		return "page_ref";
	}

	@Override
	public void setData(Map<String, Object> data)
	{
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException
	{
		this.context = applicationContext;
	}
}
