package org.optim4j.ns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * An implementation of adaptive large neighborhood search algorithm.
 * 
 * <p>
 * The implementation accepts a solution agent which is a local optima of the
 * problem domain. The optimization process transforms the same using destroy
 * and repair heuristics over multiple iterations. The new solution in each
 * iteration is accepted based on provided acceptance criteria. The process
 * continues until the completion condition is reached.
 * </p>
 * 
 * @author Avijit Basak
 *
 * @param <A>
 * @param <T>
 */
public class AdaptiveLargeNeighborhoodSearchOptimizer<A extends Agent, T> implements Optimizer<A> {

	private CompletionCondition completionCondition;

	private AcceptanceCriteria acceptanceCriteria;

	private Observer observer;

	private String name;

	private ScoreBasedRepairerDestroyerManager repairerDestroyerManager;

	public AdaptiveLargeNeighborhoodSearchOptimizer(AcceptanceCriteria acceptanceCriteria,
			CompletionCondition completionCondition, Observer observer, String name, List<Repairer<T, A>> repairers,
			List<Destroyer<A, T>> destroyers, Scores repairerScores, Scores destroyerScores) {
		this.acceptanceCriteria = acceptanceCriteria;
		this.completionCondition = completionCondition;
		this.observer = observer;
		this.name = name;
		this.repairerDestroyerManager = new ScoreBasedRepairerDestroyerManager(repairers, destroyers, repairerScores,
				destroyerScores);
	}

	public AdaptiveLargeNeighborhoodSearchOptimizer(AcceptanceCriteria acceptanceCriteria,
			CompletionCondition completionCondition, Observer observer, String name, List<Repairer<T, A>> repairers,
			List<Destroyer<A, T>> destroyers) {
		this.acceptanceCriteria = acceptanceCriteria;
		this.completionCondition = completionCondition;
		this.observer = observer;
		this.name = name;
		this.repairerDestroyerManager = new ScoreBasedRepairerDestroyerManager(repairers, destroyers);
	}

	public A optimize(A currentAgent) {
		int generation = 0;
		A bestAgent = currentAgent;
		while (!completionCondition.isComplete(currentAgent)) {
			observer.notify(bestAgent, name, generation);

			if (generation++ % 10 == 0) {
				repairerDestroyerManager.updateScoreBoundaries();
			}

			// Extract repairer and destroyer.
			final Repairer<T, A> repairer = repairerDestroyerManager.getRepairer();
			final Destroyer<A, T> destroyer = repairerDestroyerManager.getDestroyer();

			// Find the neighbor solution.
			final A neighbour = repairer.repair(destroyer.destroy(currentAgent));

			// Update the repairer and destroyer scores.
			if (neighbour.compareTo(bestAgent) >= 0) {
				repairerDestroyerManager.updateScoresWhenNeighborBetterThanBest(repairer, destroyer);
				currentAgent = neighbour;
				bestAgent = neighbour;
			} else if (neighbour.compareTo(currentAgent) >= 0) {
				repairerDestroyerManager.updateScoresWhenNeighborBetterThanCurrent(repairer, destroyer);
				currentAgent = neighbour;
			} else if (acceptanceCriteria.isAcceptable(currentAgent, neighbour)) {
				repairerDestroyerManager.updateScoresWhenNeighborAcceptable(repairer, destroyer);
				currentAgent = neighbour;
			}
		}
		return bestAgent;
	}

	private class ScoreBasedRepairerDestroyerManager {

		private Map<Repairer<T, A>, Double> repairerScoreMap = new HashMap<>();

		private Map<Destroyer<A, T>, Double> destroyerScoreMap = new HashMap<>();

		private Map<ScoreBoundary, Repairer<T, A>> repairerScoreBoundaries = new HashMap<>();

		private Map<ScoreBoundary, Destroyer<A, T>> destroyerScoreBoundaries = new HashMap<>();

		private Scores repairerScores;

		private Scores destroyerScores;

		private ScoreBasedRepairerDestroyerManager(List<Repairer<T, A>> repairers, List<Destroyer<A, T>> destroyers,
				Scores repairerScores, Scores destroyerScores) {
			this.repairerScores = repairerScores;
			this.destroyerScores = destroyerScores;
			repairers.stream().forEach(repairer -> repairerScoreMap.put(repairer, repairerScores.initial));
			destroyers.stream().forEach(destroyer -> destroyerScoreMap.put(destroyer, destroyerScores.initial));
		}

		private ScoreBasedRepairerDestroyerManager(List<Repairer<T, A>> repairers, List<Destroyer<A, T>> destroyers) {
			this.repairerScores = new Scores(25.0, 100.0, 75.0, 50.0);
			this.destroyerScores = new Scores(25.0, 100.0, 75.0, 50.0);
			repairers.stream().forEach(repairer -> repairerScoreMap.put(repairer, repairerScores.initial));
			destroyers.stream().forEach(destroyer -> destroyerScoreMap.put(destroyer, destroyerScores.initial));
		}

