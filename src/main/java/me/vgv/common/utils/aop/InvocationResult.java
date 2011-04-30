package me.vgv.common.utils.aop;

/**
 * Результат вызова метода
 * Метод содержит результат вызова метода - значение или исключение
 *
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public final class InvocationResult {

	private final Object result;
	private final Throwable error;

	private InvocationResult(Object result, Throwable error) {
		this.result = result;
		this.error = error;
	}

	public Object getResult() {
		return result;
	}

	public Throwable getError() {
		return error;
	}

	public static InvocationResult ok(Object result) {
		return new InvocationResult(result, null);
	}

	public static InvocationResult error(Throwable error) {
		if (error == null) {
			throw new IllegalArgumentException("error is null");
		}
		return new InvocationResult(null, error);
	}

}
