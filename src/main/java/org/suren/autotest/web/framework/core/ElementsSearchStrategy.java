/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core;

import java.util.List;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * @author suren
 * @date 2016年12月30日 下午7:16:19
 */
public interface ElementsSearchStrategy<T>
{
	List<T> searchAll(Element element);
}
