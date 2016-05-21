package agent;

import java.util.List;

import massim.javaagents.Agent;
import eis.iilang.Action;
import eis.iilang.Parameter;
import eis.iilang.Percept;

public class ExplorateurAgent extends Agent {

	public ExplorateurAgent(String name, String team) {
		super(name, team);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Action step() {
		// TODO Auto-generated method stub
		
		for(Percept p : getAllPercepts()){

			List<Parameter> list = p.getParameters();
			for(Parameter param : list){
				System.out.println(param.toString());
				System.out.println("-----------------------------------");
			}
		}
		
		return null;
	}

	@Override
	public void handlePercept(Percept p) {
		// TODO Auto-generated method stub
		
		
		/*
		String xml = p.toXML();
		String[] tokens = xml.split(" ");
		String identifier = tokens[1];
		String[] values = identifier.split("=");
		String facility = values[1];
		String id = facility.substring(facility.indexOf("\""), facility.indexOf("\"", facility.indexOf("\"")+1));
		*/
	}

}
