/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;

import java.io.InputStream;
import java.net.URL;

/**
 * @author suren
 * @date 2016年9月24日 下午3:51:14
 */
public class GroovyTest
{
	public static void main(String[] args) throws Exception
	{
		Binding binding = new Binding();
		binding.setVariable("language", "Groovy");
		
		GroovyShell shell = new GroovyShell(binding);
		
		GroovyScriptEngine engine = new GroovyScriptEngine(new URL[]{GroovyTest.class.getClassLoader().getResource("/")});
		Script script = shell.parse(GroovyTest.class.getClassLoader().getResource("random.groovy").toURI());
		
//		System.out.println(script);
//		script.invokeMethod("new SuRenRandom()", null);
//		script.evaluate("new SuRenRandom()");
//		engine.run("random.groovy", binding);
		
		InputStream stream = GroovyTest.class.getClassLoader().getResource("random.groovy").openStream();
		StringBuffer buf = new StringBuffer();
		byte[] bf = new byte[1024];
		int len = -1;
		while((len = stream.read(bf)) != -1)
		{
			buf.append(new String(bf, 0, len));
		}
		buf.append("\n");
		
		for(int i = 0; i < 30; i++)
		{
			System.out.println(shell.evaluate(buf.toString() + "new SuRenRandom().randomPhoneNum()"));
			System.out.println(shell.evaluate(buf.toString() + "new SuRenRandom().randomEmail()"));
			System.out.println(shell.evaluate(buf.toString() + "new SuRenRandom().randomZipCode()"));
		}
	}
}
