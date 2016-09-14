/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.validation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

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
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		URL xsdURL = Validation.class.getClassLoader().getResource(xsdFile);
		if(xsdURL != null)
		{
			Schema schema = factory.newSchema(xsdURL);
			Validator validator = schema.newValidator();
			
			Source source = new StreamSource(xmlInput);
			validator.validate(source);
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
