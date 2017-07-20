/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.Test;

/**
 * @author suren
 * @since 2016年12月11日 下午9:15:21
 */
public class EncryptorTest
{
	@Test
	public void test() throws NoSuchAlgorithmException, NoSuchPaddingException, 
		InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		Encryptor encryptor = Encryptor.getInstance(Encryptor.ALG_DES);
		
		String plainText = "123456";
		
		String encryptText = encryptor.encryptStr(plainText);
		System.out.println("plainText : " + plainText);
		System.out.println("encryptText : " + encryptText);
		
		String decryptText = encryptor.decryptStr(encryptText);
		System.out.println("decryptText : " + decryptText);
	}
}
