package me.vgv.common.utils.context;

import me.vgv.common.utils.concurrency.InvokeUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public final class ContextUtils {

	private ContextUtils() {
	}

	public static <T> boolean isSameContext(Context<T> context, T value) {
		T contextValue = context.get();

		if (value == null) {
			return contextValue == null;
		} else {
			return value.equals(contextValue);
		}
	}

	public static <T> void invokeNow(Context<T> context, T value, Runnable code) {
		if (isSameContext(context, value)) {
			// если вызов уже в том контексте, который требуется - просто вызовем метод
			code.run();
		} else {
			// вызов в неинициализированном конексте - окружим вызов инициализацией и сбросом контекста
			try {
				context.set(value);
				code.run();
			} finally {
				context.set(null);
			}
		}
	}

	public static <T, V> V invokeNow(Context<T> context, T value, Callable<V> code) throws Exception {
		if (isSameContext(context, value)) {
			// если вызов уже в том контексте, который требуется - просто вызовем метод
			return code.call();
		} else {
			// вызов в неинициализированном конексте - окружим вызов инициализацией и сбросом контекста
			try {
				context.set(value);
				return code.call();
			} finally {
				context.set(null);
			}
		}
	}

	public static <T> void invokeAsync(final Context<T> context, final T value, final Runnable code) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				invokeNow(context, value, code);
			}
		};

		InvokeUtils.invokeAsync(runnable);
	}

	public static <T> void invokeAsync(final Context<T> context, final T value, final Runnable code, int millis) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				invokeNow(context, value, code);
			}
		};

		InvokeUtils.invokeAsync(runnable, millis);
	}

	public static <T, V> Future<V> invokeAsync(final Context<T> context, final T value, final Callable<V> code) {
		Callable<V> callable = new Callable<V>() {
			@Override
			public V call() throws Exception {
				return invokeNow(context, value, code);
			}
		};

		return InvokeUtils.invokeAsync(callable);
	}

	public static <T, V> Future<V> invokeAsync(final Context<T> context, final T value, final Callable<V> code, int millis) {
		Callable<V> callable = new Callable<V>() {
			@Override
			public V call() throws Exception {
				return invokeNow(context, value, code);
			}
		};

		return InvokeUtils.invokeAsync(callable, millis);
	}

}
