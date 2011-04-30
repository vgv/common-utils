package me.vgv.common.utils.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Утилитные AOP-методы
 *
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public final class AopUtils {

	private static final Logger log = LoggerFactory.getLogger(AopUtils.class);

	private AopUtils() {
	}

	private static final String CGLIB_PROXY_CLASS_PART = "$$";

	/**
	 * Вызов метода и сохранение параметров вызова в объекте InvocationResult
	 *
	 * @param methodInvocation вызываемый метод
	 * @return результат вызова метода
	 */
	public static InvocationResult invokeMethod(final MethodInvocation methodInvocation) {
		try {
			return InvocationResult.ok(methodInvocation.proceed());
		} catch (Throwable e) {
			return InvocationResult.error(e);
		}
	}

	/**
	 * Конвертирует отложенный вызов в виде MethodInvocation в вид Callable
	 *
	 * @param methodInvocation отложенный вызов
	 * @return отложенный вызов в виде Callable
	 */
	public static Callable<Object> convertMethodInvocation(final MethodInvocation methodInvocation) {
		return new Callable<Object>() {
			@Override
			public Object call() throws java.lang.Exception {
				try {
					return methodInvocation.proceed();
				} catch (Exception e) {
					throw e;
				} catch (Throwable throwable) {
					// callable не может кидать throwable, а MethodInvocation может,
					// поэтому за-chain-им это исключение
					throw new Exception(throwable);
				}
			}
		};
	}

	/**
	 * Обрабатывает результаты вызова метода, возвращая либо результат, либо бросая исключение
	 *
	 * @param invocationResult результаты вызова метода
	 * @return результат
	 * @throws Throwable если в результате было брошено исключение
	 */
	public static Object processInvocationResult(final InvocationResult invocationResult) throws Throwable {
		if (invocationResult.getError() != null) {
			throw invocationResult.getError();
		} else {
			return invocationResult.getResult();
		}
	}

	/**
	 * Метод "докапывается" до настоящего класса объекта, отбрасывая все
	 * сгенерированные прокси
	 *
	 * @param clazz класс
	 * @return класс, очищенный от CGLIB-шелухи
	 */
	public static Class<?> getTargetClass(Class<?> clazz) {
		while (clazz.getName().contains(CGLIB_PROXY_CLASS_PART)) {
			clazz = clazz.getSuperclass();
		}

		return clazz;
	}
}
