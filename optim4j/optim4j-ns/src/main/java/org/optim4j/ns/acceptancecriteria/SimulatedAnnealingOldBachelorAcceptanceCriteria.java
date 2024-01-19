package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;

public class SimulatedAnnealingOldBachelorAcceptanceCriteria implements AcceptanceCriteria {

	private double temperature = 10000000;

	private final double decayRate;

	private final double incrementRate;

	public SimulatedAnnealingOldBachelorAcceptanceCriteria(double decayRate, double incrementRate) {
		this.decayRate = decayRate;
		this.incrementRate = incrementRate;
	}

	@Override
	public boolean isAcceptable(Agent current, Agent neighbour) {
		double neighbourFitness = neighbour.evaluate();
		double currentFitness = current.evaluate();
		if (neighbourFitness >= currentFitness) {
			temperature *= decayRate;
			return true;
		}
		final double acceptanceProbability = 1 / (1 + Math.exp(-(neighbourFitness - currentFitness) / temperature));
		if (Math.random() <= acceptanceProbability) {
			temperature *= decayRate;
			return true;
		} else {
			temperature *= incrementRate;
			return false;
		}
	}

}
