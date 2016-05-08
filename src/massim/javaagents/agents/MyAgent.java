package massim.javaagents.agents;

import java.util.Iterator;
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
			if(p.getName().equalsIgnoreCase("auctionJob")){
				
			}
			if(p.getName().equalsIgnoreCase("pricedJob")){
//				System.out.println(p.toXML());
				List<Parameter> list = p.getParameters();
				Parameter id = list.get(0);
				Parameter storage = list.get(1);
				Parameter begin = list.get(2);
				Parameter end = list.get(3);
				Parameter reward = list.get(4);
				Parameter itemName = list.get(5);
//				Parameter amount = list.get(6);
//				if(list.get(7) != null){
//					Parameter delivered = list.get(7);
//				}
				System.out.println("Storage : "+ storage.toString());
				return CityUtil.action("goto", "facility="+storage.toString());
				
			}
			
//			for(Parameter param : list){
//				System.out.println(param.toString());
//				System.out.println("-----------------------------------");
//			}
		}
        
		return CityUtil.action("goto", "facility=shop1");
	}
}
