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

package org.suren.autotest.web.framework.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网络相关的工具类
 * @author suren
 * @date 2017年5月7日 下午4:33:43
 */
public class NetUtil
{
    /**
     * @return 所有的对外ip，没有的话返回空集合
     */
    public static Map<String, String> allIP()
    {
    	Map<String, String> allIP = new HashMap<String, String>();
        try
        {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements())
            {
                NetworkInterface netInter = (NetworkInterface) netInterfaces.nextElement();
                
                List<InterfaceAddress> address = netInter.getInterfaceAddresses();
                for(InterfaceAddress interAddr : address)
                {
                	InetAddress addr = interAddr.getAddress();
                	if(addr.isLoopbackAddress() || addr.isLinkLocalAddress() || addr instanceof Inet6Address)
                	{
                		continue;
                	}
                	
                	allIP.put(netInter.getDisplayName(), addr.getHostAddress());
                }
            }
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
    
        return allIP;
    }
}
