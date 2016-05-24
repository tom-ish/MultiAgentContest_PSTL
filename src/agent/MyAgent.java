package agent;

import apltk.interpreter.data.LogicGoal;
import massim.javaagents.Agent;
import massim.javaagents.agents.CityUtil;
import eis.iilang.Action;
import eis.iilang.Percept;
import eis.iilang.Parameter;


public class MyAgent extends Agent {
	

	public MyAgent(String name, String team) {
		super(name, team);
		// TODO Auto-generated constructor stub
	}

	private boolean startRole = false;
	private Role role = Role.JobSelector;

	@Override
	public Action step() {
		if(role == Role.JobSelector){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			selectJob();
			sortNeededItems();
			
			return new Action("skip");		
		}
		return null;
	}
	
	private void selectJob() {
		int seuil = 0;
		this.clearGoals();
		for(Percept p : getAllPercepts()) {
			if(p.getName().equalsIgnoreCase("auctionJob")){
				for(Parameter param : p.getParameters()){
					System.out.println("======================> "+param.toString());
					// parser les informations contenues dans les jobs
					// <auctionJob id="job_id1" storage="stor1" begin="35" end="85" fine="100" maxBid="200">
					// <pricedJob id="job_id2" storage="stor2" begin="50" end="120" reward="150">
					String[] infos = param.toString().split(" ");
					String jobType = p.getName();
					String begin = infos[2];
					String end = infos[3];
					Integer duration = Integer.valueOf(end) - Integer.valueOf(begin);
					// Si la durée du job est supérieure au seuil défini, c'est que le job est réalisable donc on l'ajoute aux Goals
					if(duration > seuil) {
						// new LogicGoal(String predicate, Collection<String> parameters)
						this.addGoal(new LogicGoal(jobType, param.toString()));
					}
					// sinon on skip le job car on considère qu'il ne pourra pas etre finalisé
					else {
						System.err.println("Job non ajouté");
					}
				}
			}
			else if(p.getName().equalsIgnoreCase("pricedJob")){
				for(Parameter param : p.getParameters()){
					String jobType = p.getName();
				}
			}
		}
	}
	
	private void sortNeededItems() {
		for(Percept p : getAllPercepts()) {
			
		}
	}

	@Override
	public void handlePercept(Percept p) {
		// TODO Auto-generated method stub
		
	}

	
}