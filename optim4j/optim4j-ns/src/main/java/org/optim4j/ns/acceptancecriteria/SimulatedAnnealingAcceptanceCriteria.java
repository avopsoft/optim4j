package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;

public class SimulatedAnnealingAcceptanceCriteria implements AcceptanceCriteria {

	private double temperature;

	private final double decayRate;

	public SimulatedAnnealingAcceptanceCriteria(double initialTemperature, double decayRate) {
		this.temperature = initialTemperature;
		this.decayRate = decayRate;
	}

	@Override
	public boolean isAcceptable(Agent current, Agent neighbour) {
		temperature *= decayRate;
		double neighbourFitness = neighbour.evaluate();
		double currentFitness = current.evaluate();
		if (neighbourFitness >= currentFitness) {
			return true;
		}
		final double acceptanceProbability = 1 / (1 + Math.exp(-(neighbourFitness - currentFitness) / temperature));

		return Math.random() <= acceptanceProbability;
	}

}
