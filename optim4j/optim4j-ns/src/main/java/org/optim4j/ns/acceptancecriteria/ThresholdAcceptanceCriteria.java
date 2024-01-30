package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An acceptance criteria based on threshold value.
 * <p>
 * The neighbor agent is accepted if it has higher fitness than the current
 * agent or if the fitness difference between current and neighbor is lesser
 * than or equal to the threshold value.
 * </p>
 */
public class ThresholdAcceptanceCriteria implements AcceptanceCriteria {

	/**
	 * Threshold to consider in case neighbor agent fitness is lesser than current
	 * one.
	 */
	private double threshold;

	/**
	 * The factor by which the threshold is to be reduced on each iteration of
	 * optimization.
	 */
	private double reductionFactor;

	/**
	 * Instance of logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ThresholdAcceptanceCriteria.class);

	/**
	 * Constructs a threshold acceptance criteria based on initial threshold value
	 * and reduction factor.
	 * 
	 * @param initialThreshold initial threshold value for neighbor acceptance
	 *                         fitness
	 * @param reductionFactor  threshold reduction factor for each iteration
	 */
	public ThresholdAcceptanceCriteria(double initialThreshold, double reductionFactor) {
		this.threshold = initialThreshold;
		this.reductionFactor = reductionFactor;
	}

	/**
	 * Accepts the neighbor agent if it has better fitness than the current agent or
	 * the fitness difference is lesser than or equal to threshold. The threshold is
	 * also reduced monotonically by predefined reduction factor.
	 * 
	 * @param current  current agent
	 * @param neighbor neighbor agent
	 * @return true/false
	 */
	@Override
	public boolean isAcceptable(Agent current, Agent neighbor) {
		LOGGER.debug("Current agent fitness: {}, Neighbor agent fitness: {} & Threshold: {}", current.evaluate(),
				neighbor.evaluate(), threshold);
		if (neighbor.compareTo(current) >= 0) {
			LOGGER.trace("Neighbor better than current agent. Hence neighbor acceptable");
			return true;
		} else if (current.compareTo(neighbor) <= threshold) {
			LOGGER.trace(
					"Fitness difference between current and neighbor agent is lesser than threshold. Hence neighbor is acceptable.");
			threshold *= reductionFactor;
			return true;
		}
		LOGGER.trace("Neighbor is not acceptable.");

		return false;
	}

}
