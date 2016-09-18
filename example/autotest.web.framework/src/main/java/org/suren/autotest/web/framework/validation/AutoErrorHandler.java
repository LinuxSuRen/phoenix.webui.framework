/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.validation;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
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
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException
	{
		System.out.println("sd");
	}

}
