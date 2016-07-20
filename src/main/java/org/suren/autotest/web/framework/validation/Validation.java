/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.validation;

import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 * @author suren
 * @date 2016年7月19日 下午7:05:12
 */
public class Validation
{

	public static void validation(String xsdFile, InputStream xmlInput) throws Exception
	{
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema = factory.newSchema(Validation.class.getClassLoader().getResource(xsdFile));
		Validator validator = schema.newValidator();
		
		Source source = new StreamSource(xmlInput);
		validator.validate(source);
	}
	
	public static void validationFramework(InputStream xmlInput) throws Exception
	{
		validation("autotest.web.framework.xsd", xmlInput);
	}
	
	public static void validationDataSource(InputStream dataSourceInput) throws Exception
	{
		validation("autotest.web.framework.datasource.xsd", dataSourceInput);
	}

}
