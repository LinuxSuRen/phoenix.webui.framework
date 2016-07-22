/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.jmx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.data.DataSource;

/**
 * @author suren
 * @date 2016年7月21日 下午7:18:42
 */
@Component
public class DataSourceInfoManager implements IDataSourceInfoMXBean
{

	private Map<String, DataSource> dataSourceMap;
	
	@Override
	public int getTotalCount()
	{
		return dataSourceMap.size();
	}

	@Override
	public List<DataSource> getDataSourceList()
	{
		return new ArrayList<DataSource>(dataSourceMap.values());
	}

}
