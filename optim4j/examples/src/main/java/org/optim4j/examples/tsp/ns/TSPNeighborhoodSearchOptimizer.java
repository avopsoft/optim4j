package org.optim4j.examples.tsp.ns;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.optim4j.ns.NeighborhoodSearchOptimizer;
import org.optim4j.ns.acceptancecriteria.SimulatedAnnealingAcceptanceCriteria;
import org.optim4j.ns.completioncond.UnchangedBestFitness;

public class TSPNeighborhoodSearchOptimizer {

	public static void main(String[] args) throws IOException {
		List<Node> nodes = getTravelNodes(args[0]);
		DistanceMatrix distanceMatrix = DistanceMatrix.getInstance();
		distanceMatrix.initialize(nodes);
		Collections.shuffle(nodes);
		TravelRoute travelRoute = new TravelRoute(nodes, new TravelRouteFitnessCalculator());
		NeighborhoodSearchOptimizer<TravelRoute, PartiallyDestroyedTravelRoute> neighborhoodSearchOptimizer = new NeighborhoodSearchOptimizer<>(
				new SimulatedAnnealingAcceptanceCriteria(.01), new UnchangedBestFitness(5000),
				new TravelRouteRandomRepairer(), new TravelRouteRandomDestroyer((int) (nodes.size() * .1)),
				new GraphicalObserver("TSP Optimizer", "generations", "cost"), "TSP");
		TravelRoute optimizedTravelRoute = neighborhoodSearchOptimizer.optimize(travelRoute);
		System.out.println(optimizedTravelRoute);
	}

	private static List<Node> getTravelNodes(String filePath) throws IOException {
		List<Node> nodes = new ArrayList<Node>();
		CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(' ');
		try (CSVParser parser = new CSVParser(new FileReader(filePath), csvFormat);) {
			CSVRecord record = null;
			Iterator<CSVRecord> itr = parser.iterator();
			while (itr.hasNext()) {
				record = itr.next();
				Node node = new Node(Integer.parseInt(record.get(0)), Double.parseDouble(record.get(1)),
						Double.parseDouble(record.get(2)));
				nodes.add(node);
			}
		}
		return nodes;
	}

}
