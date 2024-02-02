package org.optim4j.examples.tsp.ns.repairer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.optim4j.examples.tsp.ns.Node;
import org.optim4j.examples.tsp.ns.PartiallyDestroyedTravelRoute;
import org.optim4j.examples.tsp.ns.TravelRoute;
import org.optim4j.ns.Repairer;

/**
 * Repairer of travel route in a random fashion.
 * 
 * @author Avijit Basak
 */
public class TravelRouteRandomRepairer implements Repairer<PartiallyDestroyedTravelRoute, TravelRoute> {

	public TravelRoute repair(PartiallyDestroyedTravelRoute t) {
		final TravelRoute partialTravelRoute = t.getPartialTravelRoute();
		final List<Node> nodes = t.getDestroyedNodes();
		final List<Node> partialRouteNodes = partialTravelRoute.getRepresentation();
		final List<Node> newRouteNodes = new ArrayList<>(partialRouteNodes);
		newRouteNodes.addAll(nodes);
		Collections.shuffle(newRouteNodes);

		return new TravelRoute(newRouteNodes, partialTravelRoute.getFitnessCalculator());
	}

}
