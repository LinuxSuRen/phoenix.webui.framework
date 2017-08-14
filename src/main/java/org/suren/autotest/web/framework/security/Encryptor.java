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

package org.suren.autotest.web.framework.security;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * 加密类
 * @author <a href="http://surenpi.com">suren</a>
 */
public class Encryptor
{
    private KeyGenerator    keyGen;
    private SecretKey   key;
    private Cipher  cipher;
    private byte[]  cipherByte;
    
    public static final String ALG_DES = "DES";
    private static Encryptor encryptor;
    
    private Encryptor(){}
    
    private Encryptor(String algorithm) throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        keyGen = KeyGenerator.getInstance(algorithm);
        key = keyGen.generateKey();
        
        cipher = Cipher.getInstance(algorithm);
    }
    
    public static Encryptor getInstance(String algorithm) throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        if(encryptor == null)
        {
            encryptor = new Encryptor(algorithm);
        }
        
        return encryptor;
    }
    
    public byte[] encrypt(String str) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherByte = cipher.doFinal(str.getBytes());
        return cipherByte;
    }
    
    public String encryptStr(String str) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        byte[] encryptArray = encrypt(str);
        
        StringBuffer strBuf = new StringBuffer();
        for(byte e : encryptArray)
        {
            strBuf.append(intToChar((e >> 4) & 0x0f));
            strBuf.append(intToChar(e & 0x0f));
        }
        
        return strBuf.toString();
    }
    
    public byte[] decrypt(byte[] buf) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        cipher.init(Cipher.DECRYPT_MODE, key);
        cipherByte = cipher.doFinal(buf);
        return cipherByte;
    }
    
    public String decryptStr(byte[] buf) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        return new String(decrypt(buf));
    }
    
    /**
     * decrypt string
     * @see #encryptStr(String)
     * @param encryptStr 1
     * @return 1
     * @throws InvalidKeyException 1
     * @throws IllegalBlockSizeException 1
     * @throws BadPaddingException 1
     * @throws NullPointerException if encryptStr is null
     */
    public String decryptStr(String encryptStr) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        if(encryptStr == null)
        {
            throw new NullPointerException();
        }
        
        encryptStr = encryptStr.toUpperCase();
        
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        int len = encryptStr.length();
        for(int i = 0; i < len; i += 2)
        {
            int b = ((charToByte(encryptStr.charAt(i)) << 4) & 0xff) | charToByte(encryptStr.charAt(i + 1));
            byteOut.write(b);
        }
        
        return decryptStr(byteOut.toByteArray());
    }
    
    private byte charToByte(char c)
    {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    
    private char intToChar(int b)
    {
        return "0123456789ABCDEF".charAt(b);
    }
    
    public void clean()
    {
        cipherByte = null;
    }
}
