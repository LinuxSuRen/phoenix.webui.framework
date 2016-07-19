/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.core.action;

import java.io.File;
import java.net.URL;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * @author zhaoxj
 * @since jdk1.6
 * @since 3.1.1-SNAPSHOT
 * 2016年7月19日
 */
public interface FileUploadAble extends Status
{
	boolean upload(Element element, URL url);
	
	boolean upload(Element element, File file);
	
	boolean click(Element element);
}
