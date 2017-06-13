package org.suren.autotest.web.framework.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 一个独立的测试用例
 * @author suren
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@ComponentScan
public @interface AutoApplication
{
    /**
     * 用例名称
     * @return
     */
    String name() default "";

    /**
     * 用例描述信息
      * @return
     */
    String description() default "";

    /**
     * @return 项目（模块）关注者的邮件列表
     */
    String[] concernMailList() default {};

    /**
     * Page子类所在的包（package）
     * @return
     */
    @AliasFor(
            annotation = ComponentScan.class,
            attribute = "basePackages"
    )
    String[] scanBasePackages() default {};
}
