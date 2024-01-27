package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;

/**
 * An acceptance criteria based on simulated annealing principle.
 * 
 * @author Avijit Basak
 *
 */
public class SimulatedAnnealingAcceptanceCriteria implements AcceptanceCriteria {

	/**
	 * Temperature of annealing process.
	 */
	private double temperature;

	/**
	 * Decay rate of temperature.
	 */
	private final double decayRate;

	public SimulatedAnnealingAcceptanceCriteria(double initialTemperature, double decayRate) {
		this.temperature = initialTemperature;
		this.decayRate = decayRate;
	}

	/**
	 * Accepts the neighbor agent if it has better fitness than current or generated
	 * random number is lesser than acceptance probability generated based on
	 * sigmoid function. This process reduces the temperature by decay rate in each iteration monotonically.
	 */
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
