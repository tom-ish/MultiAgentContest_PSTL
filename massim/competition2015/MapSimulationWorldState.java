package massim.competition2015;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import massim.competition2015.configuration.FacilityConfiguration;
import massim.competition2015.configuration.FacilityConfiguration.FacilityStock;
import massim.competition2015.configuration.JobConfiguration;
import massim.competition2015.configuration.MapSimulationConfiguration;
import massim.competition2015.configuration.ProductConfiguration;
import massim.competition2015.scenario.AuctionJob;
import massim.competition2015.scenario.ChargingStation;
import massim.competition2015.scenario.CityMap;
import massim.competition2015.scenario.DumpLocation;
import massim.competition2015.scenario.Facility;
import massim.competition2015.scenario.Item;
import massim.competition2015.scenario.Job;
import massim.competition2015.scenario.Location;
import massim.competition2015.scenario.PricedJob;
import massim.competition2015.scenario.Shop;
import massim.competition2015.scenario.Shop.ShopStock;
import massim.competition2015.scenario.Storage;
import massim.competition2015.scenario.TeamState;
import massim.competition2015.scenario.Workshop;
import massim.framework.simulation.WorldState;
import massim.gridsimulations.SimulationWorldState;

/**
 * Holds the current state of a map simulation (2015 Map Scenario)
 */
public class MapSimulationWorldState extends SimulationWorldState implements WorldState {

	private static final long serialVersionUID = -6316439899157240323L;

	// World model values


	/**
	 * A map from an agent's name to its current state. It is used as a cache to
	 * provide fast access to this information that is heavily accessed during
	 * the simulation execution.
	 */
	protected Map<String, MapSimulationAgentState> agentNamesMap;

	/**
	 * An ArrayList holding all agents that take part in the simulation.
	 */
	protected ArrayList<MapSimulationAgentState> agents;
	
	/**
	 * A map from an agent's location to its current state. It is used as a cache to
	 * provide fast access to this information that is heavily accessed during
	 * the simulation execution.
	 */
	protected Map<Location, MapSimulationAgentState> agentLocationMap;
	
	
	
	// TODO 2015:: add Javadoc
	protected ArrayList<Facility> facilities;
	protected Map<String, Facility> facilityNamesMap;
	protected Map<Location, Facility> facilityLocatiosMap;
	
	protected ArrayList<Item> items;
	protected Map<String, Item> itemsNamesMap;
	
	protected ArrayList<Job> jobs;
	protected Map<String, Job> jobsNamesMap;
	

	public Map<String, MapSimulationAgentState> getAgentNamesMap() {
		return agentNamesMap;
	}



	public void setAgentNamesMap(Map<String, MapSimulationAgentState> agentNamesMap) {
		this.agentNamesMap = agentNamesMap;
	}



	public Map<Location, MapSimulationAgentState> getAgentLocationMap() {
		return agentLocationMap;
	}



	public void setAgentLocationMap(
			Map<Location, MapSimulationAgentState> agentLocationMap) {
		this.agentLocationMap = agentLocationMap;
	}


	public Collection<Facility> getAllFacilities() {
		return new ArrayList<Facility>(facilities);
	}

	public Facility getFacility(String name) {
		return facilityNamesMap.get(name);
	}



	public void setFacilityNamesMap(Map<String, Facility> facilityNamesMap) {
		this.facilityNamesMap = facilityNamesMap;
	}


	public Facility getFacilityAtLocation(Location loc) {
		return facilityLocatiosMap.get(loc);
	}




	public void setFacilityLocatiosMap(Map<Location, Facility> facilityLocatiosMap) {
		this.facilityLocatiosMap = facilityLocatiosMap;
	}



	public Item getItem(String id) {
		return itemsNamesMap.get(id);
	}



	public void setItemsNamesMap(Map<String, Item> itemsNamesMap) {
		this.itemsNamesMap = itemsNamesMap;
	}


	public Collection<Job> getAllJobs() {
		return new ArrayList<Job>(jobs);
	}
	
	public Map<String, Job> getJobsNamesMap() {
		return jobsNamesMap;
	}



	public void setJobsNamesMap(Map<String, Job> jobsNamesMap) {
		this.jobsNamesMap = jobsNamesMap;
	}

	/**
	 * The configuration of this simulation.
	 */
	protected MapSimulationConfiguration config;

	/**
	 * An ArrayList holding the states of all the teams that take part in the
	 * simulation.
	 */
	public ArrayList<TeamState> teamsStates;

	
	/**
	 * The city map - graph implementation of the map.
	 */
	public CityMap cityMap;

	protected double minLat;
	protected double minLon;
	protected double maxLat;
	protected double maxLon;
	protected double proximity;
	protected double cellSize;
	


