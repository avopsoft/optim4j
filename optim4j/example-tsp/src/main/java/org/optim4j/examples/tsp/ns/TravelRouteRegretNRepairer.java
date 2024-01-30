package org.optim4j.examples.tsp.ns;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.optim4j.ns.Repairer;

public class TravelRouteRegretNRepairer implements Repairer<PartiallyDestroyedTravelRoute, TravelRoute> {

	@Override
	public TravelRoute repair(PartiallyDestroyedTravelRoute partiallyDestroyedTravelRoute) {
		TravelRoute partialTravelRoute = partiallyDestroyedTravelRoute.getPartialTravelRoute();
		int regretDimension = (int) (partialTravelRoute.len() * .2);
		Set<Node> destroyedNodes = new HashSet<>(partiallyDestroyedTravelRoute.getDestroyedNodes());
		if (regretDimension >= partialTravelRoute.len()) {
			throw new IllegalArgumentException(String.format("Invalid regretDimension %d for partial length %d",
					regretDimension, partialTravelRoute.len()));
		}
		TravelRoute completeTravelRoute = partialTravelRoute;
		for (Node destroyedNode : destroyedNodes) {
			int len = completeTravelRoute.len();
			Map<Double, Integer> sortedNodeIndex = new TreeMap<>();
			for (int i = 0; i <= len; i++) {
				sortedNodeIndex.put(Math.abs(completeTravelRoute.insert(i, destroyedNode).evaluate()), i);
			}
			Iterator<Double> itr = sortedNodeIndex.keySet().iterator();
			for (int i = 0; itr.hasNext() && i < regretDimension - 1; i++) {
				itr.next();
			}
			completeTravelRoute = completeTravelRoute.insert(sortedNodeIndex.get(itr.next()), destroyedNode);
		}
		return completeTravelRoute;
	}

}