		private void updateScoreBoundaries() {
			Set<Repairer<T, A>> repairers = repairerScoreMap.keySet();
			Set<Destroyer<A, T>> destroyers = destroyerScoreMap.keySet();
			Double cumulativeScore = Double.valueOf(0);
			for (Repairer<T, A> repairer : repairers) {
				ScoreBoundary scoreBoundary = new ScoreBoundary(cumulativeScore,
						cumulativeScore = (cumulativeScore + repairerScoreMap.get(repairer)));
				repairerScoreBoundaries.put(scoreBoundary, repairer);
			}

			cumulativeScore = Double.valueOf(0);
			for (Destroyer<A, T> destroyer : destroyers) {
				ScoreBoundary scoreBoundary = new ScoreBoundary(cumulativeScore,
						cumulativeScore = (cumulativeScore + destroyerScoreMap.get(destroyer)));
				destroyerScoreBoundaries.put(scoreBoundary, destroyer);
			}
		}

		private Repairer<T, A> getRepairer() {
			Optional<Double> totalScore = repairerScoreBoundaries.keySet().stream()
					.map(scoreBoundary -> scoreBoundary.maxScore).reduce((Double t, Double u) -> t > u ? t : u);
			double randomScore = Math.random() * totalScore.get();
			for (ScoreBoundary scoreBoundary : repairerScoreBoundaries.keySet()) {
				if (scoreBoundary.isBetween(randomScore)) {
					return repairerScoreBoundaries.get(scoreBoundary);
				}
			}
			throw new RuntimeException("Invalid random score generated.");
		}

		private Destroyer<A, T> getDestroyer() {
			Optional<Double> totalScore = destroyerScoreBoundaries.keySet().stream()
					.map(scoreBoundary -> scoreBoundary.maxScore).reduce((Double t, Double u) -> t > u ? t : u);
			double randomScore = Math.random() * totalScore.get();
			for (ScoreBoundary scoreBoundary : destroyerScoreBoundaries.keySet()) {
				if (scoreBoundary.isBetween(randomScore)) {
					return destroyerScoreBoundaries.get(scoreBoundary);
				}
			}
			throw new RuntimeException("Invalid random score generated.");
		}

		private class ScoreBoundary {

			private double minScore;

			private double maxScore;

			private ScoreBoundary(double minScore, double maxScore) {
				this.minScore = minScore;
				this.maxScore = maxScore;
			}

			private boolean isBetween(double score) {
				return score >= minScore && score < maxScore;
			}

			@Override
			public String toString() {
				return "ScoreRange: " + (maxScore - minScore);
			}

		}

		private void updateScoresWhenNeighborBetterThanBest(Repairer<T, A> repairer, Destroyer<A, T> destroyer) {
			double existingRepairerScore = this.repairerScoreMap.get(repairer);
			double existingDestroyerScore = this.destroyerScoreMap.get(destroyer);
			this.repairerScoreMap.put(repairer,
					repairerScores.incrementWhenNeighborBetterThanBest + existingRepairerScore);
			this.destroyerScoreMap.put(destroyer,
					destroyerScores.incrementWhenNeighborBetterThanBest + existingDestroyerScore);
		}

		private void updateScoresWhenNeighborBetterThanCurrent(Repairer<T, A> repairer, Destroyer<A, T> destroyer) {
			double existingRepairerScore = this.repairerScoreMap.get(repairer);
			double existingDestroyerScore = this.destroyerScoreMap.get(destroyer);
			this.repairerScoreMap.put(repairer,
					repairerScores.incrementWhenNeighborBetterThanCurrent + existingRepairerScore);
			this.destroyerScoreMap.put(destroyer,
					destroyerScores.incrementWhenNeighborBetterThanCurrent + existingDestroyerScore);
		}

		private void updateScoresWhenNeighborAcceptable(Repairer<T, A> repairer, Destroyer<A, T> destroyer) {
			double existingRepairerScore = this.repairerScoreMap.get(repairer);
			double existingDestroyerScore = this.destroyerScoreMap.get(destroyer);
			this.repairerScoreMap.put(repairer, repairerScores.incrementWhenNeighborAcceptable + existingRepairerScore);
			this.destroyerScoreMap.put(destroyer,
					destroyerScores.incrementWhenNeighborAcceptable + existingDestroyerScore);
		}

	}

	public static class Scores {

		private double initial;

		private double incrementWhenNeighborBetterThanBest;

		private double incrementWhenNeighborBetterThanCurrent;

		private double incrementWhenNeighborAcceptable;

		public Scores(double initial, double incrementWhenNeighborBetterThanBest,
				double incrementWhenNeighborBetterThanCurrent, double incrementWhenNeighborIsAcceptable) {
			this.initial = initial;
			this.incrementWhenNeighborBetterThanBest = incrementWhenNeighborBetterThanBest;
			this.incrementWhenNeighborBetterThanCurrent = incrementWhenNeighborBetterThanCurrent;
			this.incrementWhenNeighborAcceptable = incrementWhenNeighborIsAcceptable;
		}
	}

}
