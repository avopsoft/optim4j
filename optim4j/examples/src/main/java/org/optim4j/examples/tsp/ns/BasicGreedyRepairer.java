package org.optim4j.examples.tsp.ns;

import java.util.List;

import org.optim4j.ns.Repairer;

public class BasicGreedyRepairer implements Repairer<PartiallyDestroyedTravelRoute, TravelRoute> {

	@Override
	public TravelRoute repair(PartiallyDestroyedTravelRoute t) {
		TravelRoute partialTravelRoute = t.getPartialTravelRoute();
		List<Node> destroyedNodes = t.getDestroyedNodes();
		
		return null;
	}

}
