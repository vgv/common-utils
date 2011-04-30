package me.vgv.common.utils.context;

import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class ContextUtilsTest {

	private Context<Object> context = new ThreadLocalContext<Object>();
	private Object value = new Object();

	@DataProvider(name = "is-same-context-provider")
	private Object[][] forIsSameContextProvider() {
		Object obj = new Object();
		return new Object[][]{
				{7777, 7777, true},
				{new Object(), new Object(), false},
				{null, null, true},
				{obj, obj, true},
				{null, new Object(), false},
				{new Object(), null, false},
				{Boolean.FALSE, Boolean.FALSE, true}
		};
	}

	@Test(dataProvider = "is-same-context-provider")
	public void testIsSameContext(Object contextValue, Object value, boolean answer) {
		Context<Object> context = new ThreadLocalContext<Object>();
		context.set(contextValue);

		Assert.assertEquals(answer, ContextUtils.isSameContext(context, value));
	}

	@Test
	public void testInvokeNowWithRunnable() throws Exception {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Assert.assertSame(context.get(), value);
			}
		};

		Assert.assertNull(context.get());
		ContextUtils.invokeNow(context, value, runnable);
		Assert.assertNull(context.get());
	}

	@Test
	public void testInvokeNowWithRunnableInSameContext() throws Exception {
		@SuppressWarnings({"unchecked"})
		final Context<Object> ctx = Mockito.mock(Context.class);
		final Object contextValue = new Object();
		Mockito.when(ctx.get()).thenReturn(contextValue);

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Assert.assertSame(ctx.get(), contextValue);
			}
		};

		Assert.assertNotNull(ctx.get());
		ContextUtils.invokeNow(ctx, contextValue, runnable);
		Assert.assertNotNull(ctx.get());
		Mockito.verify(ctx, VerificationModeFactory.times(0)).set(Mockito.<Object>any());
	}


	@Test
	public void testInvokeNowWithCallable() throws Exception {
		final Object result = new Object();
		Callable<Object> callable = new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				Assert.assertSame(context.get(), value);
				return result;
			}
		};

		Assert.assertNull(context.get());
		Object actualResult = ContextUtils.invokeNow(context, value, callable);
		Assert.assertSame(result, actualResult);
		Assert.assertNull(context.get());
	}

	@Test
	public void testInvokeNowWithCallableInSameContext() throws Exception {
		@SuppressWarnings({"unchecked"})
		final Context<Object> ctx = Mockito.mock(Context.class);
		final Object contextValue = new Object();
		Mockito.when(ctx.get()).thenReturn(contextValue);

		final Object result = new Object();
		Callable<Object> callable = new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				Assert.assertSame(ctx.get(), contextValue);
				return result;
			}
		};

		Assert.assertNotNull(ctx.get());
		Object actualResult = ContextUtils.invokeNow(ctx, contextValue, callable);
		Assert.assertSame(result, actualResult);
		Assert.assertNotNull(ctx.get());
		Mockito.verify(ctx, VerificationModeFactory.times(0)).set(Mockito.<Object>any());
	}


	@Test
	public void testInvokeAsyncWithRunnable() throws Exception {
		final long currentThread = Thread.currentThread().getId();
		final AtomicBoolean completed = new AtomicBoolean(false);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Assert.assertSame(context.get(), value);
				Assert.assertTrue(currentThread != Thread.currentThread().getId());
				completed.set(true);
			}
		};

		Assert.assertNull(context.get());

		ContextUtils.invokeAsync(context, value, runnable);
		while (!completed.get()) ;

		Assert.assertNull(context.get());
	}

	@Test
	public void testInvokeAsyncWithCallable() throws Exception {
		final Object result = new Object();
		final long currentThread = Thread.currentThread().getId();
		Callable<Object> callable = new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				Thread.sleep(100);
				Assert.assertSame(context.get(), value);
				Assert.assertTrue(currentThread != Thread.currentThread().getId());
				return result;
			}
		};

		Assert.assertNull(context.get());

		Future<Object> future = ContextUtils.invokeAsync(context, value, callable);

		Assert.assertFalse(future.isDone());
		Assert.assertSame(result, future.get());
		Assert.assertNull(context.get());
	}

	@Test
	public void testInvokeAsyncWithRunnableAndTimeout() throws Exception {
		final long currentThread = Thread.currentThread().getId();
		final AtomicBoolean completed = new AtomicBoolean(false);
		int delay = 1000;
		long currentTime = System.currentTimeMillis();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Assert.assertSame(context.get(), value);
				Assert.assertTrue(currentThread != Thread.currentThread().getId());
				completed.set(true);
			}
		};

		Assert.assertNull(context.get());

		ContextUtils.invokeAsync(context, value, runnable, delay);
		while (!completed.get()) ;

		Assert.assertTrue(System.currentTimeMillis() - currentTime >= delay);
		Assert.assertNull(context.get());
	}

	@Test
	public void testInvokeAsyncWithCallableAndTimeout() throws Exception {
		final Object result = new Object();
		final long currentThread = Thread.currentThread().getId();
		int delay = 1000;
		long currentTime = System.currentTimeMillis();
		Callable<Object> callable = new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				Assert.assertSame(context.get(), value);
				Assert.assertTrue(currentThread != Thread.currentThread().getId());
				return result;
			}
		};

		Assert.assertNull(context.get());

		Future<Object> future = ContextUtils.invokeAsync(context, value, callable, delay);

		Assert.assertFalse(future.isDone());
		Assert.assertSame(result, future.get());
		Assert.assertTrue(System.currentTimeMillis() - currentTime >= delay);
		Assert.assertNull(context.get());
	}
}
