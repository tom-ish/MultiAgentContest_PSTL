package agent;

import eis.iilang.Action;
import eis.iilang.Percept;
import massim.javaagents.Agent;
import massim.javaagents.agents.CityUtil;

public class MyAgent extends Agent {

	public MyAgent(String name, String team) {
		super(name, team);
		// TODO do something if necessary
	}

	@Override
	public void handlePercept(Percept arg0) {
		// TODO handle percepts if necessary
		
	}

	@Override
	public Action step() {
		// TODO deliberate and Parameter action
//		return ActionUtils.gotoAction();
		return CityUtil.action("goto", "facility=shop1");
	}

}
