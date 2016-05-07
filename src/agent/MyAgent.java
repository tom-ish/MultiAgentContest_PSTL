package agent;

import java.util.LinkedList;

import org.jdesktop.application.Action.Parameter;

import eis.EIDefaultImpl;
import eis.EILoader;
import eis.iilang.Action;
import eis.iilang.Percept;
import massim.competition2015.monitor.data.AgentInfo;
import massim.eismassim.EnvironmentInterface;
import massim.javaagents.Agent;

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
		return ActionUtils.gotoAction();
	}

}
