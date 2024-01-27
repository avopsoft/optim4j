package org.optim4j.ns;

/**
 * Destroyer of a valid solution agent as part of neighborhood search process.
 * 
 * @author Avijit Basak
 *
 * @param <A> A Solution Agent
 * @param <T> Partially destroyed Agent
 */
@FunctionalInterface
public interface Destroyer<A extends Agent, T> {

	/**
	 * Destroys a valid solution agent to create a partially destroyed solution.
	 * 
	 * @param agent
	 * @return
	 */
	T destroy(A agent);
}
