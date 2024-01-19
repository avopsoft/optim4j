package org.optim4j.examples.tsp.ns;

import java.util.List;

import org.optim4j.ns.Destroyer;

public class TravelRouteSubPathDestroyer implements Destroyer<TravelRoute, PartiallyDestroyedTravelRoute> {

	private int noOfEdgesToBeRemoved;

	public TravelRouteSubPathDestroyer(int noOfEdgesToBeRemoved) {
		this.noOfEdgesToBeRemoved = noOfEdgesToBeRemoved;
	}

	@Override
	public PartiallyDestroyedTravelRoute destroy(TravelRoute travelRoute) {
		final List<Node> representation = travelRoute.getRepresentation();
		int nodeCount = representation.size();

		int startIndex = (int) (Math.random() * representation.size());
		int endIndex = (startIndex + noOfEdgesToBeRemoved) <= nodeCount - 1 ? startIndex + noOfEdgesToBeRemoved
				: startIndex + noOfEdgesToBeRemoved - nodeCount + 1;

		if (startIndex <= endIndex) {
			final List<Node> newRepresentation = representation.subList(0, startIndex + 1);
			newRepresentation.addAll(representation.subList(endIndex + 1, nodeCount));

			return new PartiallyDestroyedTravelRoute(
					new TravelRoute(newRepresentation, travelRoute.getFitnessCalculator()),
					representation.subList(startIndex, endIndex + 1));
		} else {
			final List<Node> destroyedNodes = representation.subList(startIndex, nodeCount);
			destroyedNodes.addAll(representation.subList(0, endIndex + 1));

			return new PartiallyDestroyedTravelRoute(new TravelRoute(representation.subList(endIndex, startIndex + 1),
					travelRoute.getFitnessCalculator()), destroyedNodes);
		}
	}

}
