package me.vgv.common.utils.concurrency;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public final class RunnableUtils {

	private RunnableUtils() {
	}

	/**
	 * Пустой экземпляр Runnable, который не делает ничего
	 */
	public static final Runnable EMPTY_RUNNABLE = new Runnable() {
		@Override
		public void run() {
			// VOID
		}
	};

	/**
	 * Комбинирует несколько заданий в одно, которое выполняет все скомбинированные задания
	 * по очереди
	 *
	 * @param first  первое задание
	 * @param second второе задание
	 * @param other  остальные задания, если есть
	 * @return одно задание последовательно выполняющее все указанные задания
	 */
	public static Runnable combine(final Runnable first, final Runnable second, final Runnable... other) {
		if (first == null) {
			throw new NullPointerException("first runnable parameter must be not null");
		}
		if (second == null) {
			throw new NullPointerException("second runnable parameter must be not null");
		}

		// сконструируем новый объект
		return new Runnable() {
			@Override
			public void run() {
				first.run();
				second.run();
				for (Runnable command : other) {
					command.run();
				}
			}
		};
	}
}
