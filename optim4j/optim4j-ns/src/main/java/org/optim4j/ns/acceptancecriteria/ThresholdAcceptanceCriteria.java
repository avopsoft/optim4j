package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;

public class ThresholdAcceptanceCriteria implements AcceptanceCriteria {

	private double threshold;

	private double reductionFactor;

	public ThresholdAcceptanceCriteria(double threshold, double reductionFactor) {
		this.threshold = threshold;
		this.reductionFactor = reductionFactor;
	}

	@Override
	public boolean isAcceptable(Agent current, Agent neighbour) {
		if (neighbour.compareTo(current) >= 0) {
			return true;
		} else if (neighbour.compareTo(current) <= threshold) {
			threshold *= reductionFactor;
			return true;
		}
		return false;
	}

}
