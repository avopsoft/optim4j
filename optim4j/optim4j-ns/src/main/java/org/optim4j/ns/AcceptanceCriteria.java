package org.optim4j.ns;

/**
 * An acceptance criteria for newly generated neighbor solution agent.
 */
@FunctionalInterface
public interface AcceptanceCriteria {

	/**
	 * Checks if the new neighbor solution agent is acceptable compared to current
	 * one.
	 * 
	 * @param current   The current solution agent
	 * @param neighbour Newly generated solution agent.
	 * @return If the neighbor solution agent is acceptable or not
	 */
	boolean isAcceptable(Agent current, Agent neighbour);

}
