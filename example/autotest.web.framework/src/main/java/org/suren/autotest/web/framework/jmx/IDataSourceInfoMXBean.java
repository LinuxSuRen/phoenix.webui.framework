/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.jmx;

import java.util.List;
import java.util.Map;

import org.suren.autotest.web.framework.data.DataSource;

/**
 * @author suren
 * @date 2016年7月21日 下午7:17:30
 */
public interface IDataSourceInfoMXBean
{
	int getTotalCount();
	
	List<DataSource> getDataSourceList();
	
	void setDataSourceMap(Map<String, DataSource> dataSourceMap);
}
