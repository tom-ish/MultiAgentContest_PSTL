package launcher;

import java.io.IOException;
import java.util.Collection;

import eis.EILoader;
import eis.EnvironmentInterfaceStandard;
import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.Percept;

public class Launcher {

	public static void main(String[] args) {
		
		/*
		 * 1). Creating an instance of the environment interface
		 */
		EnvironmentInterfaceStandard ei = null;
		try {
			String cn = "massim.eimassim.EnvironmentInterface";
			ei = EILoader.fromClassName(cn);
		}
		catch (IOException e) {
		}
		
		/*
		 * 2). Registering the agents
		 */
		String agentName = "a1"; // a1 a2 a3 a4
		try {
			ei.registerAgent(agentName);
		}
		catch (AgentException e1) {
		}
		
		/*
		 * 3). Associate the agents with the vehicles
		 */
		String entityName = "vehicle1"; // vehicle1 vehicle2 vehicle3 vehicle4
		try {
			ei.associateEntity(agentName, entityName);
		}
		catch (RelationException e) {
		}
		
		/*
		 * 4). Start the execution
		 */
		try {
			ei.start();
		}
		catch (ManagementException e) {
		}
		
		/*
		 * 5). Perceiving the environment
		 */
		try {
			Collection<Percept> ret = getAllPercepts(getName());
			Percept p = new Percept("");
		}
		catch (PerceiveException e) {
			
		}
		catch (NoEnvironmentException e) {
			
		}
		
		/*
		 * 7). Acting
		 */
		Action action = new Action("");
		try {
			ei.performAction(agentName, action);
		}
		catch (ActException e) {
			
		}
	}

}
