package org.optim4j.ns;

/**
 * A Solution Agent for optimization.
 * 
 * @author Avijit Basak
 */
public interface Agent extends Comparable<Agent> {

	/**
	 * Evaluates the agent to calculate the fitness value.
	 * 
	 * @return the fitness value of the agent
	 */
	double evaluate();
}
