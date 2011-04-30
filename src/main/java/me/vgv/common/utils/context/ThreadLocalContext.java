package me.vgv.common.utils.context;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class ThreadLocalContext<T> implements Context<T> {

	private final ThreadLocal<T> storage = new ThreadLocal<T>();

	@Override
	public final T get() {
		return storage.get();
	}

	@Override
	public final void set(T newValue) {
		T oldValue = get();

		if (newValue == null) {
			// это, фактически, обнуление контекста, можно делать всегда
			storage.set(null);
		} else if (oldValue == null) {
			// это инициализация пустого контекста, тоже можно делать всегда
			storage.set(newValue);
		} else if (!newValue.equals(oldValue)) {
			// а вот это попытка переинициализировать контекст другим значением, это защита от ошибок
			// программирования, поэтому в таком случае бросаем неконтролируемое исключение
			throw new IllegalStateException("you are trying to reinitialize not empty context");
		}
	}
}
