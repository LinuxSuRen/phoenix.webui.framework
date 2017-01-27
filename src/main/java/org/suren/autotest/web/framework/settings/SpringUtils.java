/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.settings;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author suren
 * @date 2017年1月23日 下午9:02:06
 */
@Component
public class SpringUtils implements ApplicationContextAware
{
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException
	{
		SpringUtils.applicationContext = applicationContext;
	}

	/**
	 * @return the applicationContext
	 */
	public static ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

}
