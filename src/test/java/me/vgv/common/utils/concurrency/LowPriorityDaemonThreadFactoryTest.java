package me.vgv.common.utils.concurrency;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class LowPriorityDaemonThreadFactoryTest {

	@Test
	public void testNewThread() throws Exception {
		LowPriorityDaemonThreadFactory threadFactory = new LowPriorityDaemonThreadFactory();

		Thread thread = threadFactory.newThread(RunnableUtils.EMPTY_RUNNABLE);
		Assert.assertEquals(thread.getPriority(), Thread.MIN_PRIORITY);
		Assert.assertTrue(thread.isDaemon());
	}

	@Test
	public void testNewThreadWithParams() throws Exception {
		String name = "TEST";

		LowPriorityDaemonThreadFactory threadFactory = new LowPriorityDaemonThreadFactory(name);

		Thread thread = threadFactory.newThread(RunnableUtils.EMPTY_RUNNABLE);
		Assert.assertEquals(thread.getName(), name);
		Assert.assertTrue(thread.isDaemon());
	}

}
