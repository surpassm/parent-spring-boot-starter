package com.github.surpassm.tool.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author mc
 * Create date 2019/4/15 11:18
 * Version 1.0
 * Description
 */
public class AesEncryptUtils {
	/**
	 * 可配置到Constant中，并读取配置文件注入
	 */
	private final static String AES = "AES";
	/**
	 * 算法名称/加密模式/数据填充方式
	 */
	private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
	/**
	 * 加密
	 * @param content 加密的字符串
	 * @param encryptKey key值 必须为16位
	 */
	public static String encrypt(String content, String encryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance(AES);
		kgen.init(128);
		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), AES));
		byte[] b = cipher.doFinal(content.getBytes("utf-8"));
		return Base64.encodeBase64String(b);
	}

	/**
	 * 解密
	 * @param encryptStr 解密的字符串
	 * @param decryptKey 解密的key值
	 */
	public static String decrypt(String encryptStr, String decryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance(AES);
		kgen.init(128);
		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), AES));
		byte[] encryptBytes = Base64.decodeBase64(encryptStr);
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		return new String(decryptBytes);
	}


}
