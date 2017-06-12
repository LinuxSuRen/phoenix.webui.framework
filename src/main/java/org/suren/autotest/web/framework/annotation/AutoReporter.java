package org.suren.autotest.web.framework.annotation;

import java.lang.annotation.*;

/**
 * @author suren
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoReporter {
    String email() default "";

    String model() default "";

    String description() default "";
}
