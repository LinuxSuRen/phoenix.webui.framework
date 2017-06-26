package org.suren.autotest.web.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author suren
 * @date 2017年6月26日 上午10:17:00
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoCookie
{

    /**
     * @return 是否跳过目标方法
     */
    boolean skipMethod() default false;

    /**
     * @return 是否要执行打开页面操作
     */
    boolean openInvoke() default false;
    
    /**
     * @return 保存cookie的文件名，在~/.autotest中
     */
    String fileName() default "phoenix.autotest.cookie";
}
