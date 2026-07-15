package com.vmfg.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AESEncryptor {
	private static final Logger logger = LoggerFactory.getLogger(AESEncryptor.class);
	
	private static final String key = "!ndiaVMFG!ndia18"; // 128 bit key
	private static final String initVector =  "VlogixTechChenna"; // 16 bytes IV

	public static String encrypt(String value) {
		logger.info("<------------------encrypt--------------------Method Start--------------------------------->");
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());

			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			logger.error("<------------------encrypt----Method Exception--------------------------------->" + ex);

		}
		logger.info("<------------------encrypt--------------------Method end--------------------------------->");
		return null;
	}

	public static String decrypt(String encrypted) {
		logger.info("<------------------decrypt--------------------Method Start--------------------------------->");
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			logger.info("<------------------decrypt--------------------Method 1--------------------------------->");
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			logger.info("<------------------decrypt--------------------Method Start2--------------------------------->");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			logger.info("<------------------decrypt--------------------Method Start3--------------------------------->");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			logger.info("<------------------decrypt--------------------Method Start4--------------------------------->");
			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
			logger.info("<------------------decrypt--------------------Method Start5--------------------------------->");
			return new String(original);
		} catch (Exception ex) {
			logger.error("<------------------decrypt----Method Exception--------------------------------->" + ex);

		}
		logger.info("<------------------decrypt--------------------Method end--------------------------------->");
		return null;
	}

//	public static void main(String[] args) {
//		String key = "!ndiaVMFG!ndia18"; // 128 bit key
//		String initVector = "VlogixTechChenna"; // 16 bytes IV
//	}

}
