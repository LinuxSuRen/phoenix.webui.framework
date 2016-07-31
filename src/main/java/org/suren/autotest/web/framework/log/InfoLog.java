/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author suren
 * @date Jul 30, 2016 10:09:16 PM
 */
@Component
@Aspect
public class InfoLog
{
	private static final Logger logger = LoggerFactory.getLogger(InfoLog.class);
	
//	@Before("execution(* org.suren.autotest.web.framework.core.ElementSearchStrategy.search*(..))")
	public void before()
	{
		System.out.println("hell");
	}
	
	@Around("execution(* org.suren.autotest.web.framework.core.ElementSearchStrategy.search*(..))")
	public Object hello(ProceedingJoinPoint joinPoint)
	{
		long beginTime = System.currentTimeMillis();
		
		Object[] args = joinPoint.getArgs();
		
		Object res = null;
		try
		{
			res = joinPoint.proceed(args);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		
		long endTime = System.currentTimeMillis();
		
		logger.debug(String.format("target [%s] time [%s].",
				joinPoint.getSignature(), endTime - beginTime));
		
		return res;
	}
}
