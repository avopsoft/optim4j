package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;

/**
 * An acceptance criteria based on greedy acceptance strategy.
 * 
 * @author Avijit Basak
 *
 */
public class GreedyAcceptanceCriteria implements AcceptanceCriteria {

	/**
	 * Accepts if the neighbor has better fitness value than current agent.
	 */
	public boolean isAcceptable(Agent current, Agent neighbour) {
		return neighbour.compareTo(current) >= 0;
	}

}
