/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.page;

/**
 * 代表一个iFrame
 * @author suren
 * @date Jul 24, 2016 5:39:54 PM
 */
public class IFramePage extends Page
{
	private String frameId;

	public String getFrameId()
	{
		return frameId;
	}

	public void setFrameId(String frameId)
	{
		this.frameId = frameId;
	}
}
