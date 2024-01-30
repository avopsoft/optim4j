package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;

/**
 * An acceptance criteria based on greedy acceptance strategy.
 * <p>
 * It accepts any neighbor which has higher fitness compared to the current
 * agent.
 * </p>
 * 
 * @author Avijit Basak
 */
public class GreedyAcceptanceCriteria implements AcceptanceCriteria {

	/**
	 * Accepts if the neighbor has better fitness value than current agent.
	 * 
	 * @param current  current agent
	 * @param neighbor neighbor agent
	 * @return acceptability of the neighbor
	 */
	public boolean isAcceptable(Agent current, Agent neighbor) {
		return neighbor.compareTo(current) >= 0;
	}

}
