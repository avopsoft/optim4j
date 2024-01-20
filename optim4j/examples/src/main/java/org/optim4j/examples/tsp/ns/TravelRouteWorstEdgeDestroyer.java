package org.optim4j.examples.tsp.ns;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.optim4j.ns.Destroyer;

public class TravelRouteWorstEdgeDestroyer implements Destroyer<TravelRoute, PartiallyDestroyedTravelRoute> {

	@Override
	public PartiallyDestroyedTravelRoute destroy(TravelRoute travelRoute) {
		int noOfEdgesToBeRemoved = (int) (Math.random() * travelRoute.getRepresentation().size());
		final List<Node> removedNodes = new ArrayList<>();
		final Map<Double, Node> distanceMap = new TreeMap<>(new Comparator<Double>() {

			@Override
			public int compare(Double o1, Double o2) {
				if (o2 > o1) {
					return 1;
				} else if (o2 < o1) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		travelRoute.getRepresentation().stream()
				.forEach(node -> distanceMap.put(travelRoute.remove(node).evaluate() - travelRoute.evaluate(), node));

		TravelRoute newTravelRoute = travelRoute;
		Iterator<Double> itr = distanceMap.keySet().iterator();
		for (int i = 0; itr.hasNext() && i < noOfEdgesToBeRemoved; i++) {
			final Node removableNode = distanceMap.get(itr.next());
			newTravelRoute = newTravelRoute.remove(removableNode);
			removedNodes.add(removableNode);
		}

		return new PartiallyDestroyedTravelRoute(newTravelRoute, removedNodes);
	}

}