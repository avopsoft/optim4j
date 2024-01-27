package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;

/**
 * An acceptance criteria based on simulated annealing old bachelor strategy.
 * 
 * @author Avijit Basak
 *
 */
public class SimulatedAnnealingOldBachelorAcceptanceCriteria implements AcceptanceCriteria {

	/**
	 * Temperature of annealing process.
	 */
	private double temperature;

	/**
	 * Decay rate of temperature.
	 */
	private final double decayRate;

	/**
	 * Increment rate of temperature.
	 */
	private final double incrementRate;

	public SimulatedAnnealingOldBachelorAcceptanceCriteria(double initialTemperature, double decayRate,
			double incrementRate) {
		this.temperature = initialTemperature;
		this.decayRate = decayRate;
		this.incrementRate = incrementRate;
	}

	/**
	 * Accepts the neighbor agent if it has better fitness than current or generated
	 * random number is lesser than acceptance probability generated based on
	 * sigmoid function. This process reduces the temperature by decay rate if the
	 * neighbor is accepted otherwise increases the same by increment rate.
	 */
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
