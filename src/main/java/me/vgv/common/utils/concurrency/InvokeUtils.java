package me.vgv.common.utils.concurrency;

import java.lang.management.ManagementFactory;
import java.util.concurrent.*;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public final class InvokeUtils {

	private InvokeUtils() {
	}

	private static final int AVAILABLE_PROCESSORS;

	static {
		int processors = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
		if (processors < 4) {
			// даже если выполнение происходит на однопроцессорной машине - все равно будем создавать несколько рабочих потоков
			processors = 4;
		}

		AVAILABLE_PROCESSORS = processors;
	}

	// для выполнения задач, которые надо выполнить сейчас
	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(AVAILABLE_PROCESSORS, new LowPriorityDaemonThreadFactory("CA-daemon"));
	// для выполнения отложенных задач
	private static final ScheduledExecutorService SCHEDULED_EXECUTOR = Executors.newScheduledThreadPool(AVAILABLE_PROCESSORS, new LowPriorityDaemonThreadFactory("CSA-daemon"));

	/**
	 * Добавляет задание task в очередь на выполнение в параллельном потоке. Если очередь
	 * загружена не полностью - выполнение задания начинается немедленно.
	 *
	 * @param task задание
	 */
	public static void invokeAsync(final Runnable task) {
		EXECUTOR.submit(task);
	}

	/**
	 * Добавляет отложенное задание task в очередь на выполнение в параллельном потоке. Задание начнет
	 * выполняться через millis миллисекунд или немедленно, если millis меньше или равно 0
	 *
	 * @param task   задание
	 * @param millis через какое количество миллисекунд начать выполнять задание, если
	 *               меньше или равно 0 - задание начинает выполняться немедленно
	 */
	public static void invokeAsync(final Runnable task, long millis) {
		if (millis <= 0) {
			EXECUTOR.execute(task);
		} else {
			SCHEDULED_EXECUTOR.schedule(task, millis, TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * Добавляет задание task в очередь на выполнение в параллельном потоке и возвращает результат.
	 * Если очередь загружена не полностью - выполнение задания начинается немедленно.
	 *
	 * @param task задание
	 * @return объект типа Future с помощью которого можно рулить отложенным заданием
	 */
	public static <V> Future<V> invokeAsync(final Callable<V> task) {
		return EXECUTOR.submit(task);
	}

	/**
	 * Добавляет задание task в очередь на выполнение в параллельном потоке и возвращает результат.
	 * Задание начнет выполняться через millis миллисекунд или немедленно, если millis меньше или равно 0
	 *
	 * @param task   задание
	 * @param millis через какое количество миллисекунд начать выполнять задание, если
	 *               меньше или равно 0 - задание начинает выполняться немедленно
	 * @return объект типа Future с помощью которого можно рулить отложенным заданием
	 */
	public static <V> Future<V> invokeAsync(final Callable<V> task, long millis) {
		if (millis <= 0) {
			return EXECUTOR.submit(task);
		} else {
			return SCHEDULED_EXECUTOR.schedule(task, millis, TimeUnit.MILLISECONDS);
		}
	}

	public static void invokeAsyncWithFixedDelay(final Runnable task, long initialDelayMillis, long delayMillis) {
		SCHEDULED_EXECUTOR.scheduleWithFixedDelay(task, initialDelayMillis, delayMillis, TimeUnit.MILLISECONDS);
	}

	public static void invokeAsyncAtFixedRate(final Runnable task, long initialDelayMillis, long periodMillis) {
		SCHEDULED_EXECUTOR.scheduleAtFixedRate(task, initialDelayMillis, periodMillis, TimeUnit.MILLISECONDS);
	}

}
