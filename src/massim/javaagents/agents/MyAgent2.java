package massim.javaagents.agents;

import eis.iilang.Action;
import eis.iilang.Percept;
import massim.javaagents.Agent;

public class MyAgent2 extends Agent {

	public MyAgent2(String name, String team) {
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
		return CityUtil.action("goto", "facility=workshop1");
	}

}
