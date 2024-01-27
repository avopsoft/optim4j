package org.optim4j.ns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AdaptiveRepairerDestroyerManager<T, A extends Agent> {

	private Map<Repairer<T, A>, Double> repairerScoreMap = new HashMap<>();

	private Map<Destroyer<A, T>, Double> destroyerScoreMap = new HashMap<>();

	private Map<ScoreBoundary, Repairer<T, A>> repairerScoreBoundaries = new HashMap<>();

	private Map<ScoreBoundary, Destroyer<A, T>> destroyerScoreBoundaries = new HashMap<>();

	private Scores repairerScores;

	private Scores destroyerScores;

	public AdaptiveRepairerDestroyerManager(List<Repairer<T, A>> repairers, List<Destroyer<A, T>> destroyers,
			Scores repairerScores, Scores destroyerScores) {
		this.repairerScores = repairerScores;
		this.destroyerScores = destroyerScores;
		repairers.stream().forEach(repairer -> repairerScoreMap.put(repairer, repairerScores.initial));
		destroyers.stream().forEach(destroyer -> destroyerScoreMap.put(destroyer, destroyerScores.initial));
	}

	public AdaptiveRepairerDestroyerManager(List<Repairer<T, A>> repairers, List<Destroyer<A, T>> destroyers) {
		this.repairerScores = new Scores(25.0, 100.0, 75.0, 50.0);
		this.destroyerScores = new Scores(25.0, 100.0, 75.0, 50.0);
		repairers.stream().forEach(repairer -> repairerScoreMap.put(repairer, repairerScores.initial));
		destroyers.stream().forEach(destroyer -> destroyerScoreMap.put(destroyer, destroyerScores.initial));
	}

	public void updateScores(Repairer<T, A> repairer, Destroyer<A, T> destroyer, IncrementType incrementType) {
		double existingRepairerScore = this.repairerScoreMap.get(repairer);
		double existingDestroyerScore = this.destroyerScoreMap.get(destroyer);
		switch (incrementType) {
		case NEIGHBOR_BETTER_THAN_BEST:
			this.repairerScoreMap.put(repairer,
					repairerScores.incrementWhenNeighborBetterThanBest + existingRepairerScore);
			this.destroyerScoreMap.put(destroyer,
					destroyerScores.incrementWhenNeighborBetterThanBest + existingDestroyerScore);
			break;

		case NEIGHBOR_BETTER_THAN_CURRENT:
			this.repairerScoreMap.put(repairer,
					repairerScores.incrementWhenNeighborBetterThanCurrent + existingRepairerScore);
			this.destroyerScoreMap.put(destroyer,
					destroyerScores.incrementWhenNeighborBetterThanCurrent + existingDestroyerScore);
			break;

		case NEIGHBOR_ACCEPTABLE:
			this.repairerScoreMap.put(repairer, repairerScores.incrementWhenNeighborAcceptable + existingRepairerScore);
			this.destroyerScoreMap.put(destroyer,
					destroyerScores.incrementWhenNeighborAcceptable + existingDestroyerScore);
			break;

		default:
			throw new IllegalArgumentException("Invalid value found for increment type.");
		}
	}

	private void updateRepairerScoreBoundaries() {
		Set<Repairer<T, A>> repairers = repairerScoreMap.keySet();
		Double cumulativeScore = Double.valueOf(0);
		for (Repairer<T, A> repairer : repairers) {
			ScoreBoundary scoreBoundary = new ScoreBoundary(cumulativeScore,
					cumulativeScore = (cumulativeScore + repairerScoreMap.get(repairer)));
			repairerScoreBoundaries.put(scoreBoundary, repairer);
		}
	}

	private void updateDestroyerScoreBoundaries() {
		Set<Destroyer<A, T>> destroyers = destroyerScoreMap.keySet();
		Double cumulativeScore = Double.valueOf(0);
		for (Destroyer<A, T> destroyer : destroyers) {
			ScoreBoundary scoreBoundary = new ScoreBoundary(cumulativeScore,
					cumulativeScore = (cumulativeScore + destroyerScoreMap.get(destroyer)));
			destroyerScoreBoundaries.put(scoreBoundary, destroyer);
		}
	}

	public Repairer<T, A> getRepairer() {
		updateRepairerScoreBoundaries();
		Optional<Double> totalScore = repairerScoreMap.keySet().stream().map(t -> repairerScoreMap.get(t))
				.reduce((Double t, Double u) -> t + u);
		double randomScore = Math.random() * totalScore.get();
		for (ScoreBoundary scoreBoundary : repairerScoreBoundaries.keySet()) {
			if (scoreBoundary.isBetween(randomScore)) {
				return repairerScoreBoundaries.get(scoreBoundary);
			}
		}
		throw new RuntimeException("Invalid random score generated.");
	}

	public Destroyer<A, T> getDestroyer() {
		updateDestroyerScoreBoundaries();
		Optional<Double> totalScore = destroyerScoreMap.keySet().stream().map(t -> destroyerScoreMap.get(t))
				.reduce((Double t, Double u) -> t + u);
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

	enum IncrementType {
		NEIGHBOR_BETTER_THAN_BEST, NEIGHBOR_BETTER_THAN_CURRENT, NEIGHBOR_ACCEPTABLE
	}

}
