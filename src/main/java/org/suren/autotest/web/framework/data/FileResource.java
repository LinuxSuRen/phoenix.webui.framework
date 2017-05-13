/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