	// Used for randomly situating the agents in the map.
	private Random random;

	/**
	 * Creates a simulation state as defined by <code>config</code>
	 *
	 * @param config
	 */
	public MapSimulationWorldState(MapSimulationConfiguration config) {
		this.simulationName = config.simulationName;
		
		this.agents = new ArrayList<>();
		this.agentNamesMap = new HashMap<>();

		this.teamsStates = new ArrayList<>();

		this.config = config;
		
		this.minLat = config.minLat;
		this.minLon = config.minLon;
		this.maxLat = config.maxLat;
		this.maxLon = config.maxLon;

		this.proximity = config.proximity;
		this.cellSize = config.cellSize;
		Location.setProximity(config.proximity);
		
		this.cityMap = new CityMap(config.mapName, cellSize, proximity);
		
		addProducts();
		addFacilities();
		addJobs();

	}



	/**
	 * getter for the ArrayList holding all agents that take part in the
	 * simulation.
	 *
	 * @return
	 */
	public ArrayList<MapSimulationAgentState> getAgents() {
		return this.agents;
	}

	/**
	 * setter for the ArrayList holding all agents that take part in the
	 * simulation.
	 *
	 * @param agents
	 */
	public void setAgents(ArrayList<MapSimulationAgentState> agents) {
		this.agents = agents;
	}

	/**
	 * getter for the configuration object.
	 *
	 * @return
	 */
	public MapSimulationConfiguration getConfig() {
		return this.config;
	}

	/**
	 * Returns the state of an agent given its name.
	 *
	 * @param agentName
	 * @return
	 */
	public MapSimulationAgentState getAgent(String agentName) {
		return this.agentNamesMap.get(agentName);
	}







	// private static int team1Used = 0;
	// private static int team2Used = 0;
	// private static String team1Name = "";
	/**
	 * Adds <code>agent</code> to the currently simulation, and situates it in a
	 * random node in the map.
	 * 
	 * @param agent
	 */
	public void addAgent(MapSimulationAgentState agent, List<Integer> agentPositions, boolean newPosition) {
		
		this.agents.add(agent);
		this.agentNamesMap.put(agent.name, agent);

		if (getTeamNr(agent.team) == -1) {
			int idx = this.teamsStates.size();
			this.teamsStates.add(new massim.competition2015.scenario.TeamState(agent.team, idx));
		}
		agent.teamState = this.getTeamState(agent.team); 
		
	}
	
	public void addProducts() {
		
		this.items = new ArrayList<>();
		this.itemsNamesMap = new HashMap<>();
		
		for (ProductConfiguration prodConf : getConfig().productsConf){
			Item item = new Item();
			item.name = prodConf.id;
			item.volume = prodConf.volume;
			item.userAssembled = prodConf.userAssembled;
			this.items.add(item);
			this.itemsNamesMap.put(item.name, item);
		}
		for (ProductConfiguration prodConf : getConfig().productsConf){
			if (prodConf.userAssembled){
				for(Entry <String,Integer> prodEntry : prodConf.itemsConsumed.entrySet()){
					this.itemsNamesMap.get(prodConf.id).itemsConsumed.put(
							this.itemsNamesMap.get(prodEntry.getKey()),
							prodEntry.getValue());
				}
				for(Entry <String,Integer> prodEntry : prodConf.toolsNeeded.entrySet()){
					this.itemsNamesMap.get(prodConf.id).toolsNeeded.put(
							this.itemsNamesMap.get(prodEntry.getKey()),
							prodEntry.getValue());
				}
			}
		}
		
	}
	
