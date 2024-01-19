package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;

public class ThresholdOldBachelorAcceptanceCriteria implements AcceptanceCriteria {

	private double threshold;

	private double reductionFactor;

	private double incrementFactor;

	public ThresholdOldBachelorAcceptanceCriteria(double threshold, double reductionFactor, double incrementFactor) {
		this.threshold = threshold;
		this.reductionFactor = reductionFactor;
		this.incrementFactor = incrementFactor;
	}

	@Override
	public boolean isAcceptable(Agent current, Agent neighbour) {
		if (neighbour.compareTo(current) >= 0) {
			return true;
		} else if (neighbour.compareTo(current) <= threshold) {
			threshold *= reductionFactor;
			return true;
		} else {
			threshold *= incrementFactor;
			return false;
		}
	}

}
