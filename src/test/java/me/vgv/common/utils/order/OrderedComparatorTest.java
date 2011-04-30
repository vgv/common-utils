package me.vgv.common.utils.order;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class OrderedComparatorTest {

	@Test
	public void testAscSort() throws Exception {
		OrderedClass one = new OrderedClass(1);
		OrderedClass two = new OrderedClass(2);
		OrderedClass third = new OrderedClass(3);

		Set<OrderedClass> orderedClasses = new TreeSet<OrderedClass>(new OrderedComparator(true));
		orderedClasses.add(third);
		orderedClasses.add(one);
		orderedClasses.add(two);

		Assert.assertSame(one, orderedClasses.toArray()[0]);
		Assert.assertSame(two, orderedClasses.toArray()[1]);
		Assert.assertSame(third, orderedClasses.toArray()[2]);
	}

	@Test
	public void testDescSort() throws Exception {
		OrderedClass one = new OrderedClass(1);
		OrderedClass two = new OrderedClass(2);
		OrderedClass third = new OrderedClass(3);

		Set<OrderedClass> orderedClasses = new TreeSet<OrderedClass>(new OrderedComparator(false));
		orderedClasses.add(third);
		orderedClasses.add(one);
		orderedClasses.add(two);

		Assert.assertSame(one, orderedClasses.toArray()[2]);
		Assert.assertSame(two, orderedClasses.toArray()[1]);
		Assert.assertSame(third, orderedClasses.toArray()[0]);
	}

	@Test
	public void testCompareNull() throws Exception {
		OrderedComparator orderedComparator = new OrderedComparator();
		Assert.assertEquals(0, orderedComparator.compare(null, null));
	}
}

class OrderedClass implements Ordered {

	private final int order;

	OrderedClass(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return order;
	}
}