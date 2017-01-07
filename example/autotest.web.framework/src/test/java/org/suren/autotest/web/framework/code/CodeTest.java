/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.code;

import org.junit.Test;

/**
 * @author suren
 * @date 2016年12月3日 下午8:59:07
 */
public class CodeTest
{
	@Test
	public void testPage()
	{
		new DefaultXmlCodeGenerator().generate("ad.xml", "target");
		new DefaultXmlCodeGenerator().generate("test.xml", "target");
	}
}
