package org.suren.autotest.web.framework.core;

/**
 * Created by suren on 2017/6/9.
 */
public class ElementException extends RuntimeException
{
    public ElementException(String message)
    {
        super(message);
    }

    public ElementException(Class<?> elementCls)
    {
        super("wrong element type: " + elementCls.getClass().getName());
    }
}
