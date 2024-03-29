# optim4j - A java library for optimization algorithms
This repository contains a java library for optimization algorithms. Current version of library provides implementation of following algorithms.
## Large Neighborhood Search (LNS): 
**Large Neighborhood Search** algorithm starts with an **initial** solution and searchs its neighborhood to find an optimum solution. The neighborhood size is considered to be large. The algorithm uses a destroy and repair heuristic to explore the domain. The implementation is provided as part of **optim4j-ns** module. The detail UML design is presented [here](https://github.com/avopsoft/optim4j/blob/main/design/optim4j-ns/lns.png).
<br/>The algorithm implements the following **pseudocode**:
<br/>&ensp;**Input:** Problem Domain I
<br/>&ensp;create initial solution a<sub>best</sub> = a ∈ A(I)
<br/>&ensp;**while** stopping criteria not met **do**
<br/>&ensp;&ensp;&ensp;a<sub>neighbor</sub> = repair(destroy(a))
<br/>&ensp;&ensp;&ensp;**if** accept(a, a<sub>neighbor</sub>) **then**
<br/>&ensp;&ensp;&ensp;&ensp;a = a<sub>neighbor</sub>
<br/>&ensp;&ensp;&ensp;&ensp;**if** fitness(a) >= fitness(a<sub>best</sub>) **then**
<br/>&ensp;&ensp;&ensp;&ensp;&ensp;a<sub>best</sub> = a
<br/>&ensp;**return** a<sub>best</sub>
<br/>**Usage:**
>&ensp;&ensp;// *Initialize* an **acceptance criteria**.
<br/>&ensp;&ensp;AcceptanceCriteria *acceptanceCriteria* = new SimulatedAnnealingAcceptanceCriteria(100000, .9);
<br/>&ensp;&ensp;// **Initialize** the **completion condition**.
<br/>&ensp;&ensp;CompletionCondition *completionCondition* = new FixedIteration(100);
<br/>&ensp;&ensp;// *Create* an **initial solution agent**.
<br/>&ensp;&ensp;Agent *initialSolutionAgent* = ...;
<br/>&ensp;&ensp;// *Create* a **destroyer** to create a partially destroyed solution out of a complete valid solution for the respective problem domain.
<br/>&ensp;&ensp;Destroyer<SolutionAgent, PartiallyDestroyedSolutionAgent> *destroyer* = ...;
<br/>&ensp;&ensp;// *Create* a **repairer** to recreate a complete solution from the partially destroyed solution.
<br/>&ensp;&ensp;Repairer<PartiallyDestroyedSolutionAgent, SolutionAgent> *repairer* = ...;
<br/>&ensp;&ensp;// *Create* an instance of **large neighborhood search** optimizer using the initialization parameters.
<br/>&ensp;&ensp;LargeNeighborhoodSearchOptimizer<SolutionAgent, PartiallyDestroyedSolutionAgent> *lnsOptimizer* =
<br/>&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;new LargeNeighborhoodSearchOptimizer<>(acceptanceCriteria, stoppingCondition, repairer, destroyer, observer);
<br/>&ensp;&ensp;// *Invoke* the **optimize** method.
<br/>&ensp;&ensp;Agent *finalSolution* = lnsOptimizer.optimize(initialSolutionAgent);


## Adaptive Large Neighborhood Search (ALNS):
**Adaptive Large Neighborhood Search** algorithm searches a large neighbor following LNS using multiple destroy and repair heuristics to find the optimum solution. The repair and destroy heuristic is chosen based on past performance during optimization. The implementation is provided as part of **optim4j-ns** module. The detail UML design is presented [here](https://github.com/avopsoft/optim4j/blob/main/design/optim4j-ns/alns.png).
<br/>The algorithm implements the following **pseudocode**:
<br/>&ensp;**Input:** Problem Domain I
<br/>&ensp;create initial solution a<sub>best</sub> = a ∈ A(I)
<br/>&ensp;**while** stopping criteria not met **do**
<br/>&ensp;&ensp;&ensp;**adjust** the probabilities p<sub>i</sub> of the destroyer and repairer heuristics **following** configured adjustment period
<br/>&ensp;&ensp;&ensp;**select** repairer and destroyer based on their respective probabilities p<sub>i</sub>
<br/>&ensp;&ensp;&ensp;a<sub>neighbor</sub> = repair(destroy(a))
<br/>&ensp;&ensp;&ensp;**if** accept(a, a<sub>neighbor</sub>) **then**
<br/>&ensp;&ensp;&ensp;&ensp;a = a<sub>neighbor</sub>
<br/>&ensp;&ensp;&ensp;&ensp;**if** fitness(a) >= fitness(a<sub>best</sub>) **then**
<br/>&ensp;&ensp;&ensp;&ensp;&ensp;a<sub>best</sub> = a
<br/>&ensp;&ensp;&ensp;**update** scores of destroyer and repairer heuristics.
<br/>&ensp;**return** a<sub>best</sub>
<br/>**Usage:**
>&ensp;&ensp;// *Initialize* an **acceptance criteria**.
<br/>&ensp;&ensp;AcceptanceCriteria *acceptanceCriteria* = new SimulatedAnnealingAcceptanceCriteria(100000, .9);
<br/>&ensp;&ensp;// *Initialize* the **completion condition**.
<br/>&ensp;&ensp;CompletionCondition *completionCondition* = new FixedIteration(100);
<br/>&ensp;&ensp;// *Create* an **initial solution agent**.
<br/>&ensp;&ensp;Agent *initialSolutionAgent* = ...;
<br/>&ensp;&ensp;// *Create* a **list of destroyers** based on the problem domain.
<br/>&ensp;&ensp;List<Repairer<PartiallyDestroyedSolutionAgent, SolutionAgent>> *repairers* = ...;
<br/>&ensp;&ensp;// *Create* a **list of repairers** based on the problem domain.
<br/>&ensp;&ensp;List<Destroyer<SolutionAgent, PartiallyDestroyedSolutionAgent>> *destroyers* = ...;
<br/>&ensp;&ensp;// *Create* an instance of **adaptive large neighborhood search** optimizer using the initialization parameters.
<br/>&ensp;&ensp;AdaptiveLargeNeighborhoodSearchOptimizer<TravelRoute, PartiallyDestroyedTravelRoute> *alnsOptimizer*
><br/>&ensp;&ensp;&ensp;&ensp;&ensp;&ensp; = new AdaptiveLargeNeighborhoodSearchOptimizer<>(acceptanceCriteria, completionCondition, repairers, destroyers, observer);
<br/>&ensp;&ensp;// *Invoke* the **optimize** method.
<br/>&ensp;&ensp;Agent *finalSolutionAgent* = alnsOptimizer.optimize(initialSolutionAgent);

## Examples:
Examples has been provided for travelling salesman problem as part of **[tsp](https://github.com/avopsoft/optim4j/tree/feature/restructure/sources/examples/tsp)** module.

## References:
1) [Roman Lutz. Adaptive Large Neighborhood Search. Bachelor thesis, Ulm University, 15.08.2014](https://d-nb.info/1072464683/34)
