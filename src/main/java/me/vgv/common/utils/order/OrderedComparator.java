package me.vgv.common.utils.order;

import java.util.Comparator;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public final class OrderedComparator implements Comparator<Ordered> {

	private final boolean ascSort;

	public OrderedComparator() {
		this.ascSort = true;
	}

	public OrderedComparator(boolean ascSort) {
		this.ascSort = ascSort;
	}

	@Override
	public int compare(Ordered o1, Ordered o2) {
		int result = 0;

		if (o1 == o2) {
			result = 0;
		} else if (o1 == null) {
			result = 1;
		} else {
			result = Integer.valueOf(o1.getOrder()).compareTo(o2.getOrder());
		}

		if (!ascSort) {
			result *= -1;
		}

		return result;
	}
}
