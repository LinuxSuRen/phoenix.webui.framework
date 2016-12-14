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

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		new DefaultXmlCodeGenerator().generate("ad.xml", ".");
	}

	@Test
	public void testPage()
	{
		new DefaultXmlCodeGenerator().generate("test.xml", ".");
	}
}
