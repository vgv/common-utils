package me.vgv.common.utils.types;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public final class MutableTriple<A, B, C> implements Triple<A, B, C> {

	private A first;
	private B second;
	private C third;

	public MutableTriple() {
	}

	public MutableTriple(A first, B second, C third) {
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

	public MutableTriple<A, B, C> setFirst(A first) {
		this.first = first;
		return this;
	}

	public MutableTriple<A, B, C> setSecond(B second) {
		this.second = second;
		return this;
	}

	public MutableTriple<A, B, C> setThird(C third) {
		this.third = third;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MutableTriple that = (MutableTriple) o;

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
