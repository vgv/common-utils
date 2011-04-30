package me.vgv.common.utils.concurrency;

import java.util.concurrent.ThreadFactory;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public final class LowPriorityDaemonThreadFactory implements ThreadFactory {

	private final String threadName;

	public LowPriorityDaemonThreadFactory() {
		this("Low-priority daemon");
	}

	public LowPriorityDaemonThreadFactory(String threadName) {
		this.threadName = threadName;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r, threadName);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.setDaemon(true);
		return thread;
	}
}
