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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.suren.autotest.webdriver.downloader.PathUtil;
import org.xml.sax.SAXException;

/**
 * xsd验证工具类
 * @author suren
 * @date 2016年7月19日 下午7:05:12
 */
public class Validation
{
	private Validation(){}
	
	/**
	 * 利用xsd验证xml
	 * @param xsdFile
	 * @param xmlInput
	 * @throws SAXException 
	 * @throws IOException 
	 */
	public static void validation(String xsdFile, InputStream xmlInput) throws SAXException, IOException
	{
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		URL xsdURL = Validation.class.getClassLoader().getResource(xsdFile);
		if(xsdURL != null)
		{
			Schema schema = factory.newSchema(xsdURL);
			Validator validator = schema.newValidator();
//			validator.setErrorHandler(new AutoErrorHandler());

			Source source = new StreamSource(xmlInput);
			
			try(OutputStream resultOut = new FileOutputStream(new File(PathUtil.getRootDir(), xsdFile + ".xml")))
			{
				Result result = new StreamResult(resultOut);
				validator.validate(source, result);
			}
		}
		else
		{
			throw new FileNotFoundException(String.format("can not found xsd file [%s] from classpath.", xsdFile));
		}
	}
	
	/**
	 * 对框架的xml进行验证
	 * @param xmlInput
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static void validationFramework(InputStream xmlInput) throws SAXException, IOException
	{
		validation("autotest.web.framework.xsd", xmlInput);
	}
	
	/**
	 * 对数据源的xml进行验证
	 * @param dataSourceInput
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static void validationDataSource(InputStream dataSourceInput) throws SAXException, IOException
	{
		validation("autotest.web.framework.datasource.xsd", dataSourceInput);
	}
	
	/**
	 * 验证测试套件的配置文件
	 * @param suiteInput
	 * @throws IOException 
	 * @throws SAXException 

	 */
	public static void validationSuite(InputStream suiteInput) throws SAXException, IOException
	{
		validation("autotest.web.framework.suite.xsd", suiteInput);
	}

}
