/**
 * 
 */
package org.suren.autotest.web.framework.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.surenpi.autotest.webui.core.Locator;

/**
 * 根据标签文本来定位。例如下面的标签：
 * <pre>{@code
 * <button data="simple" data-color="red">按钮</button>
 * }</pre>
 * 标签使用方法为：
 * <pre>
 * &#064;AutoAttrLocator(tagName = "button", text = "按钮")
 * private Button But;
 * </pre>
 * @author <a href="http://surenpi.com">suren</a>
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@AutoField
public @interface AutoTextLocator
{
    /**
     * 例如下面的标签名称为button
     * <pre>{@code
     * <button data="simple" data-color="red">按钮</button>
     * }</pre>
     * @return 标签名
     */
    String tagName();
    
    /**
     * 例如下面的文本为“按钮”
     * <pre>{@code
     * <button data="simple" data-color="red">按钮</button>
     * }</pre>
     * @return 文本
     */
    String text();
    
    /**
     * @return 条件
     */
    int condition() default Locator.EQUAL;

    /**
     * @return 显式的查找超时时间（毫秒）
     */
    long timeout() default 0;

    /**
     * @return 定位方法的优先级，数字越大优先级越高
     */
    int order() default 0;
}
