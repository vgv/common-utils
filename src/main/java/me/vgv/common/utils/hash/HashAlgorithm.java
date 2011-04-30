package me.vgv.common.utils.hash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import me.vgv.common.utils.CloseUtils;
import me.vgv.common.utils.UtilsWrapperException;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Доступные алгоритмы хеширования
 *
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public enum HashAlgorithm {

	MD2("MD2"),

	MD5("MD5"),

	SHA1("SHA-1"),

	SHA256("SHA-256"),

	SHA384("SHA-384"),

	SHA512("SHA-512");

	private static final Logger log = LoggerFactory.getLogger(HashAlgorithm.class);

	private final String algorithmName;

	HashAlgorithm(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public String generateHash(String str) {
		return generateHash(str, "UTF-8");
	}

	public String generateHash(String str, String encoding) {
		return generateHash(convertStringToBytes(str, encoding));
	}

	public String generateHash(InputStream inputStream) {
		try {
			MessageDigest messageDigest = this.createMessageDigest();
			int readBytes;
			byte[] buffer = new byte[4096]; // 4kb
			while ((readBytes = inputStream.read(buffer)) != -1) {
				messageDigest.update(buffer, 0, readBytes);
			}

			byte[] digest = messageDigest.digest();
			return convertByteArrayToHexString(digest);
		} catch (IOException e) {
			log.error("can't calculate " + this + " hash", e);
			throw new UtilsWrapperException("can't calculate " + this + " hash", e);
		}
	}

	public String generateHash(byte[] data) {
		MessageDigest messageDigest = this.createMessageDigest();
		messageDigest.update(data);
		byte[] digest = messageDigest.digest();
		return convertByteArrayToHexString(digest);
	}

	public String generateHash(File file) {
		FileInputStream fileInputStream = null;

		try {

			fileInputStream = new FileInputStream(file);
			return generateHash(fileInputStream);

		} catch (IOException e) {
			log.error("can't calculate " + this + " hash - I/O exception", e);
			throw new UtilsWrapperException("can't calculate " + this + " hash - I/O exception", e);
		} finally {
			CloseUtils.close(fileInputStream);
		}
	}

	public String generateHash(Reader reader) {
		return generateHash(reader, "UTF-8");
	}

	public String generateHash(Reader reader, String encoding) {
		try {

			MessageDigest messageDigest = this.createMessageDigest();
			int readChars;
			char[] buffer = new char[4096];
			while ((readChars = reader.read(buffer)) != -1) {
				String str = new String(buffer, 0, readChars);
				messageDigest.update(convertStringToBytes(str, encoding));
			}

			byte[] digest = messageDigest.digest();
			return convertByteArrayToHexString(digest);

		} catch (IOException e) {
			log.error("can't calculate " + this + " hash - I/O exception", e);
			throw new UtilsWrapperException("can't calculate " + this + " hash - I/O exception", e);
		}
	}

	public MessageDigest createMessageDigest() {
		try {
			return MessageDigest.getInstance(algorithmName);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Hmm, do you use unknown algorithm?", e);
		}
	}

	private static byte[] convertStringToBytes(String str, String encoding) {
		try {
			return str.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Hmm, selected encoding " + encoding + " doesn't support");
		}
	}

	public static String convertByteArrayToHexString(byte[] bytes) {
		StringBuilder stringBuilder = new StringBuilder(64);
		for (byte aByte : bytes) {
			int value = aByte & 0xFF;
			if (value < 0x10) {
				stringBuilder.append("0");
			}
			stringBuilder.append(Integer.toHexString(value));
		}

		return stringBuilder.toString().toLowerCase();
	}

}
