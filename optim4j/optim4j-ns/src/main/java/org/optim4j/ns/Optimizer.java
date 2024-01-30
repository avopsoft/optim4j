package org.optim4j.ns;

/**
 * Optimizer based on neighborhood search.
 *
 * @param <A> A solution agent.
 */
public interface Optimizer<A extends Agent> {

	/**
	 * Executes the optimization process. Accepts a solution agent as input
	 * optimizes the same using destroy and repair process through multiple
	 * iterations following neighborhood search methodology and returns an optimum
	 * result.
	 * 
	 * @param agent A valid solution agent
	 * @return An optimized solution agent
	 */
	A optimize(A agent);

}
