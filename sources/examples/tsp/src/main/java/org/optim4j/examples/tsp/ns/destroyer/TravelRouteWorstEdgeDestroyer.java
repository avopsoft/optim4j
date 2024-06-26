package org.optim4j.examples.tsp.ns.destroyer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.optim4j.examples.tsp.ns.Node;
import org.optim4j.examples.tsp.ns.PartiallyDestroyedTravelRoute;
import org.optim4j.examples.tsp.ns.TravelRoute;
import org.optim4j.ns.Destroyer;

/**
 * A destroyer heuristic of travel route which destroys few worst edges.
 * 
 * @author Avijit Basak
 */
public class TravelRouteWorstEdgeDestroyer implements Destroyer<TravelRoute, PartiallyDestroyedTravelRoute> {

	private Random random = new Random();

	@Override
	public PartiallyDestroyedTravelRoute destroy(TravelRoute travelRoute) {
		int noOfEdgesToBeRemoved = random.nextInt(travelRoute.len()) % (travelRoute.len() / 2);
		final List<Node> removedNodes = new ArrayList<>();
		final Map<Double, Node> distanceMap = new TreeMap<>((Double o1, Double o2) -> {
			if (o2 > o1) {
				return 1;
			} else if (o2 < o1) {
				return -1;
			} else {
				return 0;
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