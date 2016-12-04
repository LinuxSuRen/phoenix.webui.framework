/**
 * http://surenpi.com
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

    public SuRenCompiler(File file, String workDir) {
        this.file = file;
        this.workDir = workDir;
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
        String fullPkgName = this.file.getParentFile().getAbsolutePath().replace(workDir, "");
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
