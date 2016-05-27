package com.fob.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * @author talaya
 */
public class MD5EncodeUtil {
	private final static String TAG = "MD5EncodeUtil";
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * convert byte array to hex string
	 * 
	 * @param b
	 * @return
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * convert byte to hex string
	 * 
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * get md5 encode of string
	 * 
	 * @param origin
	 *            String
	 * @return String
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static String MD5Encode(String origin) {
		if (origin == null)
			return null;
		String resultString;
		resultString = origin;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
		}
		resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		return resultString;
	}

	public static byte[] sha(String info) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("SHA");
			byte[] srcBytes = info.getBytes();
			md5.update(srcBytes);
			byte[] resultBytes = md5.digest();
			return resultBytes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info.getBytes();
	}

	/**
	 * 使用apche codec对数组进行encode
	 * 
	 * @param b
	 * @return
	 */
	public static String getenBASE64inCodec(byte[] b) {
		if (b == null)
			return null;
		return new String((new Base64()).encode(b));
	}

	public static String sign(String token, String timestamp, String nonce) {
		String source = token.concat("-").concat(timestamp).concat("-")
				.concat(nonce);
		return ZipWithZlib.getenBASE64inCodec(sha(source));
	}

}