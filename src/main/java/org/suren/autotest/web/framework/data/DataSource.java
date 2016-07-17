/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import org.suren.autotest.web.framework.page.Page;

/**
 * @author suren
 * @date Jul 17, 2016 8:45:03 AM
 */
public interface DataSource
{
	boolean loadData(DataResource resource, Page page);
}
