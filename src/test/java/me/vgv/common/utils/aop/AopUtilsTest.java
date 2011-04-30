package me.vgv.common.utils.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class AopUtilsTest {

	@Test
	public void testInvokeMethodWithError() throws Throwable {
		Throwable expectedException = new RuntimeException("mock test");

		MethodInvocation methodInvocation = Mockito.mock(MethodInvocation.class);
		Mockito.when(methodInvocation.proceed()).thenThrow(expectedException);
		Mockito.when(methodInvocation.getMethod()).thenReturn(Object.class.getDeclaredMethod("hashCode")); // любой вообще метод, это используется внутри для логгирования

		InvocationResult invocationResult = AopUtils.invokeMethod(methodInvocation);

		Assert.assertNull(invocationResult.getResult());
		Assert.assertEquals(invocationResult.getError(), expectedException);
	}

	@Test
	public void testInvokeMethod() throws Throwable {
		Object expectedResult = new Object();
		MethodInvocation methodInvocation = Mockito.mock(MethodInvocation.class);
		Mockito.when(methodInvocation.proceed()).thenReturn(expectedResult);
		Mockito.when(methodInvocation.getMethod()).thenReturn(Object.class.getDeclaredMethod("hashCode")); // любой вообще метод, это используется внутри для логгирования

		InvocationResult invocationResult = AopUtils.invokeMethod(methodInvocation);

		Assert.assertEquals(invocationResult.getResult(), expectedResult);
		Assert.assertNull(invocationResult.getError());
	}

	@Test
	public void testConvertMethodInvokation() throws Throwable {
		MethodInvocation methodInvocation = new MethodInvocation() {
			@Override
			public Method getMethod() {
				return null;
			}

			@Override
			public Object[] getArguments() {
				return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
			public Object proceed() throws Throwable {
				return 567;
			}

			@Override
			public Object getThis() {
				return null;
			}

			@Override
			public AccessibleObject getStaticPart() {
				return null;
			}
		};

		Callable<?> callable = AopUtils.convertMethodInvocation(methodInvocation);
		Object obj = callable.call();
		Assert.assertEquals(obj, 567);
	}

	@Test
	public void testConvertMethodInvokationIfException() throws Throwable {
		final Exception error = new Exception();
		MethodInvocation methodInvocation = new MethodInvocation() {
			@Override
			public Method getMethod() {
				return null;
			}

			@Override
			public Object[] getArguments() {
				return new Object[0];
			}

			@Override
			public Object proceed() throws Throwable {
				throw error;
			}

			@Override
			public Object getThis() {
				return null;
			}

			@Override
			public AccessibleObject getStaticPart() {
				return null;
			}
		};

		Callable<?> callable = AopUtils.convertMethodInvocation(methodInvocation);
		try {
			callable.call();
			Assert.fail("where is the exception?");
		} catch (Exception e) {
			Assert.assertSame(e, error);
		}
	}

	@Test
	public void testConvertMethodInvokationIfThrowable() throws Throwable {
		final Throwable error = new Throwable();
		MethodInvocation methodInvocation = new MethodInvocation() {
			@Override
			public Method getMethod() {
				return null;
			}

			@Override
			public Object[] getArguments() {
				return new Object[0];
			}

			@Override
			public Object proceed() throws Throwable {
				throw error;
			}

			@Override
			public Object getThis() {
				return null;
			}

			@Override
			public AccessibleObject getStaticPart() {
				return null;
			}
		};

		Callable<?> callable = AopUtils.convertMethodInvocation(methodInvocation);
		try {
			callable.call();
			Assert.fail("where is the exception?");
		} catch (Exception e) {
			Assert.assertSame(e.getCause(), error);
		}
	}

	@Test(expectedExceptions = RuntimeException.class)
	public void testProcessInvocationResultIfError() throws Throwable {
		InvocationResult invocationResult = InvocationResult.error(new RuntimeException());

		AopUtils.processInvocationResult(invocationResult);
	}

	@Test
	public void testProcessInvocationResult() throws Throwable {
		Object expectedResult = new Object();
		InvocationResult invocationResult = InvocationResult.ok(expectedResult);

		Object actualResult = AopUtils.processInvocationResult(invocationResult);
		Assert.assertEquals(expectedResult, actualResult);
		Assert.assertNull(invocationResult.getError());
	}

	@Test
	public void testProcessInvocationResultIfNullResult() throws Throwable {
		InvocationResult invocationResult = InvocationResult.ok(null);

		Object actualResult = AopUtils.processInvocationResult(invocationResult);
		Assert.assertNull(actualResult);
		Assert.assertNull(invocationResult.getResult());
		Assert.assertNull(invocationResult.getError());
	}

	@Test
	public void testGetTargetClass() {
		// test Guice-proxied lass
		class A {
			// это базовый класс

		}

		class B$$C extends A {
			// а это навернутый на него CGLIB-прокси
		}

		Assert.assertEquals(A.class, AopUtils.getTargetClass(B$$C.class));
	}
}
