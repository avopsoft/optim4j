package org.optim4j.ns;

import org.optim4j.ns.AdaptiveRepairerDestroyerManager.IncrementType;

public class AdaptiveLargeNeighborhoodSearchOptimizer<A extends Agent, T> implements Optimizer<A> {

	private CompletionCondition completionCondition;

	private AcceptanceCriteria acceptanceCriteria;

	private Observer observer;

	private String name;

	private AdaptiveRepairerDestroyerManager<T, A> repairerDestroyerManager;

	public AdaptiveLargeNeighborhoodSearchOptimizer(AcceptanceCriteria acceptanceCriteria,
			CompletionCondition completionCondition, Observer observer, String name,
			AdaptiveRepairerDestroyerManager<T, A> repairerDestroyerManager) {
		this.acceptanceCriteria = acceptanceCriteria;
		this.completionCondition = completionCondition;
		this.observer = observer;
		this.name = name;
		this.repairerDestroyerManager = repairerDestroyerManager;
	}

	public A optimize(A currentAgent) {
		int generation = 0;
		A bestAgent = currentAgent;
		while (!completionCondition.isComplete(currentAgent)) {
			observer.notify(bestAgent, name, generation++);

			// Extract repairer and destroyer.
			final Repairer<T, A> repairer = repairerDestroyerManager.getRepairer();
			final Destroyer<A, T> destroyer = repairerDestroyerManager.getDestroyer();

			// Find the neighbor solution.
			final A neighbour = repairer.repair(destroyer.destroy(currentAgent));

			// Update the repairer and destroyer scores.
			if (neighbour.compareTo(bestAgent) >= 0) {
				repairerDestroyerManager.updateScores(repairer, destroyer, IncrementType.NEIGHBOR_BETTER_THAN_BEST);
				currentAgent = neighbour;
				bestAgent = neighbour;
			} else if (neighbour.compareTo(currentAgent) >= 0) {
				repairerDestroyerManager.updateScores(repairer, destroyer, IncrementType.NEIGHBOR_BETTER_THAN_CURRENT);
				currentAgent = neighbour;
			} else if (acceptanceCriteria.isAcceptable(currentAgent, neighbour)) {
				repairerDestroyerManager.updateScores(repairer, destroyer, IncrementType.NEIGHBOR_ACCEPTABLE);
				currentAgent = neighbour;
			}
		}
		return bestAgent;
	}

}
