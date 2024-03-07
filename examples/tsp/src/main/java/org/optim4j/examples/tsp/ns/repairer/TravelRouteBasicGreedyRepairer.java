package org.optim4j.examples.tsp.ns.repairer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.optim4j.examples.tsp.ns.Node;
import org.optim4j.examples.tsp.ns.PartiallyDestroyedTravelRoute;
import org.optim4j.examples.tsp.ns.TravelRoute;
import org.optim4j.ns.Repairer;

/**
 * A basic greedy repairer for partially destroyed travel route.
 * 
 * @author Avijit Basak
 */
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
