package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An acceptance criteria based on old bachelor principle.
 * <p>
 * It accepts an initial threshold, reduction factor and increment factor as
 * input. The reduction factor is used to reduce the threshold when the neighbor
 * is acceptable and increased by increment factor when the neighbor is not
 * acceptable.
 * </p>
 * 
 * @author Avijit Basak
 */
public final class OldBachelorAcceptanceCriteria implements AcceptanceCriteria {

	/**
	 * Threshold to consider in case neighbor agent fitness is lesser than current
	 * one.
	 */
	private double threshold;

	/**
	 * The factor by which the threshold is to be reduced when neighbor solution
	 * agent is acceptable.
	 */
	private final double reductionFactor;

	/**
	 * The factor by which the threshold is to be increased in case the fitness
	 * difference between current and neighbor agent is greater than current
	 * threshold.
	 */
	private final double incrementFactor;

	/**
	 * Instance of logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OldBachelorAcceptanceCriteria.class);

	/**
	 * Constructs a threshold old bachelor scheme acceptance criteria based on
	 * initial threshold, reduction factor and increment factor.
	 * 
	 * @param initialThreshold initial threshold value for neighbor acceptance
	 *                         fitness
	 * @param reductionFactor  threshold reduction factor in case neighbor is
	 *                         acceptable
	 * @param incrementFactor  threshold increment factor in case neighbor is
	 *                         unacceptable
	 */
	public OldBachelorAcceptanceCriteria(double initialThreshold, double reductionFactor, double incrementFactor) {
		if (initialThreshold <= 0) {
			throw new IllegalArgumentException("Initial threshold should be positive number.");
		}
		if (!(reductionFactor > 0 && reductionFactor < 1)) {
			throw new IllegalArgumentException("Reduction factor should be between 0 and 1.");
		}
		if (incrementFactor <= 1) {
			throw new IllegalArgumentException("Reduction factor should be greater than 1.");
		}
		this.threshold = initialThreshold;
		this.reductionFactor = reductionFactor;
		this.incrementFactor = incrementFactor;
	}

	/**
	 * Accepts the neighbor agent if it has better fitness than the current agent or
	 * the fitness difference is lesser than or equal to current threshold. The
	 * threshold is reduced by predefined reduction factor if the neighbor is
	 * acceptable otherwise increased by increment factor.
	 * 
	 * @param current  current agent
	 * @param neighbor neighbor agent
	 * @return acceptability of the neighbor
	 */
	@Override
	public boolean isAcceptable(Agent current, Agent neighbor) {
		LOGGER.debug("Current agent fitness: {}, Neighbor agent fitness: {} & Threshold: {}", current.evaluate(),
				neighbor.evaluate(), threshold);
		if (neighbor.compareTo(current) >= 0) {
			LOGGER.debug("Neighbor better than current agent. Hence neighbor acceptable");
			return true;
		} else if (current.compareTo(neighbor) <= threshold) {
			LOGGER.debug(
					"Fitness difference between current and neighbor agent is lesser than threshold. Hence neighbor is acceptable.");
			threshold *= reductionFactor;
			return true;
		} else {
			LOGGER.debug(
					"Fitness difference between current and neighbor agent is more than threshold. Hence neighbor is unacceptable.");
			threshold *= incrementFactor;
			return false;
		}
	}

	/**
	 * Returns current threshold value.
	 * 
	 * @return threshold
	 */
	public double getThreshold() {
		return threshold;
	}

	/**
	 * Returns the string representation along with current threshold value of old
	 * bachelor acceptance criteria.
	 */
	@Override
	public String toString() {
		return "OldBachelorAcceptanceCriteria [threshold=" + threshold + "]";
	}

}
