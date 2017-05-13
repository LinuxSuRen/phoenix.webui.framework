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

package org.suren.autotest.web.framework.validation;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 把和本框架没关系的异常信息移出
 * @author suren
 * @date 2016年9月14日 下午9:57:09
 */
public class AutoErrorHandler implements ErrorHandler
{

	@Override
	public void warning(SAXParseException exception) throws SAXException
	{
		System.out.println("sd");
	}

	@Override
	public void error(SAXParseException exception) throws SAXException
	{
		StackTraceElement[] stackTrace = exception.getStackTrace();
		List<StackTraceElement> stackTraceList = new ArrayList<StackTraceElement>();
		for(StackTraceElement element : stackTrace)
		{
			if(element.getClassName().startsWith("org.suren"))
			{
				stackTraceList.add(element);
			}
		}
		
		stackTrace = stackTraceList.toArray(new StackTraceElement[stackTraceList.size()]);
		exception.setStackTrace(stackTrace);
		exception.printStackTrace();
		
		throw exception;
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException
	{
		System.out.println("sd");
	}

}
