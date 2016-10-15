package com.kaiery.common;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Des {

	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	private static byte[] ivkey = { 0x63, 0x79, 0x68, 0x67, 0x30, 0x32, 0x33, 0x34 };

	/**
	 * @param encryptString
	 *            需要加密的明文
	 * @param encryptKey
	 *            DES公钥
	 * @return 加密后的密文
	 * @throws Exception
	 */
	public static String encryptDES(String encryptString, String encryptKey)
			throws Exception {
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		IvParameterSpec zeroIv = new IvParameterSpec(ivkey);
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
		return new String(Base64.encode(encryptedData));
	}

	/****
	 * @param decrypString
	 *            密文
	 * @param decryptKey
	 *            DES公钥
	 * @return 解密后的明文
	 * @throws Exception
	 */
	public static String decryptDES(String decrypString, String decryptKey)
			throws Exception {
		byte[] byteMi = Base64.decode(decrypString);
		IvParameterSpec zeroIv = new IvParameterSpec(ivkey);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte[] decryptedData = cipher.doFinal(byteMi);
		return new String(decryptedData);
	}

	public static void main(String[] args) throws Exception {
		String publicKey = "abcdefgh";
		System.out.println(Des.encryptDES("123大放送|+_)(*&^%$#@!~?/><.,", publicKey));
		System.out.println(Des.decryptDES(Des.encryptDES("123大放送|+_)(*&^%$#@!~?/><.,", publicKey), publicKey));
	}
}
