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

package org.suren.autotest.web.framework.settings;

import java.io.File;
import java.io.IOException;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * 文件夹可用性校验，如果无法创建指定的目录，则抛出异常
 * @author linuxsuren
 * @since 2.1.0
 */
public class FolderValidator implements IParameterValidator
{

    @Override
    public void validate(String name, String value) throws ParameterException
    {
        File file = new File(value);
        if(file.isFile())
        {
            throw new ParameterException(String.format("目标目录%s已经是一个文件，需要指定文件夹！", value));
        }

        String msg = String.format("当前用户没有创建目录%s的权限！", value);
        if(file.isDirectory())
        {
            File tmpFile = new File(file, ".suren");
            try
            {
                if(tmpFile.createNewFile())
                {
                    tmpFile.delete();
                    return;
                }
            }
            catch (IOException e)
            {
                msg = e.getMessage();
            }
        }
        else
        {
            if(file.mkdirs())
            {
                file.delete();
                return;
            }
        }
        
        throw new ParameterException(msg);
    }

}
