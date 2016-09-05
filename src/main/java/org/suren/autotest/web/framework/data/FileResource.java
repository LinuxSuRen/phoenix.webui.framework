/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 文件类型的资源
 * @author suren
 * @date Jul 17, 2016 8:49:18 AM
 */
public class FileResource implements DataResource
{
	private File file;
	
	public FileResource(File file)
	{
		this.file = file;
	}

	@Override
	public URL getUrl() throws MalformedURLException
	{
		return file.toURI().toURL();
	}

}
