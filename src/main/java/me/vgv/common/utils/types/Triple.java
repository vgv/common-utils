package me.vgv.common.utils.types;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public interface Triple<A, B, C> {

	A getFirst();

	B getSecond();

	C getThird();

}
