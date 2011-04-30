package me.vgv.common.utils.concurrency;

import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.testng.annotations.Test;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class RunnableUtilsTest {

	@Test
	public void testCombine() throws Exception {
		Runnable first = Mockito.mock(Runnable.class);
		Runnable second = Mockito.mock(Runnable.class);
		Runnable third = Mockito.mock(Runnable.class);
		Runnable fourth = Mockito.mock(Runnable.class);

		Runnable combined = RunnableUtils.combine(first, second, third, fourth);
		combined.run();

		Mockito.verify(first, VerificationModeFactory.times(1)).run();
		Mockito.verify(second, VerificationModeFactory.times(1)).run();
		Mockito.verify(third, VerificationModeFactory.times(1)).run();
		Mockito.verify(fourth, VerificationModeFactory.times(1)).run();
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void testNullInFirstArgument() throws Exception {
		RunnableUtils.combine(null, RunnableUtils.EMPTY_RUNNABLE, RunnableUtils.EMPTY_RUNNABLE);
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void testNullInSecondArgument() throws Exception {
		RunnableUtils.combine(RunnableUtils.EMPTY_RUNNABLE, null, RunnableUtils.EMPTY_RUNNABLE);
	}

	@Test
	public void testNullInThirdArgument() throws Exception {
		Runnable first = Mockito.mock(Runnable.class);
		Runnable second = Mockito.mock(Runnable.class);

		Runnable combined = RunnableUtils.combine(first, second, (Runnable) null);
		combined.run();

		Mockito.verify(first, VerificationModeFactory.times(1)).run();
		Mockito.verify(second, VerificationModeFactory.times(1)).run();
	}
}
