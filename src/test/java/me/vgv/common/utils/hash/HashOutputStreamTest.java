package me.vgv.common.utils.hash;

import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class HashOutputStreamTest {

	@Test
	public void testHashOutputStream() throws Exception {
		HashOutputStream hashOutputStream = new HashOutputStream(new ByteArrayOutputStream(), HashAlgorithm.SHA1);
		hashOutputStream.write("a".getBytes("UTF-8"));
		hashOutputStream.write('b');
		hashOutputStream.write("c".getBytes("UTF-8"), 0, 1);

		Assert.assertEquals(HashAlgorithm.SHA1.generateHash("abc"), hashOutputStream.getHash());
	}

	@Test
	public void testOtherWrappedMethods() throws Exception {
		OutputStream outputStream = Mockito.mock(OutputStream.class);
		HashOutputStream hashOutputStream = new HashOutputStream(outputStream, HashAlgorithm.SHA1);

		hashOutputStream.flush();
		hashOutputStream.close();

		InOrder inOrder = Mockito.inOrder(outputStream);
		inOrder.verify(outputStream, VerificationModeFactory.times(1)).flush();
		inOrder.verify(outputStream, VerificationModeFactory.times(1)).close();
	}

}
