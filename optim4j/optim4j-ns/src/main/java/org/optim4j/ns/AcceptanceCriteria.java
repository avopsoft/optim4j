package org.optim4j.ns;

@FunctionalInterface
public interface AcceptanceCriteria {

	boolean isAcceptable(Agent current, Agent neighbour);

}
