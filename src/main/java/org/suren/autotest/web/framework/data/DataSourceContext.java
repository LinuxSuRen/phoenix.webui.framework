/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import java.util.Map;

/**
 * @author suren
 * @date 2016年7月22日 下午5:43:05
 */
public class DataSourceContext
{
	private Map<String, DataSource>	dataSourceMap;

	/**
	 * @return the dataSourceMap
	 */
	public Map<String, DataSource> getDataSourceMap()
	{
		return dataSourceMap;
	}

	/**
	 * @param dataSourceMap the dataSourceMap to set
	 */
	public void setDataSourceMap(Map<String, DataSource> dataSourceMap)
	{
		this.dataSourceMap = dataSourceMap;
	}
}
