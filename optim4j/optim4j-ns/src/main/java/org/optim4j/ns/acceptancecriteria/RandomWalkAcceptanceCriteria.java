package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;

/**
 * An acceptance criteria based on random walk principal.
 * 
 * @author Avijit Basak
 *
 */
public class RandomWalkAcceptanceCriteria implements AcceptanceCriteria {

	/**
	 * Accepts all solutions irrespective of their fitness value.
	 */
	@Override
	public boolean isAcceptable(Agent current, Agent neighbour) {
		return true;
	}

}
