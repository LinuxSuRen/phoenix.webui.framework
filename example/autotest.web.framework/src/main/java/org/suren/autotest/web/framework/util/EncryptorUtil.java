/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

/**
 * 加解密工具类
 * @author suren
 * @date 2016年12月24日 下午3:45:52
 */
public class EncryptorUtil
{
	/**
	 * 加密字符串
	 * @param plainText
	 * @return
	 */
	public static String encrypt(String plainText)
	{
		try
		{
			return Encryptor.getInstance(Encryptor.ALG_DES).encryptStr(plainText);
		}
		catch (InvalidKeyException e)
		{
			e.printStackTrace();
		}
		catch (IllegalBlockSizeException e)
		{
			e.printStackTrace();
		}
		catch (BadPaddingException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e)
		{
			e.printStackTrace();
		}
		
		return plainText;
	}
	
	/**
	 * 解密字符串
	 * @param encryptText
	 * @return
	 */
	public static String decrypt(String encryptText)
	{
		try
		{
			return Encryptor.getInstance(Encryptor.ALG_DES).decryptStr(encryptText);
		}
		catch (InvalidKeyException e)
		{
			e.printStackTrace();
		}
		catch (IllegalBlockSizeException e)
		{
			e.printStackTrace();
		}
		catch (BadPaddingException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e)
		{
			e.printStackTrace();
		}
		
		return encryptText;
	}
	
	/**
	 * 先加密后再进行Base64编码
	 * @param plainText
	 * @return
	 */
	public static String encryptWithBase64(String plainText)
	{
		String encryptText = encrypt(plainText);
//		Encryptor.getInstance(Encryptor.ALG_DES).encrypt(plainText)
		try
		{
			return Base64.encodeBase64String(Encryptor.getInstance(Encryptor.ALG_DES).encrypt(plainText));
		}
		catch (InvalidKeyException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalBlockSizeException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (BadPaddingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 先使用Base64解码然后解密
	 * @param base64Text
	 * @return
	 */
	public static String decryptWithBase64(String base64Text)
	{
		byte[] encryptData = Base64.decodeBase64(base64Text);
		try
		{
			return Encryptor.getInstance(Encryptor.ALG_DES).decryptStr(encryptData);
		}
		catch (InvalidKeyException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalBlockSizeException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (BadPaddingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return base64Text;
	}
}
