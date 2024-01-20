package org.optim4j.examples.tsp.ns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.optim4j.ns.Destroyer;

public class TravelRouteRandomDestroyer implements Destroyer<TravelRoute, PartiallyDestroyedTravelRoute> {

	public PartiallyDestroyedTravelRoute destroy(TravelRoute travelRoute) {
		final List<Node> routeNodes = travelRoute.getRepresentation();
		final List<Node> modRouteNodes = new ArrayList<>();
		final List<Node> removedNodes = new ArrayList<>();
		final Set<Integer> removableIndexes = generateRandomIndexes((int) (Math.random() * routeNodes.size()) + 1,
				routeNodes.size());
		removableIndexes.stream().forEach(index -> {
			removedNodes.add(routeNodes.get(index));
		});
		for (int i = 0; i < routeNodes.size(); i++) {
			if (!removableIndexes.contains(i)) {
				modRouteNodes.add(routeNodes.get(i));
			}
		}
		return new PartiallyDestroyedTravelRoute(new TravelRoute(modRouteNodes, travelRoute.getFitnessCalculator()),
				removedNodes);
	}

	private Set<Integer> generateRandomIndexes(int count, int max) {
		Set<Integer> indexes = new HashSet<>();
		while (indexes.size() < count) {
			indexes.add((int) (Math.random() * max));
		}
		return indexes;
	}

}
