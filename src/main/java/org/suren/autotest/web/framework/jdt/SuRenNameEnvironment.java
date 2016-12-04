/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.jdt;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;

/**
 * @author suren
 * @date 2016年11月26日 下午5:50:44
 */
public class SuRenNameEnvironment implements INameEnvironment
{
	private String workDir;
	
	public SuRenNameEnvironment(String workDir)
	{
		this.workDir = workDir;
	}

	private String join(char[][] chars)
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
		File file = new File(workDir, name.replace('.', '/') + ".java");
		if (file.isFile())
		{
			return new NameEnvironmentAnswer(new SuRenCompilationUnit(file, workDir),
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

		File target = new File(workDir, name.replace('.', '/'));
		return !target.isFile();
	}
}
