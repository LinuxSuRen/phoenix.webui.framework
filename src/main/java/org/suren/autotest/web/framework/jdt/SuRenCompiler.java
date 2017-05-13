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
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;

/**
 * @author suren
 * @date 2016年11月26日 下午5:52:59
 */
public class SuRenCompiler implements ICompilationUnit
{
    private File file;
    private String workDir;

    public SuRenCompiler(File file, String workDir)
    {
        this.file = file;
        this.workDir = workDir;
    }

    public char[] getContents()
    {
        try
        {
            return FileUtils.readFileToString(file, "utf-8").toCharArray();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public char[] getMainTypeName()
    {
        return file.getName().replace(".java", "").toCharArray();
    }

    public char[][] getPackageName()
    {
        String fullPkgName = this.file.getParentFile().getAbsolutePath().replace(workDir, "");
        fullPkgName = fullPkgName.replace("/", ".").replace("\\", ".");
        if (fullPkgName.startsWith("."))
            fullPkgName = fullPkgName.substring(1);
        String[] items = fullPkgName.split("[.]");
        char[][] pkgName = new char[items.length][];
        for (int i = 0; i < items.length; i++)
        {
            pkgName[i] = items[i].toCharArray();
        }
        return pkgName;
    }

    public boolean ignoreOptionalProblems()
    {
        return false;
    }

    public char[] getFileName()
    {
        return this.file.getName().toCharArray();
    }
}
