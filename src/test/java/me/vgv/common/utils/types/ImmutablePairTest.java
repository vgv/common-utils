package me.vgv.common.utils.types;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class ImmutablePairTest {

	@Test
	public void testConstructor() {
		BigInteger first = new BigInteger("123123");
		Date second = new Date();

		ImmutablePair<BigInteger, Date> object = new ImmutablePair<BigInteger, Date>(first, second);

		Assert.assertSame(first, object.getFirst());
		Assert.assertSame(second, object.getSecond());
	}

	@Test
	public void testEqualityAndHashCode() {
		BigInteger first = new BigInteger("123123");
		Date second = new Date();

		ImmutablePair<BigInteger, Date> object1 = new ImmutablePair<BigInteger, Date>(first, second);
		ImmutablePair<BigInteger, Date> object2 = new ImmutablePair<BigInteger, Date>(first, second);

		Assert.assertEquals(object1, object2);
		Assert.assertEquals(object1.hashCode(), object2.hashCode());
	}
}
