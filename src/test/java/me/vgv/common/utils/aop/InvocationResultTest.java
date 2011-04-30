package me.vgv.common.utils.aop;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class InvocationResultTest {

	@Test
	public void testOk() throws Exception {
		Object result = new Object();
		InvocationResult invocationResult = InvocationResult.ok(result);

		Assert.assertSame(result, invocationResult.getResult());
		Assert.assertNull(invocationResult.getError());
	}

	@Test
	public void testError() throws Exception {
		Exception error = new Exception();
		InvocationResult invocationResult = InvocationResult.error(error);

		Assert.assertSame(error, invocationResult.getError());
		Assert.assertNull(invocationResult.getResult());
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testErrorWithNull() throws Exception {
		InvocationResult invocationResult = InvocationResult.error(null);
	}

}
