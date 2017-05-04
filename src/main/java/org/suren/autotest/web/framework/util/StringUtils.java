/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.util;

import java.util.Iterator;
import java.util.Map;

/**
 * @author suren
 * @date 2016年11月26日 上午10:22:04
 */
public class StringUtils {
	/**
	 * 把参数型的值进行转换
	 * 
	 * @param data
	 * @param paramPrefix
	 * @param value
	 * @return
	 */
	public static String paramTranslate(Map<String, Object> data,
			String paramPrefix, String value) {
		String result = value;

		Iterator<String> dataIt = data.keySet().iterator();
		while (dataIt.hasNext()) {
			String param = dataIt.next();
			if (!param.startsWith(paramPrefix)) {
				continue;
			}

			Object paramVal = data.get(param);
			if (paramVal != null) {
				result = result
						.replace("${" + param + "}", paramVal.toString());
			}
		}

		return result;
	}

	/**
	 * @param namesize servernamesize
	 *            指定长度的EMAIL
	 * @return 随机的email地址
	 */
	public static String email( int namesize, int servernamesize) {
		
		String name = RandomStringUtils.randomAlphabetic(randomtest(namesize));
		
		String[] sa = new String[3];
		sa[0]="com";
		sa[1]="cn";
		sa[2]="net";
		StringBuffer buf = new StringBuffer();
		String server=name+"."+sa[randomtest(sa.length)];

		buf.append(RandomStringUtils.randomAlphabetic(randomtest(servernamesize)));
		buf.append("@");
		buf.append(server);

		return buf.toString();
		
	}
	/**
	 * @return 随机的email地址
	 */
	public static String email() {
		String name = RandomStringUtils.randomAlphabetic(randomtest(10));
		
		String[] sa = new String[3];
		sa[0]="com";
		sa[1]="cn";
		sa[2]="net";
		StringBuffer buf = new StringBuffer();
		String server=name+"."+sa[randomtest(sa.length)];

		buf.append(RandomStringUtils.randomAlphabetic(randomtest(10)));
		buf.append("@");
		buf.append(server);

		return buf.toString();
		
	}

	public static int randomtest(int max) {
		int x = 0;
		if (max > 0) {
			x =  (int) (Math.random() * max);
			return x;

		}

		return 0;
	}

	public static boolean isNotBlank(CharSequence cs) {
		return !StringUtils.isBlank(cs);
	}

	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param hostType
	 * @param hostValue
	 * @return
	 */
	public static boolean isAnyBlank(String hostType, String hostValue) {
		return StringUtils.isBlank(hostValue) || StringUtils.isBlank(hostType);
	}

	/**
	 * 把字符串转化为首字母小写
	 * 
	 * @param clsName
	 * @return
	 */
	public static String uncapitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuilder(strLen)
				.append(Character.toLowerCase(str.charAt(0)))
				.append(str.substring(1)).toString();
	}
}
