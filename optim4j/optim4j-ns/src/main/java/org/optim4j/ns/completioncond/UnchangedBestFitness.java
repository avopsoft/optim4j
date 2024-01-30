package org.optim4j.ns.completioncond;

import org.optim4j.ns.Agent;
import org.optim4j.ns.CompletionCondition;

/**
 * A completion condition based on unchanged best fitness value for predefined
 * number of generations.
 * 
 * @author Avijit Basak
 */
public class UnchangedBestFitness implements CompletionCondition {

	/**
	 * Maximum number of generations allowed with unchanged best fitness.
	 */
	private final int maxNoOfGenerationsWithUnchangedBestFitness;

	/**
	 * Number of generations passed with unchanged best fitness.
	 */
	private int noOfGenerationsWithUnchangedBestFitness;

	/**
	 * Last best fitness found so far.
	 */
	private double lastBestFitness = -Double.MAX_VALUE;

	/**
	 * Constructs a completion condition based on maximum number of generations with
	 * unchanged best fitness allowed.
	 * 
	 * @param maxNoOfGenerationsWithUnchangedBestFitness maximum number of
	 *                                                   generations allowed with
	 *                                                   unchanged best fitness
	 */
	public UnchangedBestFitness(int maxNoOfGenerationsWithUnchangedBestFitness) {
		this.maxNoOfGenerationsWithUnchangedBestFitness = maxNoOfGenerationsWithUnchangedBestFitness;
	}

	/**
	 * Checks if the maximum no of generations is passed since we get the last best
	 * fitness. It updates the last best fitness value once an agent is found having
	 * better fitness or increases the value of no of generations with unchanged
	 * best fitness. Once value of no of generations with unchanged best fitness
	 * equals the configured max generation the completion condition is considered
	 * to be achieved.
	 * 
	 * @param agent an {@link Agent}
	 * @return completion status of the optimization process
	 */
	@Override
	public boolean isComplete(Agent agent) {
		double currentFitness = 0.0;
		if ((currentFitness = agent.evaluate()) != -Double.MAX_VALUE && currentFitness >= lastBestFitness) {
			lastBestFitness = currentFitness;
			noOfGenerationsWithUnchangedBestFitness = 0;
		} else {
			noOfGenerationsWithUnchangedBestFitness++;
		}
		return noOfGenerationsWithUnchangedBestFitness >= maxNoOfGenerationsWithUnchangedBestFitness ? true : false;
	}

}
