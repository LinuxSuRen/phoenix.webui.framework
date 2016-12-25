/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.code;

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
	public void generate(String srcCoding, String outputDir)
	{
		//执行以后刷新当前工程就能看到生成的Page文件
		Generator generator = new DefaultXmlCodeGenerator();
		generator.generate(srcCoding, outputDir); //这里从类路径下读取

		generator = new DefaultXmlDataSourceGenerator();
		generator.generate(srcCoding, outputDir); //这里从类路径下读取

		generator = new DefaultXmlSuiteRunnerGenerator();
		generator.generate(srcCoding, outputDir); //这里从类路径下读取}
	}

}
