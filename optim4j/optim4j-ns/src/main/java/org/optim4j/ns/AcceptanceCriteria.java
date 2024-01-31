package org.optim4j.ns;

/**
 * An acceptance criteria for newly generated neighbor solution agent.
 * 
 * @author Avijit Basak
 */
@FunctionalInterface
public interface AcceptanceCriteria {

	/**
	 * Checks if the new neighbor solution agent is acceptable compared to current
	 * one.
	 * 
	 * @param current   current solution agent
	 * @param neighbour newly generated solution agent
	 * @return acceptability of the neighbor
	 */
	boolean isAcceptable(Agent current, Agent neighbour);

}
