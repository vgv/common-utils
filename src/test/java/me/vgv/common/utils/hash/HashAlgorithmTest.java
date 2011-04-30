package me.vgv.common.utils.hash;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;
import me.vgv.common.utils.UtilsWrapperException;

import java.io.*;
import java.nio.CharBuffer;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class HashAlgorithmTest {

	@Test
	public void testGenerateHash() throws Exception {
		Assert.assertEquals("8b45e4bd1c6acb88bebf6407d16205f567e62a3e", HashAlgorithm.SHA1.generateHash("some string"));
	}

	@Test
	public void testGenerateHashFromInputStream() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("some string".getBytes("UTF-8"));
		Assert.assertEquals("8b45e4bd1c6acb88bebf6407d16205f567e62a3e", HashAlgorithm.SHA1.generateHash(inputStream));
	}

	@Test
	public void testGenerateHashFromInputStreamWithException() throws Exception {
		InputStream inputStream = Mockito.mock(InputStream.class);
		IOException ioException = new IOException();
		Mockito.when(inputStream.read()).thenThrow(ioException);
		Mockito.when(inputStream.read(Mockito.<byte[]>any())).thenThrow(ioException);
		Mockito.when(inputStream.read(Mockito.<byte[]>any(), Mockito.anyInt(), Mockito.anyInt())).thenThrow(ioException);

		try {
			HashAlgorithm.MD5.generateHash(inputStream);
			Assert.fail("Where is the exception?");
		} catch (UtilsWrapperException e) {
			Assert.assertSame(e.getCause(), ioException);
		}
	}

	@Test
	public void testGenerateHashFromReader() throws Exception {
		Reader reader = new StringReader("some string");
		Assert.assertEquals("8b45e4bd1c6acb88bebf6407d16205f567e62a3e", HashAlgorithm.SHA1.generateHash(reader));
	}

	@Test
	public void testGenerateHashFromReaderWithException() throws Exception {
		Reader reader = Mockito.mock(Reader.class);
		IOException ioException = new IOException();
		Mockito.when(reader.read()).thenThrow(ioException);
		Mockito.when(reader.read(Mockito.<CharBuffer>any())).thenThrow(ioException);
		Mockito.when(reader.read(Mockito.<char[]>any())).thenThrow(ioException);
		Mockito.when(reader.read(Mockito.<char[]>any(), Mockito.anyInt(), Mockito.anyInt())).thenThrow(ioException);

		try {
			HashAlgorithm.MD5.generateHash(reader);
			Assert.fail("Where is the exception?");
		} catch (UtilsWrapperException e) {
			Assert.assertSame(e.getCause(), ioException);
		}
	}

	@Test
	public void testGenerateHashFromFile() throws Exception {
		File file = File.createTempFile("commons_test_", "");
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write("some string".getBytes("UTF-8"));
		fileOutputStream.close();

		Assert.assertEquals("8b45e4bd1c6acb88bebf6407d16205f567e62a3e", HashAlgorithm.SHA1.generateHash(file));
	}

	@Test
	public void testGenerateHashFromByteArray() throws Exception {
		byte[] data = "some string".getBytes("UTF-8");
		Assert.assertEquals("8b45e4bd1c6acb88bebf6407d16205f567e62a3e", HashAlgorithm.SHA1.generateHash(data));
	}
}
