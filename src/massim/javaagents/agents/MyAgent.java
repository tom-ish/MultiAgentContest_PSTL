package massim.javaagents.agents;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import massim.javaagents.Agent;

public class MyAgent extends Agent {

	
	public MyAgent(String name, String team) {
		super(name, team);
		// TODO do something if necessary
	}

	@Override
	public void handlePercept(Percept arg0) {
		// TODO handle percepts if necessary
		if(arg0.getName().equalsIgnoreCase("simStart")){
			
		}
	}

	@Override
	public Action step() {
		for(Percept p : getAllPercepts()){
			
			List<Parameter> list = p.getParameters();
			for(Parameter param : list){
				System.out.println(param.toString());
				System.out.println("-----------------------------------");
			}
		}
        
		return CityUtil.action("goto", "facility=shop1");
	}
}
