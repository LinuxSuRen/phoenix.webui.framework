/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.strategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ElementSearchStrategy;
import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 元素查找策略规则
 * @author suren
 * @date Jul 17, 2016 8:04:51 AM
 */
@Component
public class SearchStrategyUtils implements ApplicationContextAware
{
	private ApplicationContext context;
	private Map<String, String> strategyMap = new HashMap<String, String>();
	
	public SearchStrategyUtils()
	{
		strategyMap.put("priority", "prioritySearchStrategy");
		strategyMap.put("cyle", "cyleSearchStrategy");
		strategyMap.put("zone", "zoneSearchStrategy");
	}
	
	@SuppressWarnings("unchecked")
	public <T> ElementSearchStrategy<T> findStrategy(Class<T> type, Element element)
	{
		String strategy = element.getStrategy();
		strategy = StringUtils.isBlank(strategy) ? "prioritySearchStrategy" : strategyMap.get(strategy);
		
		return (ElementSearchStrategy<T>) context.getBean(strategy, ElementSearchStrategy.class);
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException
	{
		this.context = context;
	}
	
	/**
	 * @return 所有的策略
	 */
	public Set<String> getAllStrategy()
	{
		return strategyMap.keySet();
	}
}
