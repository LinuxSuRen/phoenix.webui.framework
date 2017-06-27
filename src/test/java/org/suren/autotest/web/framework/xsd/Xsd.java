/*
 *
 *  * Copyright 2002-2007 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.suren.autotest.web.framework.xsd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaObject;
import org.junit.Assert;
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
			Assert.assertNotNull(input);
			
			XmlSchemaCollection schemaCol = new XmlSchemaCollection();
			XmlSchema schema = schemaCol.read(new StreamSource(input));
			
			System.out.println(schema.getClass());
			
			XmlSchemaElement root = schema.getElementByName("autotest");
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
