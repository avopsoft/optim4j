# optim4j - A java library for optimization algorithms
This repository contains a java library for optimization algorithms. Current version of library provides implementation of following algorithms.
# Large Neighborhod Search (LNS): 
Large Neighborhood Search algorithm starts with an initial solution and searchs its neighborhood to find an optimum solution. The neighborhood size is considered to be large. The algorithm uses a destroy and repair heuristic. 
The implementation os provided as part of "optim4j-ns" module.
# Adaptive Large Neighborhod Search (ALNS)
Adaptive Large Neighborhod Search algorithm searches a large neighbor following LNS using multiple destroy and repair heuristics to find the optimum solution. The repair and destroy heurisitc is chosen based on past performance during the course of optimization.
The implementation os provided as part of "optim4j-ns" module.
