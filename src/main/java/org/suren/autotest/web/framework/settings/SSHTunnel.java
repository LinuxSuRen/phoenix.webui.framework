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

import java.io.IOException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

/**
 * ssh通道
 * @author <a href="http://surenpi.com">suren</a>
 * @since 2.1.0
 */
public class SSHTunnel
{
    private JSch jsch = new JSch();
    private Session session;
    
    public boolean open(String host, String user, final String password, int port)
    {
        try
        {
            session = jsch.getSession(user, host, port);
            session.setUserInfo(new UserInfo() {

                @Override
                public String getPassphrase()
                {
                    return null;
                }

                @Override
                public String getPassword()
                {
                    return password;
                }

                @Override
                public boolean promptPassphrase(String arg0)
                {
                    return false;
                }

                @Override
                public boolean promptPassword(String arg0)
                {
                    return true;
                }

                @Override
                public boolean promptYesNo(String arg0)
                {
                    return true;
                }

                @Override
                public void showMessage(String arg0)
                {
                    System.out.println("hello msg");
                }});
            session.connect();
            
            return true;
        }
        catch (JSchException e)
        {
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean build()
    {
        try
        {
            ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
            
            sftp.connect();
            sftp.cd("/home/surenpi/Downloads");
            sftp.disconnect();
            
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("/usr/bin/touch /home/surenpi/Desktop/hello");
            channel.connect();
            channel.disconnect();
            
            return true;
        }
        catch (JSchException e)
        {
            e.printStackTrace();
        }
        catch (SftpException e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public void close()
    {
        session.disconnect();
    }
}
