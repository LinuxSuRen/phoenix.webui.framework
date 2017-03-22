/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.action;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.LocatorUtil;
import org.suren.autotest.web.framework.core.action.SequenceAble;
import org.suren.autotest.web.framework.core.ui.AbstractElement;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.core.ui.Text;

/**
 * jqGrid组件的选择
 * @author suren
 * @date 2017年1月16日 上午9:54:37
 */
@Component
public class SeleniumJqGridSequenceOperation implements SequenceAble
{
	private static final Logger logger = LoggerFactory.getLogger(SeleniumJqGridSequenceOperation.class);
	
	/** 关键字搜索定位方法 */
	public static final String KW_SEARCH_BY = "keyword_search_by";
	public static final String KW_SEARCH_INFO = "keyword_search_info";
	public static final String SEARCH_BUT_BY = "search_button_by";
	public static final String SEARCH_BUT_INFO = "search_button_info";
	public static final String OPER_BUT_BY = "operation_button_by";
	public static final String OPER_BUT_INFO = "operation_button_info";
	
//	@Autowired
	private Text searchText;
//	@Autowired
	private Button searchBut;
//	@Autowired
	private Button operationBut;

	@Override
	public void perform(Element element, List<String> actions)
	{
		if(!(element instanceof AbstractElement))
		{
			return;
		}
		
		AbstractElement absEle = ((AbstractElement) element);
		String keyword = ((Text) element).getValue();
		
		buildSearchText(absEle);
		searchText.fillValue(keyword);
		
		buildSearchButton(absEle);
		searchBut.click();
		
		buildOperationBut(absEle);
		operationBut.click();
	}

	/**
	 * @param absEle
	 * @return
	 */
	private Text buildSearchText(AbstractElement absEle)
	{
		Map<String, String> map = Collections.singletonMap(
				absEle.getDataStr(KW_SEARCH_BY), absEle.getDataStr(KW_SEARCH_INFO));
		map.put("strategy", "priority");
		
		LocatorUtil.setLocator(map, searchText);
		
		return searchText;
	}

	/**
	 * @param absEle
	 */
	private Button buildSearchButton(AbstractElement absEle)
	{
		Map<String, String> map = Collections.singletonMap(
				absEle.getDataStr(SEARCH_BUT_BY), absEle.getDataStr(SEARCH_BUT_INFO));
		map.put("strategy", "priority");
		
		LocatorUtil.setLocator(map, searchBut);
		
		return searchBut;
	}

	/**
	 * @param absEle
	 */
	private Button buildOperationBut(AbstractElement absEle)
	{
		Map<String, String> map = Collections.singletonMap(
				absEle.getDataStr(OPER_BUT_BY), absEle.getDataStr(OPER_BUT_INFO));
		map.put("strategy", "priority");
		
		LocatorUtil.setLocator(map, operationBut);
		
		return operationBut;
	}

	@Override
	public String getName()
	{
		return "jqgrid";
	}

	@Override
	public String getDescription()
	{
		return "基于jqgrid的连续操作";
	}

}
