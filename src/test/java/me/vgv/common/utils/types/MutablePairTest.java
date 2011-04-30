package me.vgv.common.utils.types;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class MutablePairTest {

	@Test
	public void testSetters() {
		BigInteger first = new BigInteger("123123");
		Date second = new Date();

		MutablePair<BigInteger, Date> object = new MutablePair<BigInteger, Date>();
		object.setFirst(first).setSecond(second);

		Assert.assertSame(first, object.getFirst());
		Assert.assertSame(second, object.getSecond());
	}

	@Test
	public void testConstructor() {
		BigInteger first = new BigInteger("123123");
		Date second = new Date();

		MutablePair<BigInteger, Date> object = new MutablePair<BigInteger, Date>(first, second);

		Assert.assertSame(first, object.getFirst());
		Assert.assertSame(second, object.getSecond());
	}

	@Test
	public void testEqualityAndHashCode() {
		BigInteger first = new BigInteger("123123");
		Date second = new Date();

		MutablePair<BigInteger, Date> object1 = new MutablePair<BigInteger, Date>(first, second);
		MutablePair<BigInteger, Date> object2 = new MutablePair<BigInteger, Date>(first, second);

		Assert.assertEquals(object1, object2);
		Assert.assertEquals(object1.hashCode(), object2.hashCode());
	}


}
