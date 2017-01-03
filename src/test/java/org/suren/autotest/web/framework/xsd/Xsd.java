/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.xsd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.apache.ws.commons.schema.XmlSchemaComplexType;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaObject;
import org.eclipse.core.runtime.Assert;
import org.junit.Test;

/**
 * @author suren
 * @date 2017年1月2日 下午8:25:39
 */
public class Xsd
{
	@Test
	public void test()
	{
		try(InputStream input = Xsd.class.getResourceAsStream("/autotest.web.framework.xsd"))
		{
			Assert.isNotNull(input);
			
			XmlSchemaCollection schemaCol = new XmlSchemaCollection();
			XmlSchema schema = schemaCol.read(new StreamSource(input));
			
			System.out.println(schema.getClass());
			
			XmlSchemaElement root = schema.getElementByName("autotest");
			System.out.println(root);
			List<XmlSchemaObject> items = schema.getItems();
			for(XmlSchemaObject item : items)
			{
//				System.out.println(item.getClass());
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
