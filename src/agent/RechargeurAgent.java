package agent;


import massim.javaagents.Agent;
import massim.javaagents.agents.CityUtil;
import eis.iilang.Action;
import eis.iilang.Percept;

public class RechargeurAgent extends Agent {

	public RechargeurAgent(String name, String team) {
		super(name, team);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Action step() {
		// TODO deliberate and Parameter action	
		for(Percept p : getAllPercepts())
			handlePercept(p);
		
		return CityUtil.action("goto", "facility=shop1");
	}

	@Override
	public void handlePercept(Percept p) {
		// TODO handle percepts if necessary
		if(Agent.getEnvironmentInterface().getAgents() != null){
			for(String agent : Agent.getEnvironmentInterface().getAgents()){
				if(p.getName().equalsIgnoreCase("simStart")){
					
				}
				if(p.getName().equalsIgnoreCase("step")){
					
				}
				if(p.getName().equalsIgnoreCase("steps")){
					
				}
				if(p.getName().equalsIgnoreCase("product")){
					
				}
				if(p.getName().equalsIgnoreCase("role")){
					
				}
				if(p.getName().equalsIgnoreCase("id")){
					
				}
				if(p.getName().equalsIgnoreCase("team")){
					
				}
				if(p.getName().equalsIgnoreCase("entity")){
					
				}
				if(p.getName().equalsIgnoreCase("dump")){
					
				}
				if(p.getName().equalsIgnoreCase("storage")){
					
				}
				if(p.getName().equalsIgnoreCase("charging station")){
					
				}
				if(p.getName().equalsIgnoreCase("shop")){
					
				}
				if(p.getName().equalsIgnoreCase("workshop")){
					
				}
				if(p.getName().equalsIgnoreCase("lat")){
					
				}
				if(p.getName().equalsIgnoreCase("lon")){
					
				}
				if(p.getName().equalsIgnoreCase("fPosition")){
					
				}
				if(p.getName().equalsIgnoreCase("lastAction")){
					
				}
				if(p.getName().equalsIgnoreCase("lastActionParam")){
					
				}
				if(p.getName().equalsIgnoreCase("lastActionResult")){
					
				}
				if(p.getName().equalsIgnoreCase("requestAction")){
					
				}
				if(p.getName().equalsIgnoreCase("routeLength")){
					
				}
				if(p.getName().equalsIgnoreCase("money")){
					
				}
				if(p.getName().equalsIgnoreCase("charge")){
					
				}
				if(p.getName().equalsIgnoreCase("timestamp")){
					
				}
				if(p.getName().equalsIgnoreCase("deadline")){
					
				}
				if(p.getName().equalsIgnoreCase("load")){
					
				}
				if(p.getName().equalsIgnoreCase("inFacility")){
					
				}
			}
		}
	}

}
