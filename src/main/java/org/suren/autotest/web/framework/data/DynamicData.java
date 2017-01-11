/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import java.util.Map;

/**
 * 动态数据
 * @author suren
 * @date 2017年1月4日 下午12:30:53
 */
public interface DynamicData
{
	String getValue(String orginData);
	
	String getType();
	
	void setData(Map<String, Object> data);
}
