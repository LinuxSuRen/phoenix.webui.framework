/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core;

/**
 * 配置文件异常
 * @author suren
 * @date 2016年9月11日 下午8:15:05
 */
public class ConfigException extends RuntimeException
{

	/**  */
	private static final long	serialVersionUID	= 1L;
	
	private final String message;

	/**
	 * 带有自定义消息的构造函数
	 * @param message
	 */
	public ConfigException(String message)
	{
		this.message = message;
	}

	@Override
	public String getMessage()
	{
		return String.format("%s %s", super.getMessage(), this.message);
	}
}
