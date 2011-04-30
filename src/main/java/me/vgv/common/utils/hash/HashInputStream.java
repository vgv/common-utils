package me.vgv.common.utils.hash;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public final class HashInputStream extends InputStream {

	private final InputStream inputStream;
	private final MessageDigest messageDigest;

	public HashInputStream(InputStream inputStream, HashAlgorithm algorithm) {
		this.inputStream = inputStream;
		this.messageDigest = algorithm.createMessageDigest();
	}

	public String getHash() {
		return HashAlgorithm.convertByteArrayToHexString(messageDigest.digest());
	}

	@Override
	public int read() throws IOException {
		int value = inputStream.read();

		if (value != -1) {
			messageDigest.update((byte) value);
		}

		return value;
	}

	@Override
	public int read(byte[] b) throws IOException {
		int readBytes = inputStream.read(b);

		if (readBytes != -1) {
			messageDigest.update(b, 0, readBytes);
		}

		return readBytes;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int readBytes = inputStream.read(b, off, len);

		if (readBytes != -1) {
			messageDigest.update(b, off, readBytes);
		}

		return readBytes;
	}

	@Override
	public long skip(long n) throws IOException {
		return inputStream.skip(n);
	}

	@Override
	public int available() throws IOException {
		return inputStream.available();
	}

	@Override
	public void close() throws IOException {
		inputStream.close();
	}

	@Override
	public void mark(int readlimit) {
		inputStream.mark(readlimit);
	}

	@Override
	public void reset() throws IOException {
		inputStream.reset();
	}

	@Override
	public boolean markSupported() {
		return inputStream.markSupported();
	}
}
