package org.optim4j.ns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AdaptiveRepairerDestroyerManager<T, A extends Agent> {

	private Map<Repairer<T, A>, Double> repairerScoreMap = new HashMap<>();

	private Map<Destroyer<A, T>, Double> destroyerScoreMap = new HashMap<>();

	private List<Repairer<T, A>> repairers = new ArrayList<>();

	private List<Destroyer<A, T>> destroyers = new ArrayList<>();

	private Map<ScoreBoundary, Repairer<T, A>> repairerScoreBoundaries = new HashMap<>();

	private Map<ScoreBoundary, Destroyer<A, T>> destroyerScoreBoundaries = new HashMap<>();

	public AdaptiveRepairerDestroyerManager(List<Repairer<T, A>> repairers, List<Destroyer<A, T>> destroyers) {
		this.repairers.addAll(repairers);
		this.destroyers.addAll(destroyers);
		repairers.stream().forEach(repairer -> repairerScoreMap.put(repairer, 0.0));
		destroyers.stream().forEach(destroyer -> destroyerScoreMap.put(destroyer, 0.0));
	}

	public void updateRepairerScore(Repairer<T, A> repairer, Double repairerScore) {
		double existingScore = this.repairerScoreMap.get(repairer);
		this.repairerScoreMap.put(repairer, repairerScore + existingScore);
	}

	public void updateDestroyerScore(Destroyer<A, T> destroyer, Double destroyerScore) {
		double existingScore = this.destroyerScoreMap.get(destroyer);
		this.destroyerScoreMap.put(destroyer, destroyerScore + existingScore);
	}

	public void updateScoreBoundaries() {
		Set<Repairer<T, A>> repairers = repairerScoreMap.keySet();
		Double cumulativeScore = Double.valueOf(0);
		for (Repairer<T, A> repairer : repairers) {
			ScoreBoundary scoreBoundary = new ScoreBoundary(cumulativeScore,
					cumulativeScore + repairerScoreMap.get(repairer));
			repairerScoreBoundaries.put(scoreBoundary, repairer);
		}

		Set<Destroyer<A, T>> destroyers = destroyerScoreMap.keySet();
		Double destroyerCumulativeScore = Double.valueOf(0);
		for (Destroyer<A, T> destroyer : destroyers) {
			ScoreBoundary scoreBoundary = new ScoreBoundary(destroyerCumulativeScore,
					destroyerCumulativeScore + destroyerScoreMap.get(destroyer));
			destroyerScoreBoundaries.put(scoreBoundary, destroyer);
		}
	}

	public Repairer<T, A> getRepairer() {
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

}
