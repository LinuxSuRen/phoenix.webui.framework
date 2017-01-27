/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.jdt;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;

/**
 * @author suren
 * @date 2016年11月26日 下午3:47:01
 */
public class JdtTest
{

	private static final File	WORKDIR	= new File(
		"D:/Program Files (x86)/Gboat-Toolkit-Suit/workspace_surenpi/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/autotest.platform/deploy/src");

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		INameEnvironment env = new SuRenNameEnvironment(WORKDIR.getAbsolutePath());
	    ICompilerRequestor compilerRequestor = new SuRenCompilerRequestor(WORKDIR.getAbsolutePath());
        
        
        IProblemFactory problemFactory = new DefaultProblemFactory(Locale.ENGLISH);
        IErrorHandlingPolicy policy = DefaultErrorHandlingPolicies.exitOnFirstError();
 
        org.eclipse.jdt.internal.compiler.Compiler jdtCompiler =
                new org.eclipse.jdt.internal.compiler.Compiler(env, policy, getCompilerOptions(),
                        compilerRequestor, problemFactory);
 
        jdtCompiler.compile(
        		new ICompilationUnit[] {
        				new SuRenCompiler(new File(WORKDIR, "com/glodon/fujian/page/LoginPage.java"), WORKDIR.getAbsolutePath())});
	}
	
    public static CompilerOptions getCompilerOptions() {
        Map<String, String> settings = new HashMap<String, String>();
        String javaVersion = CompilerOptions.VERSION_1_7;
        settings.put(CompilerOptions.OPTION_Source, javaVersion);
        settings.put(CompilerOptions.OPTION_Compliance, javaVersion);
        return new CompilerOptions(settings);
    }
}
