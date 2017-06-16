package org.suren.autotest.web.framework.annotation;

import java.lang.annotation.*;

/**
 * 模块方法中对异常的配置
 * @author suren
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoExpect
{
    /**
     * 如果方法调用中没有发生指定的异常，则认为用例失败
     * @return 期待发生的异常
     */
    Class expect();

    /**
     * @return 可以接受的异常，则发生这些异常也认为是正常情况
     */
    Class[] accept() default {};
}
