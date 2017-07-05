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

package org.suren.autotest.web.framework.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.surenpi.autotest.datasource.DynamicData;

/**
 * 从文本文件中随机获取一个字符串
 * @author suren
 * @date 2017年4月4日 下午12:17:36
 */
@Component
public class RandomTxtDynamicData implements DynamicData
{
	@Override
	public String getValue(String orginData)
	{
		URL url = getInput(orginData);
		if(url == null)
		{
			throw new RuntimeException(String.format("Can not found random txt [%s] from data dir.", orginData));
		}
		
		List<String> list = new ArrayList<String>();
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())))
		{
			String line = null;
			while((line = reader.readLine()) != null)
			{
				line = line.trim();
				if("".equals(line))
				{
					continue;
				}
				
				list.add(line);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		if(list.size() == 0)
		{
			throw new RuntimeException(String.format("Random txt file [%s] content is empty!", orginData));
		}
		
		return list.get(new Random().nextInt(list.size()));
	}

	@Override
	public String getType()
	{
		return "random_text";
	}
	
	private URL getInput(String key)
	{
		URL url = RandomTxtDynamicData.class.getResource("/data/" + key);
		
		return url;
	}

	@Override
	public void setData(Map<String, Object> data)
	{
	}

}
