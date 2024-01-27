package org.optim4j.ns;

/**
 * Represents optimizer based on neighborhood search.
 * 
 * @author Avijit Basak
 *
 * @param <A> A solution agent.
 */
public interface Optimizer<A extends Agent> {

	A optimize(A agent);

}
