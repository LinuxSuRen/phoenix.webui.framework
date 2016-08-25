/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.locator;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 元素扩展属性定位器
 * @author suren
 * @date 2016年7月29日 下午2:59:24
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeleniumXAttrLocator extends AbstractSeleniumAttrLocator
{
	private String xAttr;

	@Override
	public String getType()
	{
		return "byXAttr";
	}

	@Override
	public String getAttrName()
	{
		return this.xAttr;
	}

	@Override
	public void setExtend(String extend)
	{
		this.xAttr = extend;
	}

}
