package org.optim4j.ns.acceptancecriteria;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An acceptance criteria based on simulated annealing principle.
 * <p>
 * It accepts any neighbor if it's fitness is higher than current agent.
 * Otherwise evaluates the acceptance probability using sigmoid function and
 * determines acceptance by random number generation.
 * </p>
 * 
 * @author Avijit Basak
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

	/**
	 * Instance of logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SimulatedAnnealingAcceptanceCriteria.class);

	/**
	 * Constructs the acceptance criteria based on simulated annealing acceptance
	 * scheme using provided initial temperature, decay rate.
	 * 
	 * @param initialTemperature initial temperature for annealing
	 * @param decayRate          decay rate of temperature in each iteration
	 */
	public SimulatedAnnealingAcceptanceCriteria(double initialTemperature, double decayRate) {
		this.temperature = initialTemperature;
		this.decayRate = decayRate;
	}

	/**
	 * Accepts the neighbor agent if it has better fitness than current agent.
	 * Otherwise its evaluates the acceptance probability using sigmoid function and
	 * determines acceptance if generated random number is lesser than acceptance
	 * probability. This process reduces the temperature by decay rate in each
	 * iteration monotonically.
	 * 
	 * @param current  current agent
	 * @param neighbor neighbor agent
	 * @return acceptability of the neighbor
	 */
	@Override
	public boolean isAcceptable(Agent current, Agent neighbor) {
		LOGGER.debug("Current agent fitness: {}, Neighbor agent fitness: {} & Temperature: {}", current.evaluate(),
				neighbor.evaluate(), temperature);
		final double neighbourFitness = neighbor.evaluate();
		final double currentFitness = current.evaluate();
		if (neighbourFitness >= currentFitness) {
			LOGGER.trace("Neighbor better than current agent. Hence neighbor acceptable");
			return true;
		}

		/*
		 * Generate acceptance probability.
		 */
		final double acceptanceProbability = 1 / (1 + Math.exp(-(neighbourFitness - currentFitness) / temperature));

		LOGGER.trace("Generated acceptance probability: {}", acceptanceProbability);

		/*
		 * Reduce temperature by decay rate.
		 */
		temperature *= decayRate;

		return Math.random() <= acceptanceProbability;
	}

}
