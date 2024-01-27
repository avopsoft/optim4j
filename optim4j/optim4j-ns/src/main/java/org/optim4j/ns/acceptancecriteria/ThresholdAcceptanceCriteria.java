package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;

/**
 * An acceptance criteria based on predefined threshold.
 * 
 * @author Avijit Basak
 *
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

	public ThresholdAcceptanceCriteria(double threshold, double reductionFactor) {
		this.threshold = threshold;
		this.reductionFactor = reductionFactor;
	}

	/**
	 * Accepts the neighbor agent if it has better fitness than the current agent or
	 * the fitness difference is lesser than or equal to current threshold. The
	 * threshold is also reduced monotonically by predefined reduction factor.
	 */
	@Override
	public boolean isAcceptable(Agent current, Agent neighbour) {
		if (neighbour.compareTo(current) >= 0) {
			return true;
		} else if (current.compareTo(neighbour) <= threshold) {
			threshold *= reductionFactor;
			return true;
		}
		return false;
	}

}
