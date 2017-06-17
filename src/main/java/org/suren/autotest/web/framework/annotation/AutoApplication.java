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
     * 该信息会在测试报告中体现。支持的文件格式为：.yml、.properties、.txt等。</br>
     * ymal和properties文件都会被解析成成json字符串。
     * @return 用户自定义的环境信息，classpath下的路径
     */
    String customEnvInfo() default "";

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
