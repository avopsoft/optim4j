package org.optim4j.ns;

/**
 * Destroyer of a valid solution agent into a partial solution.
 * 
 * @author Avijit Basak
 *
 * @param <A> A valid solution agent
 * @param <T> Partially destroyed solution agent
 */
@FunctionalInterface
public interface Destroyer<A extends Agent, T> {

	/**
	 * Destroys a valid solution agent to create a partially destroyed solution.
	 * 
	 * @param agent a valid solution agent to destroy partially
	 * @return a partially destroyed agent
	 */
	T destroy(A agent);
}
