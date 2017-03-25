/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import java.util.Map;

/**
 * @author suren
 * @date 2017年3月24日 下午10:20:06
 */
public interface DynamicDataSource
{
	
	/**
	 * 动态参数
	 * @param globalMap
	 */
	void setGlobalMap(Map<String, Object> globalMap);
	
	/**
	 * 动态参数
	 * @return
	 */
	Map<String, Object> getGlobalMap();
	
	/**
	 * @return 唯一标识数据源的名称
	 */
	String getName();
}
