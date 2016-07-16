package org.suren.autotest.web.framework.selenium.strategy;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ElementSearchStrategy;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

/**
 * 查找元素策略，找不到对应的元素会抛出异常</br>
 * <ul>
 * <li>通过id查找</li>
 * <li>通过css样式查找</li>
 * <li>通过xpath查找</li>
 * <li>通过超链接文本查找</li>
 * <li>通过超链接部分文本查找</li>
 * <li>根据标签名称来查找</li>
 * </ul>
 * @see CyleSearchStrategy
 * @see ZoneSearchStrategy
 * @author suren
 * @date Jul 16, 2016 6:45:44 PM
 */
@Component
public class PrioritySearchStrategy implements ElementSearchStrategy<WebElement> {

	@Autowired
	private SeleniumEngine engine;
	
	public WebElement search(Element element) {
		By by = null;
		
		if(StringUtils.isNotBlank(element.getId())) {
			by = By.id(element.getId());
		} else if(StringUtils.isNoneBlank(element.getCSS())) {
			by = By.className(element.getCSS());
		} else if(StringUtils.isNotBlank(element.getXPath())) {
			by = By.xpath(element.getXPath());
		} else if(StringUtils.isNotBlank(element.getLinkText())) {
			by = By.linkText(element.getLinkText());
		} else if(StringUtils.isNoneBlank(element.getPartialLinkText())) {
			by = By.partialLinkText(element.getPartialLinkText());
		} else if(StringUtils.isNotBlank(element.getTagName())) {
			by = By.tagName(element.getTagName());
		}
		
		return findElement(by);
	}

	private WebElement findElement(By by) {
		return engine.getDriver().findElement(by);
	}
}