package org.suren.autotest.web.framework.annotation;

import java.lang.annotation.*;

/**
 * Mark a class as a runner
 */
@Target(value = {ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoRunner {
}
