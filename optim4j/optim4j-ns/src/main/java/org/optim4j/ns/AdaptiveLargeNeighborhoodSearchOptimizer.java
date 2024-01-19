package org.optim4j.ns;

import java.util.ArrayList;
import java.util.List;

public class AdaptiveLargeNeighborhoodSearchOptimizer<A extends Agent, T> implements Optimizer<A> {

	private CompletionCondition completionCondition;

	private AcceptanceCriteria acceptanceCriteria;

	private Observer observer;

	private String name;

	private List<Double> repairerScores = new ArrayList<>();

	private List<Double> destroyerScores = new ArrayList<>();

	private AdaptiveRepairerDestroyerManager<T, A> repairerDestroyerManager;

	public AdaptiveLargeNeighborhoodSearchOptimizer(AcceptanceCriteria acceptanceCriteria, CompletionCondition completionCondition,
			List<Double> repairerScores, List<Double> destroyerScores, Observer observer, String name,
			AdaptiveRepairerDestroyerManager<T, A> repairerDestroyerManager) {
		this.acceptanceCriteria = acceptanceCriteria;
		this.completionCondition = completionCondition;
		this.repairerScores.addAll(repairerScores);
		this.destroyerScores.addAll(destroyerScores);
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
			Repairer<T, A> repairer = repairerDestroyerManager.getRepairer();
			Destroyer<A, T> destroyer = repairerDestroyerManager.getDestroyer();

			// Find the neighbour solution.
			A neighbour = repairer.repair(destroyer.destroy(currentAgent));

			// Check if the solution is acceptable.
			boolean isNeighbourAcceptable = acceptanceCriteria.isAcceptable(currentAgent, neighbour);

			// Update the repairer and destroyer scores.
			if (neighbour.compareTo(bestAgent) >= 0) {
				repairerDestroyerManager.updateRepairerScore(repairer, repairerScores.get(0));
				repairerDestroyerManager.updateDestroyerScore(destroyer, destroyerScores.get(0));
			} else if (neighbour.compareTo(currentAgent) >= 0) {
				repairerDestroyerManager.updateRepairerScore(repairer, repairerScores.get(1));
				repairerDestroyerManager.updateDestroyerScore(destroyer, destroyerScores.get(1));
			} else if (isNeighbourAcceptable) {
				repairerDestroyerManager.updateRepairerScore(repairer, repairerScores.get(2));
				repairerDestroyerManager.updateDestroyerScore(destroyer, destroyerScores.get(2));
			}

			// Update the solution agents.
			if (isNeighbourAcceptable) {
				currentAgent = neighbour;
				if (currentAgent.compareTo(bestAgent) >= 0) {
					bestAgent = currentAgent;
				}
			}
		}
		return bestAgent;
	}

}
