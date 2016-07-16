/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.action;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * @author suren
 * @date Jul 16, 2016 7:58:48 PM
 */
public interface Status
{
	boolean isEnabled(Element element);
	
	boolean isHidden(Element element);
}
