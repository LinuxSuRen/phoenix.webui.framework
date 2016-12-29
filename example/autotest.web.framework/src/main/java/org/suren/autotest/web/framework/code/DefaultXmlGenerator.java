/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.xpath.DefaultXPath;
import org.jaxen.SimpleNamespaceContext;

/**
 * 生成xml格式所有的文件（Page类、数据源、测试套件）
 * @author suren
 * @date 2016年12月25日 下午7:44:36
 * @see DefaultXmlCodeGenerator
 * @see DefaultXmlDataSourceGenerator
 * @see DefaultXmlSuiteRunnerGenerator
 */
public class DefaultXmlGenerator implements Generator
{

	@Override
	public void generate(String srcCoding, final String outputDir)
	{
		//执行以后刷新当前工程就能看到生成的Page文件
		Generator generator = new DefaultXmlCodeGenerator(){

			private String springConfigPath = "applicationContext.xml";
			private Set<String> allPackage = new HashSet<String>();
			
			@Override
			protected void create(AutoPage autoPage)
			{
				super.create(autoPage);
				
				allPackage.add(autoPage.getPackageName());
			}

			@Override
			protected void done()
			{
				super.done();
				
				if(allPackage.size() == 0)
				{
					return;
				}
				
				Document springConfigDoc = null;
				
				try(InputStream inputStream = getClassLoader().getResourceAsStream(springConfigPath))
				{
					if(inputStream != null)
					{
						springConfigDoc = new SAXReader().read(inputStream);
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				catch (DocumentException e)
				{
					e.printStackTrace();
				}
				
				if(springConfigDoc == null)
				{
					springConfigDoc = DocumentHelper.createDocument();

					String prefix = "p";
					Element root = springConfigDoc.addElement(prefix + ":beans");
					
					root.addNamespace(prefix, "http://www.springframework.org/schema/beans");
					root.addNamespace("context", "http://www.springframework.org/schema/context");
					root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
					root.addAttribute("xsi:schemaLocation", "http://www.springframework.org/schema/beans "
							+ "http://www.springframework.org/schema/beans/spring-beans-3.0.xsd "
							+ "http://www.springframework.org/schema/context "
							+ "http://www.springframework.org/schema/context/spring-context-3.0.xsd  ");
				}
				
				SimpleNamespaceContext simpleNamespaceContext = new SimpleNamespaceContext();
				simpleNamespaceContext.addNamespace("p", "http://www.springframework.org/schema/beans");
				simpleNamespaceContext.addNamespace("context", "http://www.springframework.org/schema/context");
				
				XPath xpath = new DefaultXPath("/p:beans/context:component-scan");
				xpath.setNamespaceContext(simpleNamespaceContext);
				
				//先查找是否有该标签
				Element componentScanEle = (Element) xpath.selectSingleNode(springConfigDoc);
				if(componentScanEle == null)
				{
					componentScanEle = springConfigDoc.getRootElement().addElement("context:component-scan");
				}
				
				//把page类所在的package加入配置
				String basePackage = componentScanEle.attributeValue("base-package", "");
				basePackage = basePackage.trim();
				if(!"".equals(basePackage) && !basePackage.endsWith(","))
				{
					basePackage += ",";
				}
				
				basePackage.replaceAll(",\\s*", ",");
				for(String newPackage : allPackage)
				{
					if(!basePackage.contains(newPackage + ","))
					{
						basePackage += (newPackage + ",");
					}
				}

				componentScanEle.addAttribute("base-package", basePackage);
				
				//保存修改后的数据
				File outputDirFile = new File(outputDir);
				if(!outputDirFile.isDirectory())
				{
					outputDirFile.mkdirs();
				}
				
				try(OutputStream dsOutput = new FileOutputStream(new File(outputDirFile, springConfigPath)))
				{
					OutputFormat outputFormat = OutputFormat.createPrettyPrint();
					outputFormat.setIndentSize(4);
					XMLWriter xmlWriter = new XMLWriter(dsOutput, outputFormat);
					
					xmlWriter.write(springConfigDoc);
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
		};
		generator.generate(srcCoding, outputDir); //这里从类路径下读取

		generator = new DefaultXmlDataSourceGenerator();
		generator.generate(srcCoding, outputDir); //这里从类路径下读取

		generator = new DefaultXmlSuiteRunnerGenerator();
		generator.generate(srcCoding, outputDir); //这里从类路径下读取}
	}
}
