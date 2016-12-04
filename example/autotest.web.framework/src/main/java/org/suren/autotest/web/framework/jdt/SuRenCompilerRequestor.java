/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.jdt;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;

/**
 * @author suren
 * @date 2016年11月26日 下午5:51:03
 */
public class SuRenCompilerRequestor implements ICompilerRequestor
{
	private String workDir;
	
	public SuRenCompilerRequestor(String workDir)
	{
		this.workDir = workDir;
	}
	
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
            File target = new File(workDir, clazzName.replace(".", "/") + ".class");
            try {
                FileUtils.writeByteArrayToFile(target, clazzFiles[i].getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
}
