package me.vgv.common.utils.hash;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public final class HashOutputStream extends OutputStream {

	private final OutputStream outputStream;
	private final MessageDigest messageDigest;

	public HashOutputStream(OutputStream outputStream, HashAlgorithm algorithm) {
		this.outputStream = outputStream;
		this.messageDigest = algorithm.createMessageDigest();
	}

	public String getHash() {
		return HashAlgorithm.convertByteArrayToHexString(messageDigest.digest());
	}

	@Override
	public void write(int b) throws IOException {
		outputStream.write(b);
		messageDigest.update((byte) b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		outputStream.write(b);
		messageDigest.update(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		outputStream.write(b, off, len);
		messageDigest.update(b, off, len);
	}

	@Override
	public void flush() throws IOException {
		outputStream.flush();
	}

	@Override
	public void close() throws IOException {
		outputStream.close();
	}
}
