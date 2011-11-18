package me.vgv.common.utils.concurrency;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class InvokeUtilsTest {

	@Test
	public void testInvokeAsync() throws Exception {
		final long currentThreadId = Thread.currentThread().getId();
		final CountDownLatch latch = new CountDownLatch(1);

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				long invokeThreadId = Thread.currentThread().getId();
				// проверим что вызов ДЕЙСТВИТЕЛЬНО произошел из другого потока
				Assert.assertTrue(currentThreadId != invokeThreadId);
				latch.countDown();
			}
		};

		InvokeUtils.invokeAsync(runnable);
		latch.await(); // эта "защелка" нужна только для того, чтобы убедится что поток выполнился полностью
	}

	@Test
	public void testInvokeAsyncWithResult() throws Exception {
		final long currentThreadId = Thread.currentThread().getId();

		Callable<Integer> callable = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				long invokeThreadId = Thread.currentThread().getId();
				// проверим что вызов ДЕЙСТВИТЕЛЬНО произошел из другого потока
				Assert.assertTrue(currentThreadId != invokeThreadId);
				return 9999;
			}
		};

		Future<Integer> future = InvokeUtils.invokeAsync(callable);
		Assert.assertEquals(Integer.valueOf(9999), future.get());
	}

	@Test
	public void testScheduledInvokeAsync() throws Exception {
		final long currentThreadId = Thread.currentThread().getId();
		final CountDownLatch latch = new CountDownLatch(1);
		final long millis = 1000;
		final long start = System.currentTimeMillis();

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				long invokeThreadId = Thread.currentThread().getId();
				// проверим что вызов ДЕЙСТВИТЕЛЬНО произошел из другого потока
				Assert.assertTrue(currentThreadId != invokeThreadId);
				// проверим что поток действительно ждал не меньше чем указанная пауза
				long diff = System.currentTimeMillis() - start;
				Assert.assertTrue(diff >= millis);
				latch.countDown();
			}
		};

		InvokeUtils.invokeAsync(runnable, millis);
		latch.await(); // эта "защелка" нужна только для того, чтобы убедится что поток выполнился полностью
	}

	@Test
	public void testScheduledInvokeAsyncAndNonPositiveMillis() throws Exception {
		final long currentThreadId = Thread.currentThread().getId();
		final CountDownLatch latch = new CountDownLatch(1);
		final long start = System.currentTimeMillis();

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				long invokeThreadId = Thread.currentThread().getId();
				// проверим что вызов ДЕЙСТВИТЕЛЬНО произошел из другого потока
				Assert.assertTrue(currentThreadId != invokeThreadId);
				// проверим что поток действительно практически не ждал, что время меньше 50 миллисекунд
				// возможно, при выполнении на медленной машине этот тест будет проваливаться - тогда надо немного увеличить
				// количество миллисекунд
				long diff = System.currentTimeMillis() - start;
				Assert.assertTrue(diff < 50);
				latch.countDown();
			}
		};

		InvokeUtils.invokeAsync(runnable, 0);
		latch.await(); // эта "защелка" нужна только для того, чтобы убедится что поток выполнился полностью
	}

	@Test
	public void testScheduledInvokeAsyncWithResult() throws Exception {
		final long currentThreadId = Thread.currentThread().getId();
		final long millis = 1000;
		final long start = System.currentTimeMillis();

		Callable<Integer> callable = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				long invokeThreadId = Thread.currentThread().getId();
				// проверим что вызов ДЕЙСТВИТЕЛЬНО произошел из другого потока
				Assert.assertTrue(currentThreadId != invokeThreadId);
				// проверим что поток действительно ждал не меньше чем указанная пауза
				long diff = System.currentTimeMillis() - start;
				Assert.assertTrue(diff >= millis);

				return 9999;
			}
		};

		Future<Integer> future = InvokeUtils.invokeAsync(callable, millis);
		Assert.assertEquals(Integer.valueOf(9999), future.get());
	}

	@Test
	public void testScheduledInvokeAsyncWithResultAndNonPositiveMillis() throws Exception {
		final long currentThreadId = Thread.currentThread().getId();
		final long start = System.currentTimeMillis();

		Callable<Integer> callable = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				long invokeThreadId = Thread.currentThread().getId();
				// проверим что вызов ДЕЙСТВИТЕЛЬНО произошел из другого потока
				Assert.assertTrue(currentThreadId != invokeThreadId);
				// проверим что поток действительно практически не ждал, что время меньше 50 миллисекунд
				// возможно, при выполнении на медленной машине этот тест будет проваливаться - тогда надо немного увеличить
				// количество миллисекунд
				long diff = System.currentTimeMillis() - start;
				Assert.assertTrue(diff < 50);

				return 9999;
			}
		};

		Future<Integer> future = InvokeUtils.invokeAsync(callable, 0);
		Assert.assertEquals(Integer.valueOf(9999), future.get());
	}

	@Test
	public void testInvokeAsyncWithFixedDelay() throws Exception {
		final long currentThreadId = Thread.currentThread().getId();
		final CountDownLatch latch = new CountDownLatch(5);

		final long initialDelay = 3000;
		final long millis = 1500;
		final long sleepTime = 1000;

		final long start = System.currentTimeMillis();
		final List<Long> wakeups = new ArrayList<Long>();

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// проверим что вызов ДЕЙСТВИТЕЛЬНО произошел из другого потока
				long invokeThreadId = Thread.currentThread().getId();
				Assert.assertTrue(currentThreadId != invokeThreadId);

				// заснем
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// проверим что поток действительно ждал не меньше чем указанная пауза
				long lastWakeup;
				if (wakeups.isEmpty()) {
					lastWakeup = start;
				} else {
					lastWakeup = wakeups.get(wakeups.size() - 1); // возьмем последнюю метку
				}
				long diff = System.currentTimeMillis() - lastWakeup;
				Assert.assertTrue(diff >= (millis + sleepTime));

				// добавим текущую метку
				wakeups.add(System.currentTimeMillis());

				latch.countDown();
			}
		};

		InvokeUtils.invokeAsyncWithFixedDelay(runnable, initialDelay, millis);
		latch.await(); // эта "защелка" нужна только для того, чтобы убедится что задание выполнилось несколько (5) раз
		final long end = System.currentTimeMillis();

		Assert.assertTrue((end - start) >= (initialDelay + (sleepTime + millis) + (sleepTime + millis) + (sleepTime + millis) + (sleepTime + millis) + (sleepTime)));
	}

	@Test
	public void testInvokeAsyncAtFixedRate() throws Exception {
		final long currentThreadId = Thread.currentThread().getId();
		final CountDownLatch latch = new CountDownLatch(5);

		final long initialDelay = 3000;
		final long millis = 1500;
		final long sleepTime = 1000;

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// проверим что вызов ДЕЙСТВИТЕЛЬНО произошел из другого потока
				long invokeThreadId = Thread.currentThread().getId();
				Assert.assertTrue(currentThreadId != invokeThreadId);

				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				latch.countDown();
			}
		};

		final long start = System.currentTimeMillis();
		InvokeUtils.invokeAsyncAtFixedRate(runnable, initialDelay, millis);
		latch.await(); // эта "защелка" нужна только для того, чтобы убедится что задание выполнилось несколько (5) раз
		final long end = System.currentTimeMillis();

		Assert.assertTrue((end - start) >= (initialDelay + millis + millis + millis + millis + sleepTime));
	}

}
