package org.optim4j.ns.completioncond;

import org.optim4j.ns.Agent;
import org.optim4j.ns.CompletionCondition;

public class UnchangedBestFitness implements CompletionCondition {

	private final int maxNoOfGenerationsWithUnchangedBestFitness;

	private int noOfGenerationsWithUnchangedBestFitness;

	private double lastBestFitness = -Double.MAX_VALUE;

	public UnchangedBestFitness(int maxNoOfGenerationsWithUnchangedBestFitness) {
		this.maxNoOfGenerationsWithUnchangedBestFitness = maxNoOfGenerationsWithUnchangedBestFitness;
	}

	@Override
	public boolean isComplete(Agent agent) {
		double currentFitness = 0.0;
		if ((currentFitness = agent.evaluate()) != -Double.MAX_VALUE && currentFitness > lastBestFitness) {
			lastBestFitness = currentFitness;
			noOfGenerationsWithUnchangedBestFitness = 0;
		} else {
			noOfGenerationsWithUnchangedBestFitness++;
		}
		return noOfGenerationsWithUnchangedBestFitness >= maxNoOfGenerationsWithUnchangedBestFitness ? true : false;
	}

}
