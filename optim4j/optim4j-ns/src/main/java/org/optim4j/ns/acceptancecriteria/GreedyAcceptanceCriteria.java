package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;

public class GreedyAcceptanceCriteria implements AcceptanceCriteria {

	public boolean isAcceptable(Agent current, Agent neighbour) {
		return neighbour.compareTo(current) >= 0;
	}

}
