/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.jdt;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;

/**
 * @author suren
 * @date 2016年11月26日 下午3:47:01
 */
public class JdtTest
{

	private static final File	WORKDIR	= new File(
		"D:/Program Files (x86)/Gboat-Toolkit-Suit/workspace_surenpi/autotest.web.framework/src/test/java");

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		INameEnvironment env = new INameEnvironment()
		{

			@Override
			public void cleanup()
			{
			}

			@Override
			public NameEnvironmentAnswer findType(char[][] compoundTypeName)
			{
				return findType(join(compoundTypeName));
			}

			@Override
			public NameEnvironmentAnswer findType(char[] typeName,
					char[][] packageName)
			{
				return findType(join(packageName) + "." + new String(typeName));
			}

			private NameEnvironmentAnswer findType(final String name)
			{
				File file = new File(WORKDIR, name.replace('.', '/') + ".java");
				if (file.isFile())
				{
					return new NameEnvironmentAnswer(new CompilationUnit(file),
							null);
				}
				try
				{
					InputStream input = this
							.getClass()
							.getClassLoader()
							.getResourceAsStream(
									name.replace(".", "/") + ".class");
					if (input != null)
					{
						byte[] bytes = IOUtils.toByteArray(input);
						if (bytes != null)
						{
							ClassFileReader classFileReader = new ClassFileReader(
									bytes, name.toCharArray(), true);
							return new NameEnvironmentAnswer(classFileReader,
									null);
						}
					}
				}
				catch (ClassFormatException e)
				{
					throw new RuntimeException(e);
				}
				catch (IOException e)
				{
					throw new RuntimeException(e);
				}
				return null;
			}

			@Override
			public boolean isPackage(char[][] parentPackageName, char[] packageName)
			{
				String name = new String(packageName);
				if (parentPackageName != null)
				{
					name = join(parentPackageName) + "." + name;
				}

				File target = new File(WORKDIR, name.replace('.', '/'));
				return !target.isFile();
			}

		};
		
	    ICompilerRequestor compilerRequestor = new ICompilerRequestor() {
	    	 
            public void acceptResult(CompilationResult result) {
                if (result.hasErrors()) {
                    for (IProblem problem : result.getErrors()) {
                        String className = new String(problem.getOriginatingFileName()).replace("/", ".");
                        className = className.substring(0, className.length() - 5);
                        String message = problem.getMessage();
                        if (problem.getID() == IProblem.CannotImportPackage) {
                            message = problem.getArguments()[0] + " cannot be resolved";
                        }
                        throw new RuntimeException(className + ":" + message);
                    }
                }
 
                ClassFile[] clazzFiles = result.getClassFiles();
                for (int i = 0; i < clazzFiles.length; i++) {
                    String clazzName = join(clazzFiles[i].getCompoundName());
                    File target = new File(WORKDIR, clazzName.replace(".", "/") + ".class");
                    try {
                        FileUtils.writeByteArrayToFile(target, clazzFiles[i].getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        
        
        IProblemFactory problemFactory = new DefaultProblemFactory(Locale.ENGLISH);
        IErrorHandlingPolicy policy = DefaultErrorHandlingPolicies.exitOnFirstError();
 
        org.eclipse.jdt.internal.compiler.Compiler jdtCompiler =
                new org.eclipse.jdt.internal.compiler.Compiler(env, policy, getCompilerOptions(),
                        compilerRequestor, problemFactory);
 
        jdtCompiler.compile(
        		new ICompilationUnit[] {
        				new CompilationUnit(new File(WORKDIR, "org\\suren\\autotest\\web\\framework\\ant\\TestAnt.java"))});
	}
	
    public static CompilerOptions getCompilerOptions() {
        Map settings = new HashMap();
        String javaVersion = CompilerOptions.VERSION_1_7;
        settings.put(CompilerOptions.OPTION_Source, javaVersion);
        settings.put(CompilerOptions.OPTION_Compliance, javaVersion);
        return new CompilerOptions(settings);
    }

	private static String join(char[][] chars)
	{
		StringBuilder sb = new StringBuilder();
		for (char[] item : chars)
		{
			if (sb.length() > 0)
			{
				sb.append(".");
			}
			sb.append(item);
		}
		return sb.toString();
	}
	
	  private static class CompilationUnit implements ICompilationUnit {
		  
	        private File file;
	 
	        public CompilationUnit(File file) {
	            this.file = file;
	        }
	 
	        public char[] getContents() {
	            try {
	                return FileUtils.readFileToString(file).toCharArray();
	            } catch (IOException e) {
	                throw new RuntimeException(e);
	            }
	        }
	 
	        public char[] getMainTypeName() {
	            return file.getName().replace(".java", "").toCharArray();
	        }
	 
	        public char[][] getPackageName() {
	            String fullPkgName = this.file.getParentFile().getAbsolutePath().replace(WORKDIR.getAbsolutePath(), "");
	            fullPkgName = fullPkgName.replace("/", ".").replace("\\", ".");
	            if (fullPkgName.startsWith("."))
	                fullPkgName = fullPkgName.substring(1);
	            String[] items = fullPkgName.split("[.]");
	            char[][] pkgName = new char[items.length][];
	            for (int i = 0; i < items.length; i++) {
	                pkgName[i] = items[i].toCharArray();
	            }
	            return pkgName;
	        }
	 
	        public boolean ignoreOptionalProblems() {
	            return false;
	        }
	 
	        public char[] getFileName() {
	            return this.file.getName().toCharArray();
	        }
	    }
}
