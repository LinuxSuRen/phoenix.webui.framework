/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.jdt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * 用于编译Java代码
 * @author suren
 * @date 2016年11月26日 下午3:47:01
 */
public class JDTUtils
{
	private String workDir;
	
	public JDTUtils(File workDirFile)
	{
		this.workDir = workDirFile.getAbsolutePath();
	}
	
	/**
	 * @param workDir 源码所在目录
	 */
	public JDTUtils(String workDir)
	{
		this.workDir = workDir;
	}
	
	/**
	 * @param srcCode 类全称，例如：org.suren.Test
	 */
	public void compileCode(String srcCode)
	{
		String srcCodeFile = srcCode.replace(".", "/");
		
		compileFile(srcCodeFile);
	}
	
	/**
	 * @return 编译过的Java源码文件集合，二进制文件和源文件在同一个目录中
	 */
	public List<File> compileAllFile()
	{
		List<File> result = new ArrayList<File>();
		File workRoot = new File(workDir);
		if(workRoot.isDirectory())
		{
			for(File file : workRoot.listFiles())
			{
				result.addAll(compileAllFile(file));
			}
		}
		
		return result;
	}
	
	public List<File> compileAllFile(File srcFile)
	{
		List<File> result = new ArrayList<File>();
		
		if(srcFile.isDirectory())
		{
			for(File item : srcFile.listFiles())
			{
				result.addAll(compileAllFile(item));
			}
		}
		else if(srcFile.isFile() && srcFile.getName().endsWith(".java"))
		{
			compileFile(srcFile);
			
			result.add(srcFile);
		}
		
		return result;
	}
	
	public void compileFile(File srcFile)
	{
		INameEnvironment env = new SuRenNameEnvironment(workDir);
	    ICompilerRequestor compilerRequestor = new SuRenCompilerRequestor(workDir);
        
        
        IProblemFactory problemFactory = new DefaultProblemFactory(Locale.ENGLISH);
        IErrorHandlingPolicy policy = DefaultErrorHandlingPolicies.exitOnFirstError();
 
        CompilerOptions compilerOptions = getCompilerOptions();
        
        org.eclipse.jdt.internal.compiler.Compiler jdtCompiler =
                new org.eclipse.jdt.internal.compiler.Compiler(env, policy, compilerOptions,
                        compilerRequestor, problemFactory);
 
        ICompilationUnit[] unit = new ICompilationUnit[] {new SuRenCompiler(srcFile, workDir)};;
        jdtCompiler.compile(unit);
	}

	/**
	 * @param srcCodeFile 相对源码（src）目录的Java代码路径
	 */
	public void compileFile(String srcCodeFile)
	{
		compileFile(new File(workDir, srcCodeFile));
	}
	
    private CompilerOptions getCompilerOptions()
    {
    	String javaVersion = CompilerOptions.VERSION_1_7;
    	
        Map<String, String> settings = new HashMap<String, String>();
        settings.put(CompilerOptions.OPTION_Source, javaVersion);
        settings.put( CompilerOptions.OPTION_TargetPlatform, javaVersion);
        settings.put(CompilerOptions.OPTION_Compliance, javaVersion);
        settings.put(CompilerOptions.OPTION_Encoding, "utf-8");
        
        return new CompilerOptions(settings);
    }
}
