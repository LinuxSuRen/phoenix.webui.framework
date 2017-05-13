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

package org.suren.autotest.web.framework.page;

/**
 * 代表一个iFrame
 * @author suren
 * @date Jul 24, 2016 5:39:54 PM
 */
public class IFramePage extends Page
{
	private String frameId;

	public String getFrameId()
	{
		return frameId;
	}

	public void setFrameId(String frameId)
	{
		this.frameId = frameId;
	}
}