	public void addFacilities() {
		
		this.facilities = new ArrayList<>();
		this.facilityNamesMap = new HashMap<>();
		this.facilityLocatiosMap = new HashMap<>();
		
		for (FacilityConfiguration facConf : getConfig().facilitiesConf){
			
			
			Location location = new Location(facConf.lon,facConf.lat);
			Facility newFac = null;
			if ("shop".equals(facConf.type)){
				Shop sh = new Shop();
				sh.name = facConf.id;
				sh.location = location;
				sh.stock = new HashMap<>();
				for (FacilityStock facStock : facConf.stock){
					ShopStock sStock =  new ShopStock();
					sStock.item = this.itemsNamesMap.get(facStock.id);
					sStock.amount = facStock.amount;
					sStock.cost = facStock.cost;
					sStock.restock = facStock.restock;
					sh.stock.put(sStock.item, sStock);
				}
				newFac = sh;

			} else if ("workshop".equals(facConf.type)){
				Workshop ws = new Workshop();
				ws.name = facConf.id;
				ws.location = location;
				ws.price = facConf.cost;
				newFac = ws;
				
			} else if ("storage".equals(facConf.type)){
				Storage st = new Storage();
				st.name = facConf.id;
				st.location = location;
				st.price = facConf.cost;
				st.totalCapacity = facConf.capacity;
				newFac = st;
				
			} else if ("dump".equals(facConf.type)){
				DumpLocation dl = new DumpLocation();
				dl.name = facConf.id;
				dl.location = location;
				dl.price = facConf.cost;
				newFac = dl;
				
			} else if ("charging".equals(facConf.type)){
				ChargingStation cs = new ChargingStation();
				cs.name = facConf.id;
				cs.location = location;
				cs.chargingRate = facConf.rate;
				cs.fuelPrice = facConf.cost;
				cs.maxConcurrentCharging = facConf.concurrent;
				newFac = cs;
			}
			
			if (newFac != null){
				if (this.facilityNamesMap.containsKey(newFac.name) ||
						this.facilityLocatiosMap.containsKey(newFac.location)){
					throw new Error("Configuration error: facility with duplicated id or location");
				}
				
				this.facilities.add(newFac);
				this.facilityNamesMap.put(newFac.name, newFac);
				this.facilityLocatiosMap.put(newFac.location, newFac);
			}
			
		}
		
	}
	
	public void addJob(Job job) {
		this.jobs.add(job);
		this.jobsNamesMap.put(job.id, job);
	}
	
	public void addJobs() {
		
		this.jobs = new ArrayList<>();
		this.jobsNamesMap = new HashMap<>();
		
		
		for (JobConfiguration jobConf : getConfig().jobsConf){
			Job job = null;
			if ("priced".equals(jobConf.type)){
				PricedJob pJob = new PricedJob(jobConf.id);
				pJob.price = jobConf.reward;
				job=pJob;
				
			} else if ("auction".equals(jobConf.type)){
				AuctionJob aJob = new AuctionJob(jobConf.id);
				aJob.fine=jobConf.fine;
				aJob.maxPrice = jobConf.maxReward;
				aJob.firstStepAuction = jobConf.firstStepAuction;
				job=aJob;
			}
			job.poster = "system";
			job.firstStepActive = jobConf.firstStepActive;
			job.lastStepActive = jobConf.lastStepActive;
			job.storageId = jobConf.storageId;
			Facility storage = this.facilityNamesMap.get(job.storageId);
			if (! (storage instanceof Storage)) {
				throw new Error("Configuration error: non-existing storage for job");
			}
			job.storage = (Storage) storage;
			for (Entry<String,Integer> prodEntry : jobConf.products.entrySet()){
				job.addRequiredItem(
						itemsNamesMap.get(prodEntry.getKey()),
						prodEntry.getValue());
			}
			this.jobs.add(job);
			this.jobsNamesMap.put(job.id, job);
			
		}
		
	}

	/**
	 * Returns a numeric representation of the team name, -1 if the team has not
	 * been added to the teams list of the simulation.
	 * 
	 * The number representation is arbitrary, possibly affected by the order in
	 * which teams are added, and should remain for the duration of the match.
	 * 
	 * @param name
	 * @return
	 */
	// TODO stick to random team numbering?
	public int getTeamNr(String name) {
		massim.competition2015.scenario.TeamState ts = getTeamState(name);
		return ts != null ? ts.teamIdx : -1;
	}

	/**
	 * Provides a numeric representation of the team name, null if the number
	 * does not correspond to any team.
	 * 
	 * The number representation is arbitrary, possibly affected by the order in
	 * which teams are added, and should remain for the duration of the match.
	 * 
	 * @param name
	 * @return
	 */
	// TODO stick to random team numbering?
	public String getTeamName(int number) {
		if (number >= 0 && number < this.teamsStates.size()) {
			assert (this.teamsStates.get(number).teamIdx == number);
			return this.teamsStates.get(number).name;
		}
		return null;
	}

	/**
	 * Returns the state of team given its name.
	 * 
	 * @param name
	 * @return
	 */
	public TeamState getTeamState(String name) {
		for (TeamState ts : this.teamsStates) {
			if (ts.name.equals(name)) {
				return ts;
			}
		}
		return null;
	}

	/**
	 * returns the <code>Random</code> object. It always returns the same object
	 * to avoid creating new ones and initializing them with the same seed in
	 * successive calls that are performed to close together, resulting in
	 * repetition of the random number generated.
	 * 
	 * @return
	 */
	public Random getRandom() {
		if (this.random == null) {
			// random = new Random(System.currentTimeMillis());
			this.random = new Random(this.config.randomSeed);
		}
		return this.random;
	}

}
