package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An acceptance criteria based on random walk principal.
 * <p>
 * It accepts any neighbor solution irrespective of it's fitness value.
 * </p>
 * 
 * @author Avijit Basak
 */
public class RandomWalkAcceptanceCriteria implements AcceptanceCriteria {

	/**
	 * Instance of logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RandomWalkAcceptanceCriteria.class);

	/**
	 * Accepts all solutions irrespective of their fitness value.
	 * 
	 * @param current  current agent
	 * @param neighbor neighbor agent
	 * @return acceptability of the neighbor
	 */
	@Override
	public boolean isAcceptable(Agent current, Agent neighbor) {
		LOGGER.debug("Current agent fitness: {}, Neighbor agent fitness: {}", current.evaluate(), neighbor.evaluate());
		return true;
	}

	/**
	 * Returns the string representation of random walk acceptance criteria.
	 */
	@Override
	public String toString() {
		return "RandomWalkAcceptanceCriteria []";
	}

}
