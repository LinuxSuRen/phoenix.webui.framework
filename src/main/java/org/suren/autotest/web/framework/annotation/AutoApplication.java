package org.suren.autotest.web.framework.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author suren
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@ComponentScan
public @interface AutoApplication {
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

    @AliasFor(
            annotation = ComponentScan.class,
            attribute = "basePackages"
    )
    String[] scanBasePackages() default {};
}
