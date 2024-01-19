package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;

public class RandomWalkAcceptanceCriteria implements AcceptanceCriteria {

	@Override
	public boolean isAcceptable(Agent current, Agent neighbour) {
		return true;
	}

}
