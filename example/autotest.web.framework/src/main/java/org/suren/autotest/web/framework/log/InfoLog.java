/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 有关元素查找所需时间的日志输出
 * @author suren
 * @date Jul 30, 2016 10:09:16 PM
 */
@Component
@Aspect
public class InfoLog
{
	private static final Logger LOGGER = LoggerFactory.getLogger(InfoLog.class);
	
	@Around("execution(* org.suren.autotest.web.framework.core.action.ClickAble.click*(..))")
	public Object clickAround(ProceedingJoinPoint joinPoint) throws Throwable
	{
		Object[] args = joinPoint.getArgs();
		
		return joinPoint.proceed(args);
	}
	
	@Around("execution(* org.suren.autotest.web.framework.core.ElementSearchStrategy.search*(..))")
	public Object hello(ProceedingJoinPoint joinPoint) throws Throwable
	{
		long beginTime = System.currentTimeMillis();
		
		Object[] args = joinPoint.getArgs();
		
		Object res = joinPoint.proceed(args);
		
		long endTime = System.currentTimeMillis();
		
		LOGGER.debug(String.format("target [%s] time [%s].",
				joinPoint.getSignature(), endTime - beginTime));
		
		return res;
	}
}
