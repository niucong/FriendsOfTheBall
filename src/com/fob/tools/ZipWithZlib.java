package com.fob.tools;

import java.io.ByteArrayOutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

import org.apache.commons.codec.binary.Base64;

public class ZipWithZlib {

	/**
	 * 压缩字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String compressData(String data) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DeflaterOutputStream zos = new DeflaterOutputStream(bos);
			zos.write(data.getBytes());
			zos.close();
			return new String(getenBASE64inCodec(bos.toByteArray()));
		} catch (Exception ex) {
			ex.printStackTrace();
			return "ZIP_ERR";
		}
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

	/**
	 * base64转码为string
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] getdeBASE64inCodec(String s) {
		if (s == null)
			return null;
		return new Base64().decode(s.getBytes());
	}

	/**
	 * 解码字符串
	 * 
	 * @param encdata
	 * @return
	 */
	public String decompressData(String encdata) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			InflaterOutputStream zos = new InflaterOutputStream(bos);
			zos.write(getdeBASE64inCodec(encdata));
			// byte [] b = encdata.getBytes();
			// int len = b.length;
			// zos.write(b, 0, len);
			// zos.write(getdeBASE64(encdata.getBytes()),0,(encdata.getBytes()).length);
			zos.close();
			return new String(bos.toByteArray());
		} catch (Exception ex) {
			ex.printStackTrace();
			return "UNZIP_ERR";
		}
	}
}
