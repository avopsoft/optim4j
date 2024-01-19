package org.optim4j.ns;

public interface Observer {

	void notify(Agent agent, String name, int generation);
}
