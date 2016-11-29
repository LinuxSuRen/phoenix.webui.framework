/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.jdt;

import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;

/**
 * @author suren
 * @date 2016年11月26日 下午5:52:59
 */
public class SuRenCompiler extends Compiler
{

	/**
	 * @param environment
	 * @param policy
	 * @param options
	 * @param requestor
	 * @param problemFactory
	 */
	public SuRenCompiler(INameEnvironment environment,
			IErrorHandlingPolicy policy, CompilerOptions options,
			ICompilerRequestor requestor, IProblemFactory problemFactory)
	{
		super(environment, policy, options, requestor, problemFactory);
		// TODO Auto-generated constructor stub
	}

}
