package org.suren.autotest.web.framework.settings;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * 浏览器类型校验
 * @author <a href="http://surenpi.com">suren</a>
 * @since 2.1.0
 */
public class BrowserParamValidator implements IParameterValidator
{

    @Override
    public void validate(String name, String value) throws ParameterException
    {
        if(!DriverConstants.DRIVER_CHROME.equals(value)
                && !DriverConstants.DRIVER_FIREFOX.equals(value)
                && !DriverConstants.DRIVER_IE.equals(value)
                && !DriverConstants.DRIVER_HTML_UNIT.equals(value)
                && !DriverConstants.DRIVER_OPERA.equals(value)
                && !DriverConstants.DRIVER_PHANTOM_JS.equals(value)
                && value != null
                && !value.equals(""))
        {
            throw new ParameterException("Browser is invalid.");
        }
    }

}
