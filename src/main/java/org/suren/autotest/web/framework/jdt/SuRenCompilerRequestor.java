/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
	
    public void acceptResult(CompilationResult result)
    {
        if(result.hasErrors())
        {
            for(IProblem problem : result.getErrors())
            {
                String className = new String(problem.getOriginatingFileName()).replace("/", ".");
                className = className.substring(0, className.length() - 5);
                String message = problem.getMessage();
                
                if (problem.getID() == IProblem.CannotImportPackage)
                {
                    message = problem.getArguments()[0] + " cannot be resolved";
                }
                
                throw new RuntimeException(className + ":" + message);
            }
        }

        ClassFile[] clazzFiles = result.getClassFiles();
        for (int i = 0; i < clazzFiles.length; i++)
        {
            String clazzName = join(clazzFiles[i].getCompoundName());
            File target = new File(workDir, clazzName.replace(".", "/") + ".class");
            try
            {
                FileUtils.writeByteArrayToFile(target, clazzFiles[i].getBytes());
            }
            catch (IOException e)
            {
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
