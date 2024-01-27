package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;

/**
 * An acceptance criteria based on predefined threshold old bachelor strategy.
 * 
 * @author Avijit Basak
 *
 */
public class ThresholdOldBachelorAcceptanceCriteria implements AcceptanceCriteria {

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
	 * The factor by which the threshold is to be increased in case the fitness
	 * difference between current and neighbor agent is greater than current
	 * threshold.
	 */
	private double incrementFactor;

	public ThresholdOldBachelorAcceptanceCriteria(double initialThreshold, double reductionFactor,
			double incrementFactor) {
		this.threshold = initialThreshold;
		this.reductionFactor = reductionFactor;
		this.incrementFactor = incrementFactor;
	}

	/**
	 * Accepts the neighbor agent if it has better fitness than the current agent or
	 * the fitness difference is lesser than or equal to current threshold. The
	 * threshold is reduced by predefined reduction factor if the neighbor is
	 * acceptable otherwise increases the same by increment factor.
	 */
	@Override
	public boolean isAcceptable(Agent current, Agent neighbour) {
		if (neighbour.compareTo(current) >= 0) {
			return true;
		} else if (current.compareTo(neighbour) <= threshold) {
			threshold *= reductionFactor;
			return true;
		} else {
			threshold *= incrementFactor;
			return false;
		}
	}

}
