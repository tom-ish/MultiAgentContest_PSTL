package massim.javaagents.agents;

import eis.iilang.Action;
import eis.iilang.Percept;
import massim.javaagents.Agent;

public class FirstAgent extends Agent {

	public FirstAgent(String name, String team) {
		super(name, team);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Action step() {
		// TODO Auto-generated method stub
		return new Action("goto");
	}

	@Override
	public void handlePercept(Percept p) {
		// TODO Auto-generated method stub
		
	}

}
