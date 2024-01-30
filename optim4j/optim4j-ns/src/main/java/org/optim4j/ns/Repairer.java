package org.optim4j.ns;

/**
 * Repairs the partially destroyed solution to build a valid solution agent.
 * 
 * @author Avijit Basak
 *
 * @param <T> Partially destroyed solution agent
 * @param <A> A valid solution agent
 */
@FunctionalInterface
public interface Repairer<T, A extends Agent> {

	/**
	 * Repairs a partially destroyed solution to build a valid solution.
	 * 
	 * @param t A partially destroyed solution agent
	 * @return A valid solution agent
	 */
	A repair(T t);
}
