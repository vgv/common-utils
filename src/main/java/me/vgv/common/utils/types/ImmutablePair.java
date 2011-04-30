package me.vgv.common.utils.types;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public final class ImmutablePair<A, B> implements Pair<A, B> {

	private final A first;
	private final B second;

	public ImmutablePair(A first, B second) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ImmutablePair that = (ImmutablePair) o;

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
