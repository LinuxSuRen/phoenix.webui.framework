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

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/** 
 * 
 * 身份证算法实现
 * 
 * 1、号码的结构 公民身份号码是特征组合码，
 *     由十七位数字本体码和一位校验码组成。
 * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码  三位数字顺序码和一位数字校验码。 
 * 
 * 2、地址码(前六位数） 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 
 * 
 * 3、出生日期码（第七位至十四位） 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 
 * 
 * 4、顺序码（第十五位至十七位） 
 *    表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配给女性。 
 * 
 * 5、校验码（第十八位数） 
 *   （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 
 * ，先对前17位数字的权求和
 *  Ai:表示第i位置上的身份证号码数字值
 *   Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 
 * （2）计算模 Y = mod(S, 11) 
 * （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7  8 9 10 
 *   校验码: 1 0 X 9 8 7 6 5 4 3 2 
 * 
 * @author suren
 * @since 2017年1月4日 上午8:49:49
 */
public class IDCardUtil
{

    public static final Map<String, Integer> areaCode = new HashMap<String, Integer>();

    static
    {
    	Properties pro = new Properties();
    	ClassLoader idClassLoader = IDCardUtil.class.getClassLoader();
    	try(InputStream idInput = idClassLoader.getResourceAsStream("data/id_card_area.properties"))
    	{
    		if(idInput == null)
    		{
    			throw new RuntimeException("Can not found idCard area data file.");
    		}
    		
    		pro.load(idInput);
    		
    		for(Object key : pro.keySet())
    		{
    			Integer code = null;
    			try
    			{
    				code = Integer.parseInt(pro.get(key).toString());
    			}
    			catch(NullPointerException | NumberFormatException e)
    			{}
    			
    			if(code == null)
    			{
    				continue;
    			}
    			areaCode.put(key.toString(), code);
    		}
    	}
		catch(IOException e)
		{
			e.printStackTrace();
		}
    }

    /**
     * @return 随机身份证号码
     */
    public static String generate()
    {
        StringBuilder generater = new StringBuilder();
        generater.append(randomAreaCode());
        generater.append(randomBirthday());
        generater.append(randomCode());
        generater.append(calcTrailingNumber(generater.toString().toCharArray()));
        return generater.toString();
    }

    /**
     * @return 随机区域编码
     */
    private static int randomAreaCode()
    {
        int index = (int) (Math.random() * IDCardUtil.areaCode.size());
        Collection<Integer> values = IDCardUtil.areaCode.values();
        Iterator<Integer> it = values.iterator();
        int i = 0;
        int code = 0;
        while (i < index && it.hasNext()) {
            i++;
            code = it.next();
        }
        return code;
    }

    /**
     * @return 随机生日
     */
    private static String randomBirthday()
    {
        Calendar birthday = Calendar.getInstance();
        birthday.set(Calendar.YEAR, (int) (Math.random() * 60) + 1950);
        birthday.set(Calendar.MONTH, (int) (Math.random() * 12));
        birthday.set(Calendar.DATE, (int) (Math.random() * 31));

        StringBuilder builder = new StringBuilder();
        builder.append(birthday.get(Calendar.YEAR));
        long month = birthday.get(Calendar.MONTH) + 1;
        if (month < 10) {
            builder.append("0");
        }
        builder.append(month);
        long date = birthday.get(Calendar.DATE);
        if (date < 10) {
            builder.append("0");
        }
        builder.append(date);
        return builder.toString();
    }

    /*
     * <p>18位身份证验证</p>
     * 根据〖中华人民共和国国家标准 GB 11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
     * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
     * 第十八位数字(校验码)的计算方法为：
     * 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * 2.将这17位数字和系数相乘的结果相加。
     * 3.用加出来和除以11，看余数是多少？
     * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2。
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
     */
    private static char calcTrailingNumber(char[] chars)
    {
        if (chars.length < 17) {
            return ' ';
        }
        int[] c = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
        char[] r = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
        int[] n = new int[17];
        int result = 0;
        for (int i = 0; i < n.length; i++) {
            n[i] = Integer.parseInt(chars[i] + "");
        }
        for (int i = 0; i < n.length; i++) {
            result += c[i] * n[i];
        }
        return r[result % 11];
    }

    /**
     * @return 随机序号码
     */
    private static String randomCode()
    {
        int code = (int) (Math.random() * 1000);
        if (code < 10) {
            return "00" + code;
        } else if (code < 100) {
            return "0" + code;
        } else {
            return "" + code;
        }
    }
}