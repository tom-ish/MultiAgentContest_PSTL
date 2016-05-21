package agent;

import massim.javaagents.Agent;
import massim.javaagents.agents.CityUtil;
import apltk.interpreter.data.LogicBelief;
import eis.iilang.Action;
import eis.iilang.Percept;

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

		// 1). Parcourir tous les Percepts
		for(Percept percept : getAllPercepts()){

			// 1.1). Mettre à jour les Beliefs
			switch(percept.getName()) {
			case "charge":
				updateChargeBelief(percept);
				break;
				/*
			case "entity":
				updateEntityBelief(percept);
				break;*/
			case "inFacility":
				updateInFacilityBelief(percept);
				break;
			case "item":
				updateItemBelief(percept);
				break;
			case "jobTaken":
				updateJobTakenBelief(percept);
				break;
			case "lat":
				updateLatBelief(percept);
				break;
			case "load":
				updateLoadBelief(percept);
				break;
			case "lon":
				updateLonBelief(percept);
				break;
			case "lastAction":
				updateLastActionBelief(percept);
				break;
			case "lastActionParam":
				updateLastActionParamBelief(percept);
				break;
			case "lastActionResult":
				updateLastActionResultBelief(percept);
				break;
			case "pricedJob":
				updatePricedJobBelief(percept);
				break;
			default:;
			}



			// 1.2). Mettre à jour les Goals
		}


		// 2). Générer l'action à effectuer

		return CityUtil.action("goto", "facility=shop1");
	}

	private void updateChargeBelief(Percept p){
		LogicBelief belief = new LogicBelief(p.getName());
		belief.getParameters().add(p.getParameters().getFirst().toString()); // current energy charge
	}
/*
	private void updateEntityBelief(Percept p){
		LogicBelief belief = new LogicBelief(p.getName());
	}
	*/
	private void updateInFacilityBelief(Percept p){
		LogicBelief belief = new LogicBelief(p.getName());
		belief.getParameters().add(p.getParameters().getFirst().toString()); // facility or none
	}
	
	private void updateItemBelief(Percept p){
		LogicBelief belief = new LogicBelief(p.getName());
		belief.getParameters().add(p.getParameters().get(0).toString()); // item name
		belief.getParameters().add(p.getParameters().get(1).toString()); // amount
	}
	
	private void updateJobTakenBelief(Percept p){
		LogicBelief belief = new LogicBelief(p.getName());
		belief.getParameters().add(p.getParameters().getFirst().toString()); // job id
	}
	
	private void updateLatBelief(Percept p){
		LogicBelief belief = new LogicBelief(p.getName());
		belief.getParameters().add(p.getParameters().getFirst().toString()); // latitude
	}
	
	private void updateLoadBelief(Percept p){
		LogicBelief belief = new LogicBelief(p.getName());
		belief.getParameters().add(p.getParameters().getFirst().toString()); // current load of the agent
	}
	
	private void updateLonBelief(Percept p){
		LogicBelief belief = new LogicBelief(p.getName());
		belief.getParameters().add(p.getParameters().getFirst().toString()); // longitude

	}
	
	private void updateLastActionBelief(Percept p){
		LogicBelief belief = new LogicBelief(p.getName());
		belief.getParameters().add(p.getParameters().getFirst().toString()); // last action id
	}
	
	private void updateLastActionParamBelief(Percept p){
		LogicBelief belief = new LogicBelief(p.getName());
		belief.getParameters().add(p.getParameters().getFirst().toString()); // last action parameters
	}
	
	private void updateLastActionResultBelief(Percept p){
		LogicBelief belief = new LogicBelief(p.getName());
		belief.getParameters().add(p.getParameters().getFirst().toString()); // result of the last action
	}
	
	private void updatePricedJobBelief(Percept p){
		LogicBelief belief = new LogicBelief(p.getName());
		belief.getParameters().add(p.getParameters().getFirst().toString()); // lat
	}


}
