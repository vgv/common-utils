package me.vgv.common.utils.context;

/**
 * Общий интерфейс для контекстов любого типа
 * <p/>
 *
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public interface Context<T> {

	/**
	 * @return контекстное значение
	 */
	public T get();

	/**
	 * Устанавливает новое контекстное значение или сбрасывает его (null)
	 * <p/>
	 * Метод не позволяет изменять инициализировнный конекст,
	 * в этом случае будет брошено IllegalStateException
	 *
	 * @param newValue новое контекстное значение или null
	 */
	public void set(T newValue);

}
