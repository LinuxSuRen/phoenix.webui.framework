/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.util;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author suren
 * @date 2016年12月24日 下午3:59:03
 */
public class EncryptUtilTest
{
	@Test
	public void test()
	{
		String plainText =" hello encryptor";
		
		String encryptText = EncryptorUtil.encrypt(plainText);
		String afterPlainText = EncryptorUtil.decrypt(encryptText);
		
		Assert.assertEquals(plainText, afterPlainText);
		
		System.out.println(encryptText);
		
		String base64 = Base64.encodeBase64String(encryptText.getBytes());
		System.out.println(base64);
		System.out.println(new String(Base64.decodeBase64(base64.getBytes())));
	}
}
