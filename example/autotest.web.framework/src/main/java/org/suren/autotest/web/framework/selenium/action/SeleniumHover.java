/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.action;

import java.awt.AWTException;
import java.awt.Robot;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.HoverAble;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.core.ui.FileUpload;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.selenium.strategy.SearchStrategyUtils;

/**
 * @author suren
 * @date 2016年10月14日 下午3:39:35
 */
@Component
public class SeleniumHover implements HoverAble
{
	private static final Logger logger = LoggerFactory.getLogger(SeleniumHover.class);
	
	@Autowired
	private SeleniumEngine			engine;
	@Autowired
	private SearchStrategyUtils		searchStrategyUtils;

	@Override
	public void hover(Element ele)
	{
		WebElement webEle = searchStrategyUtils.findStrategy(WebElement.class, ele).search(ele);
		if(webEle == null)
		{
			logger.warn("can not found element.");
			return;
		}
		
		if(!(ele instanceof FileUpload))
		{
			Dimension size = webEle.getSize();
			Point loc = webEle.getLocation();
			int toolbarHeight = engine.getToolbarHeight();
			int x = size.getWidth() / 2 + loc.getX();
			int y = size.getHeight() / 2 + loc.getY() + toolbarHeight;
			
			try
			{
				new Robot().mouseMove(x, y);
			}
			catch (AWTException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void hover(Element ele, long timeout)
	{
		// TODO Auto-generated method stub

	}

	public SeleniumEngine getEngine()
	{
		return engine;
	}

	public void setEngine(SeleniumEngine engine)
	{
		this.engine = engine;
	}

	public SearchStrategyUtils getSearchStrategyUtils()
	{
		return searchStrategyUtils;
	}

	public void setSearchStrategyUtils(SearchStrategyUtils searchStrategyUtils)
	{
		this.searchStrategyUtils = searchStrategyUtils;
	}

}