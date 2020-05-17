package com.javaxyq.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 Generator<br>
 * Modify by Kylixs <br>
 * 2007-1-22
 */
@Slf4j
public class MD5 {

	private final static MessageDigest md5;

	static {
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Computes the MD5 fingerprint of a string.
	 * 
	 * @return the MD5 digest of the input <code>String</code>
	 */
	public static String compute(String inStr) {
		return compute(inStr.getBytes());
	}

	@SneakyThrows
	public static String compute(byte[] byteArray) {
		// convert input String to a char[]
		// convert that char[] to byte[]
		// get the md5 digest as byte[]
		// bit-wise AND that byte[] with 0xff
		// prepend "0" to the output StringBuffer to make sure that we don't end
		// up with
		// something like "e21ff" instead of "e201ff"
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuilder hexValue = new StringBuilder();

		for (byte md5Byte : md5Bytes) {
			int val = ((int) md5Byte) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}
	public static void main(String[] args) {
		System.out.println(MD5.compute("Foxer"));
	}
}
