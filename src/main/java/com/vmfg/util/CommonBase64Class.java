package com.vmfg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonBase64Class {
	private static final Logger logger = LoggerFactory.getLogger(CommonBase64Class.class);
	
	public static String getBasesBinary(File file) {
		logger.info("CommonBase64Class getBasesBinary method Start");
		String encodedfile = null;
		FileInputStream fileInputStreamReader=null;
		try {
			fileInputStreamReader = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fileInputStreamReader.read(bytes);
			encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
			fileInputStreamReader.close();
		} catch (FileNotFoundException e) {
			logger.error("CommonBase64Class getBasesBinary Method FileNotFoundException---->" + e);
		} catch (Exception e) {
			logger.error("CommonBase64Class getBasesBinary Method Exception---->" + e);
		}
		finally {
			try {
				fileInputStreamReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("CommonBase64Class getBasesBinary method End");
		return encodedfile;
	}

}
