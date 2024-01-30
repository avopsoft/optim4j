package org.optim4j.ns;

/**
 * Repairer of partially destroyed solution to build a valid solution agent.
 *
 * @param <T> Partially destroyed solution agent
 * @param <A> A valid solution agent
 */
@FunctionalInterface
public interface Repairer<T, A extends Agent> {

	/**
	 * Repairs a partially destroyed solution to build a valid solution.
	 * 
	 * @param t a partially destroyed solution agent
	 * @return a valid solution agent
	 */
	A repair(T t);
}
