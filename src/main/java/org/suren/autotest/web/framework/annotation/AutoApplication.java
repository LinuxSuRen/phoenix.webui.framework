package org.suren.autotest.web.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;

/**
 * 一个独立的测试用例
 * @author linuxsuren
 * @since 2.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@ComponentScan
public @interface AutoApplication
{
    /**
     * @return 用例名称
     */
	@AliasFor("value")
    String name() default "";

    /**
     * @return 用例描述信息
     */
    String description() default "";

	@AliasFor("name")
    String value() default "";

    /**
     * 该信息会在测试报告中体现。支持的文件格式为：.yml、.properties、.txt等。
     * ymal和properties文件都会被解析成成json字符串。
     * @return 用户自定义的环境信息，classpath下的路径
     */
    String customEnvInfo() default "";

    /**
     * @return 项目（模块）关注者的邮件列表
     */
    String[] concernMailList() default {};

    /**
     * @return Page子类所在的包（package）
     */
    @AliasFor(
            annotation = ComponentScan.class,
            attribute = "basePackages"
    )
    String[] scanBasePackages() default {};
}
