package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;

public class ThresholdOldBachelorAcceptanceCriteria implements AcceptanceCriteria {

	private double threshold;

	private double reductionFactor;

	private double incrementFactor;

	public ThresholdOldBachelorAcceptanceCriteria(double initialThreshold, double reductionFactor,
			double incrementFactor) {
		this.threshold = initialThreshold;
		this.reductionFactor = reductionFactor;
		this.incrementFactor = incrementFactor;
	}

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
