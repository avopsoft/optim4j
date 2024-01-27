package org.optim4j.examples.tsp.ns;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.optim4j.ns.Repairer;

public class TravelRouteBasicGreedyRepairer implements Repairer<PartiallyDestroyedTravelRoute, TravelRoute> {

	@Override
	public TravelRoute repair(PartiallyDestroyedTravelRoute partiallyDestroyedTravelRoute) {
		TravelRoute partialTravelRoute = partiallyDestroyedTravelRoute.getPartialTravelRoute();
		Set<Node> destroyedNodes = new HashSet<>(partiallyDestroyedTravelRoute.getDestroyedNodes());
		TravelRoute completeTravelRoute = partialTravelRoute;
		for (Node destroyedNode : destroyedNodes) {
			int len = completeTravelRoute.len();
			Map<Double, Integer> sortedNodeIndex = new TreeMap<>();
			for (int i = 0; i <= len; i++) {
				sortedNodeIndex.put(Math.abs(completeTravelRoute.insert(i, destroyedNode).evaluate()), i);
			}
			int index = sortedNodeIndex.entrySet().iterator().next().getValue();
			completeTravelRoute = completeTravelRoute.insert(index, destroyedNode);
		}
		return completeTravelRoute;
	}
}
