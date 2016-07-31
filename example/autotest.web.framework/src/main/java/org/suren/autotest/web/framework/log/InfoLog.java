/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author suren
 * @date Jul 30, 2016 10:09:16 PM
 */
@Aspect
public class InfoLog
{
	public InfoLog()
	{
		System.out.println("info log.");
	}
	
	@Pointcut("execution(* org.suren.autotest.web.framework..*(..))")
	public void test()
	{
		System.out.println("test");
	}
	
	public void hello(JoinPoint point)
	{
		System.out.println("hello");
	}
}
