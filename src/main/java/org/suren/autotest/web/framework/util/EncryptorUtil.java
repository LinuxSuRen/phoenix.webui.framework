/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Properties;

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
	public static final String ENCRYPT_FILE = "encrypt.properties";
	public static final String ENCRYPT_KEY = "encrypt.key";
	
	/**
	 * @return 从配置文件中获取私钥
	 */
	public static String getSecretKey()
	{
		ClassLoader clsLoader = EncryptorUtil.class.getClassLoader();
		Enumeration<URL> urls = null;
		try
		{
			urls = clsLoader.getResources(ENCRYPT_FILE);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		if(urls == null || !urls.hasMoreElements())
		{
			throw new RuntimeException("Can not found encrypt.properties!");
		}
		
		try(InputStream input = urls.nextElement().openStream())
		{
			if(input == null)
			{
				throw new RuntimeException("Can not found encrypt.properties!");
			}
			
			Properties encryptPro = new Properties();
			encryptPro.load(input);
			
			if(!encryptPro.containsKey(ENCRYPT_KEY))
			{
				throw new RuntimeException("Can not found " + ENCRYPT_KEY + " from encrypt.properties!");
			}
			
			return encryptPro.getProperty(ENCRYPT_KEY);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 加密字符串
	 * @param plainText
	 * @return
	 */
	public static String encrypt(String plainText)
	{
		try
		{
			return Encryptor.getInstance(Encryptor.ALG_DES, getSecretKey()).encryptStr(plainText);
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
			return Encryptor.getInstance(Encryptor.ALG_DES, getSecretKey()).decryptStr(encryptText);
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
		try
		{
			return Base64.encodeBase64String(Encryptor.getInstance(Encryptor.ALG_DES, getSecretKey()).encrypt(plainText));
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
			return Encryptor.getInstance(Encryptor.ALG_DES, getSecretKey()).decryptStr(encryptData);
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
		
		return base64Text;
	}
}
