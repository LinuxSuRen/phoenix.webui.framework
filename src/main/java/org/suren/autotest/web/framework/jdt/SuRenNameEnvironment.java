/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.jdt;

import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;

/**
 * @author suren
 * @date 2016年11月26日 下午5:50:44
 */
public class SuRenNameEnvironment implements INameEnvironment
{

	@Override
	public void cleanup()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public NameEnvironmentAnswer findType(char[][] arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NameEnvironmentAnswer findType(char[] arg0, char[][] arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPackage(char[][] arg0, char[] arg1)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
