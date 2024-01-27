package org.optim4j.ns;

/**
 * Repaires the partially destroyed solution to build a valid solution agent.
 * 
 * @author Avijit Basak
 *
 * @param <T> Partially destroyed solution
 * @param <A> A solution Agent
 */
@FunctionalInterface
public interface Repairer<T, A extends Agent> {

	A repair(T t);
}
