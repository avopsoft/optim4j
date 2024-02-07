package org.optim4j.examples.tsp.ns;

import java.util.List;

/**
 * An encapsulation of partially destroyed travel route and removed nodes.
 * 
 * @author Avijit Basak
 */
public class PartiallyDestroyedTravelRoute {

	private TravelRoute partialTravelRoute;

	private List<Node> destroyedNodes;

	public PartiallyDestroyedTravelRoute(TravelRoute partialTravelRoute, List<Node> destroyedNodes) {
		this.partialTravelRoute = partialTravelRoute;
		this.destroyedNodes = destroyedNodes;
	}

	public TravelRoute getPartialTravelRoute() {
		return partialTravelRoute;
	}

	public void setPartialTravelRoute(TravelRoute partialTravelRoute) {
		this.partialTravelRoute = partialTravelRoute;
	}

	public List<Node> getDestroyedNodes() {
		return destroyedNodes;
	}

	public void setDestroyedNodes(List<Node> destroyedNodes) {
		this.destroyedNodes = destroyedNodes;
	}

}
