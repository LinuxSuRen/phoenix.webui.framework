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

package org.suren.autotest.web.framework.report.record;

import java.util.List;

/**
 * 搜索历史记录
 * @author suren
 * @date 2016年9月6日 下午8:29:33
 */
public class SearchRecord
{
	/** 搜索策略 */
	private String strategy;
	/** 定位方法 */
	private List<String> byList;
	/** 耗时 */
	private long cost;
	public SearchRecord(String strategy, long cost)
	{
		this.strategy = strategy;
		this.cost = cost;
	}
	public SearchRecord(long cost)
	{
		this.cost = cost;
	}
	/**
	 * @return the strategy
	 */
	public String getStrategy()
	{
		return strategy;
	}
	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategy(String strategy)
	{
		this.strategy = strategy;
	}
	/**
	 * @return the byList
	 */
	public List<String> getByList()
	{
		return byList;
	}
	/**
	 * @param byList the byList to set
	 */
	public void setByList(List<String> byList)
	{
		this.byList = byList;
	}
	/**
	 * @return the cost
	 */
	public long getCost()
	{
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(long cost)
	{
		this.cost = cost;
	}
}
