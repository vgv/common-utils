package me.vgv.common.utils.context;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class ThreadLocalContextTest {

	@Test
	public void testGetValue() throws Exception {
		final Context<Object> context = new ThreadLocalContext<Object>();
		Object value = new Object();
		context.set(value);

		Assert.assertSame(value, context.get());
		Thread thread = new Thread() {
			@Override
			public void run() {
				Assert.assertNull(context.get());
			}
		};
		thread.start();
		thread.join();
	}

	@Test
	public void testSetValue() throws Exception {
		final Context<Object> context = new ThreadLocalContext<Object>();
		final Object value = new Object();
		context.set(value); // в этом потоке присвоим одно значение

		Thread thread = new Thread() {
			@Override
			public void run() {
				context.set(new Object()); // а в другом потоке - другое
			}
		};
		thread.start();
		thread.join();

		Assert.assertSame(value, context.get()); // проверим, что в контексте правильное значение для этого потока
	}

	@Test
	public void testSetSameValueInInitializedContext() throws Exception {
		final Context<Integer> context = new ThreadLocalContext<Integer>();
		context.set(new Integer(9999)); // присвоим два равных значения контексту, ошибки быть не должно
		context.set(new Integer(9999));

		Assert.assertEquals(new Integer(9999), context.get());
	}

	@Test
	public void testSetOtherValueInInitializedContext() throws Exception {
		final Context<Object> context = new ThreadLocalContext<Object>();
		final Object value = new Object();
		context.set(value);

		try {
			context.set(new Object());
			Assert.fail("where is the exception?");
		} catch (IllegalStateException e) {
			// 
		}

		Assert.assertSame(value, context.get());
	}

}
