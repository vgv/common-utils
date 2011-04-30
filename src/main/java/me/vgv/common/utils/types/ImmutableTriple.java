package me.vgv.common.utils.types;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public final class ImmutableTriple<A, B, C> implements Triple<A, B, C> {

	private final A first;
	private final B second;
	private final C third;

	public ImmutableTriple(A first, B second, C third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	@Override
	public A getFirst() {
		return first;
	}

	@Override
	public B getSecond() {
		return second;
	}

	@Override
	public C getThird() {
		return third;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ImmutableTriple that = (ImmutableTriple) o;

		if (first != null ? !first.equals(that.first) : that.first != null) return false;
		if (second != null ? !second.equals(that.second) : that.second != null) return false;
		if (third != null ? !third.equals(that.third) : that.third != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = first != null ? first.hashCode() : 0;
		result = 31 * result + (second != null ? second.hashCode() : 0);
		result = 31 * result + (third != null ? third.hashCode() : 0);
		return result;
	}
}
