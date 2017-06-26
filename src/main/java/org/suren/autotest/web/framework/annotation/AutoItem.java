package org.suren.autotest.web.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 键值对注解
 * @author suren
 * @date 2017年6月26日 上午10:35:20
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoItem
{
	/**
	 * @return key
	 */
	String key();
	
	/**
	 * @return value
	 */
	@AutoDynamicData
	String value();
	
	/**
	 * @return 如果为true，则希望value被覆盖
	 */
	boolean override() default true;
}
