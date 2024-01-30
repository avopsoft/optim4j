package org.optim4j.examples.tsp.ns;

import java.util.ArrayList;
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
				: (startIndex + noOfEdgesToBeRemoved - nodeCount);

		if (startIndex <= endIndex) {
			final List<Node> newRepresentation = new ArrayList<>(representation.subList(0, startIndex));
			newRepresentation.addAll(representation.subList(endIndex + 1, nodeCount));

			return new PartiallyDestroyedTravelRoute(
					new TravelRoute(newRepresentation, travelRoute.getFitnessCalculator()),
					representation.subList(startIndex, endIndex + 1));
		} else {
			final List<Node> destroyedNodes = new ArrayList<>(representation.subList(startIndex, nodeCount));
			destroyedNodes.addAll(representation.subList(0, endIndex + 1));

			return new PartiallyDestroyedTravelRoute(new TravelRoute(representation.subList(endIndex + 1, startIndex),
					travelRoute.getFitnessCalculator()), destroyedNodes);
		}
	}

}
