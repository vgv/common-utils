package me.vgv.common.utils.types;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public final class MutablePair<A, B> implements Pair<A, B> {

	private A first;
	private B second;

	public MutablePair() {
	}

	public MutablePair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public A getFirst() {
		return first;
	}

	@Override
	public B getSecond() {
		return second;
	}

	public MutablePair<A, B> setFirst(A first) {
		this.first = first;
		return this;
	}

	public MutablePair<A, B> setSecond(B second) {
		this.second = second;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MutablePair that = (MutablePair) o;

		if (first != null ? !first.equals(that.first) : that.first != null) return false;
		if (second != null ? !second.equals(that.second) : that.second != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = first != null ? first.hashCode() : 0;
		result = 31 * result + (second != null ? second.hashCode() : 0);
		return result;
	}
}
