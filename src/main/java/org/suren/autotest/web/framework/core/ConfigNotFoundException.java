/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core;

/**
 * 找不到配置文件异常
 * @author suren
 * @date 2016年11月24日 上午8:22:50
 */
public class ConfigNotFoundException extends AutoTestException
{

	/**  */
	private static final long	serialVersionUID	= 1L;

	private String type;
	private String configPath;
	
	/**
	 * @param type
	 * @param configPath
	 */
	public ConfigNotFoundException(String type, String configPath)
	{
		this.type = type;
		this.configPath = configPath;
	}

	@Override
	public String getMessage()
	{
		return String.format("Can not found config file for AutoTest,"
				+ " type [%s], path [%s].", type, configPath);
	}

}
