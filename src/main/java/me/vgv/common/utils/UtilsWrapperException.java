package me.vgv.common.utils;

/**
 * Никакие методы в утилитах не бросают checked exceptions, вместо этого любое
 * исключение "заворачивается" в UtilsWrapperException. Базовое исключение всегда содержится
 * в {@link UtilsWrapperException#getCause()}
 *
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public final class UtilsWrapperException extends RuntimeException {

	public UtilsWrapperException(String message, Throwable cause) {
		super(message, cause);
	}

}
