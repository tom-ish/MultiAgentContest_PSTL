package agent;

import eis.iilang.Action;
import eis.iilang.Percept;
import massim.javaagents.Agent;

public class CarAgent extends Agent {

	public CarAgent(String name, String team) {
		super(name, team);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Action step() {
		// TODO deliberate and Parameter action
		return null;
	}

	@Override
	public void handlePercept(Percept p) {
		// TODO handle percepts if necessary
		if(Agent.getEnvironmentInterface().getAgents() != null){
			for(String agent : Agent.getEnvironmentInterface().getAgents()){
				if(p.getName().equalsIgnoreCase("simulation")){
					
				}
				if(p.getName().equalsIgnoreCase("self")){
					
				}
			}
		}
	}

}
