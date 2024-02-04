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
import org.optim4j.examples.tsp.ns.destroyer.TravelRouteSubPathDestroyer;
import org.optim4j.examples.tsp.ns.destroyer.TravelRouteWorstEdgeDestroyer;
import org.optim4j.examples.tsp.ns.repairer.TravelRouteBasicGreedyRepairer;
import org.optim4j.examples.tsp.ns.repairer.TravelRouteRegretNRepairer;
import org.optim4j.ns.NeighborhoodSearchOptimizer;
import org.optim4j.ns.acceptancecriteria.SimulatedAnnealingAcceptanceCriteria;
import org.optim4j.ns.completioncond.UnchangedBestFitness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Traveling salesman problem solution using neighborhood search process.
 * 
 * @author Avijit Basak
 */
public class TSPNeighborhoodSearchOptimizer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TSPNeighborhoodSearchOptimizer.class);

	public static void main(String[] args) throws IOException {
		List<Node> nodes = getTravelNodes(args[0]);
		DistanceMatrix distanceMatrix = new DistanceMatrix(nodes);
		Collections.shuffle(nodes);
		TravelRoute travelRoute = new TravelRoute(nodes, new TravelRouteFitnessCalculator(distanceMatrix));
		NeighborhoodSearchOptimizer<TravelRoute, PartiallyDestroyedTravelRoute> neighborhoodSearchOptimizer = new NeighborhoodSearchOptimizer<>(
				new SimulatedAnnealingAcceptanceCriteria(Double.MAX_VALUE, .99), new UnchangedBestFitness(200),
				new TravelRouteBasicGreedyRepairer(), new TravelRouteSubPathDestroyer((int) (nodes.size() * .2)),
				new GraphicalObserver("TSP Optimizer", "generations", "cost"));
		TravelRoute optimizedTravelRoute = neighborhoodSearchOptimizer.optimize(travelRoute);
		LOGGER.info("Optimized route: {}", optimizedTravelRoute);
	}

	private static List<Node> getTravelNodes(String filePath) throws IOException {
		List<Node> nodes = new ArrayList<>();
		CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(' ');
		try (CSVParser parser = new CSVParser(new FileReader(filePath), csvFormat);) {
			CSVRecord csvRecord = null;
			Iterator<CSVRecord> itr = parser.iterator();
			while (itr.hasNext()) {
				csvRecord = itr.next();
				Node node = new Node(Integer.parseInt(csvRecord.get(0)), Double.parseDouble(csvRecord.get(1)),
						Double.parseDouble(csvRecord.get(2)));
				nodes.add(node);
			}
		}
		return nodes;
	}

}
