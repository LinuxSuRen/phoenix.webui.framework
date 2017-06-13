package org.suren.autotest.web.framework.annotation;

import java.lang.annotation.*;

/**
 * 测试报告发送
 * @author suren
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoReporter
{
    /**
     * @return 报告者的邮件地址
     */
    String email() default "";

    /**
     * @return 所属模块
     */
    String model() default "";

    /**
     * @return 报告描述
     */
    String description() default "";
}
