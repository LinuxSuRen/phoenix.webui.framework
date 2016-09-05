/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import java.io.IOException;
import java.net.URL;

/**
 * 数据资源定义顶层接口
 * @author suren
 * @date Jul 17, 2016 8:47:30 AM
 */
public interface DataResource
{
	/**
	 * @return 资源所在的地址
	 * @throws IOException
	 */
	URL getUrl() throws IOException;
}
