package me.vgv.common.utils.types;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class MutableTripleTest {

	@Test
	public void testSetters() {
		Object first = new Object();
		BigInteger second = new BigInteger("34");
		Date third = new Date();

		MutableTriple<Object, BigInteger, Date> object = new MutableTriple<Object, BigInteger, Date>();
		object.setFirst(first).setSecond(second).setThird(third);

		Assert.assertSame(first, object.getFirst());
		Assert.assertSame(second, object.getSecond());
		Assert.assertSame(third, object.getThird());
	}

	@Test
	public void testConstructor() {
		Object first = new Object();
		BigInteger second = new BigInteger("34");
		Date third = new Date();

		MutableTriple<Object, BigInteger, Date> object = new MutableTriple<Object, BigInteger, Date>(first, second, third);

		Assert.assertSame(first, object.getFirst());
		Assert.assertSame(second, object.getSecond());
		Assert.assertSame(third, object.getThird());
	}

	@Test
	public void testEqualityAndHashCode() {
		Object first = new Object();
		BigInteger second = new BigInteger("34");
		Date third = new Date();

		MutableTriple<Object, BigInteger, Date> object1 = new MutableTriple<Object, BigInteger, Date>(first, second, third);
		MutableTriple<Object, BigInteger, Date> object2 = new MutableTriple<Object, BigInteger, Date>(first, second, third);

		Assert.assertEquals(object1, object2);
		Assert.assertEquals(object1.hashCode(), object2.hashCode());
	}
}
