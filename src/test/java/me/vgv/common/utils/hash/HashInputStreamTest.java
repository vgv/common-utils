package me.vgv.common.utils.hash;

import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class HashInputStreamTest {

	@Test
	public void testHashInputStream() throws Exception {
		HashInputStream hashInputStream = new HashInputStream(new ByteArrayInputStream("some string".getBytes("UTF-8")), HashAlgorithm.SHA1);
		hashInputStream.read(new byte[5]);
		hashInputStream.read(new byte[5], 0, 3);
		while (hashInputStream.read() != -1) ;

		Assert.assertEquals(HashAlgorithm.SHA1.generateHash("some string"), hashInputStream.getHash());
	}

	@Test
	public void testOtherWrappedMethods() throws Exception {
		InputStream inputStream = Mockito.mock(InputStream.class);
		HashInputStream hashInputStream = new HashInputStream(inputStream, HashAlgorithm.SHA1);

		hashInputStream.available();
		hashInputStream.close();
		hashInputStream.reset();
		hashInputStream.markSupported();
		hashInputStream.mark(1);
		hashInputStream.skip(2);

		InOrder inOrder = Mockito.inOrder(inputStream);
		inOrder.verify(inputStream, VerificationModeFactory.times(1)).available();
		inOrder.verify(inputStream, VerificationModeFactory.times(1)).close();
		inOrder.verify(inputStream, VerificationModeFactory.times(1)).reset();
		inOrder.verify(inputStream, VerificationModeFactory.times(1)).markSupported();
		inOrder.verify(inputStream, VerificationModeFactory.times(1)).mark(1);
		inOrder.verify(inputStream, VerificationModeFactory.times(1)).skip(2);
	}
}
