/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.os;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.suren.autotest.web.framework.util.OSUtil;

/**
 * @author suren
 * @since 2016年12月13日 下午9:49:49
 */
public class OSTest
{
	@Test
	public void test() throws MalformedURLException
	{
		System.out.println(OSUtil.is64Bit());
		
		String remoteHome = "http://surenpi.com/webdriver/chromedriver.2.9.248315_64.exe";
		File driverFile = new File("d:/test.exe");
		
		remoteHome = "file:///d:/ContactManager.apk";
		OutputStream output = null;
		URL url = new URL(remoteHome);
		System.out.println(url.getProtocol());
		try(InputStream inputStream = url.openStream())
		{
			output = new FileOutputStream(driverFile){

				@Override
				public void write(byte[] b, int off, int len)
						throws IOException
				{
					System.out.println("downloading size : " + len);
					super.write(b, off, len);
				}
			};
			IOUtils.copy(inputStream, output);
		}
		catch (IOException e)
		{
		}
		finally
		{
			IOUtils.closeQuietly(output);
		}
	}
}
