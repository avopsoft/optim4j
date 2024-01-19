package org.optim4j.examples.tsp.ns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.optim4j.ns.Destroyer;

public class TravelRouteWorstEdgeDestroyer implements Destroyer<TravelRoute, PartiallyDestroyedTravelRoute> {

	private int noOfEdgesToBeRemoved;

	public TravelRouteWorstEdgeDestroyer(int noOfEdgesToBeRemoved) {
		this.noOfEdgesToBeRemoved = noOfEdgesToBeRemoved;
	}

	@Override
	public PartiallyDestroyedTravelRoute destroy(TravelRoute travelRoute) {
		TravelRoute newTravelRoute = travelRoute;
		final List<Node> removedNodes = new ArrayList<>();
		for (int i = 0; i < noOfEdgesToBeRemoved; i++) {
			final Map<Double, Node> distanceMap = new TreeMap<>();
			newTravelRoute.getRepresentation().stream()
					.forEach(node -> distanceMap.put(travelRoute.remove(node).evaluate(), node));
			final Node removableNode = distanceMap.entrySet().iterator().next().getValue();
			newTravelRoute = travelRoute.remove(removableNode);
			removedNodes.add(removableNode);
		}
		return new PartiallyDestroyedTravelRoute(newTravelRoute, removedNodes);
	}

}
