package massim.competition2015.configuration;

import static massim.framework.util.DebugLog.LOGLEVEL_ERROR;
import static massim.framework.util.DebugLog.LOGLEVEL_NORMAL;
import static massim.framework.util.DebugLog.log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import massim.competition2015.configuration.FacilityConfiguration.FacilityStock;
import massim.framework.simulation.DefaultSimpleSimulationConfiguration;
import massim.framework.util.XMLCodec;
import massim.server.ServerSimulationConfiguration;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class holds the simulation configuration specified in the XML config file.
 */
public class MapSimulationConfiguration extends DefaultSimpleSimulationConfiguration implements Serializable, ServerSimulationConfiguration, XMLCodec.XMLDecodable{
	
	private static final long serialVersionUID = 5802657031982257279L;	
	
	/**
	 * Tournament's name.
	 */
	public String tournamentName = "";
	
	/**
	 * Simulation's name.
	 */
	public String simulationName = "";
	
	private Vector<String> teamNames;
	
	/**
	 * The max number of steps that this simulation should run if not finalized or interrupted before.
	 */
	public int maxNumberOfSteps;
	
	/**
	 * The number of agents taking part in the simulation.
	 */
	public int numberOfAgents;
	
	/**
	 * The number of teams taking part in the simulation.
	 */
	public int numberOfTeams;
	
	/**
	 * The number of agents taking part in each team.
	 */
	public int agentsPerTeam;
	
	
	/////// Scenario config

	/**
	 * For choosing city-map to use.
	 */
	public String mapName;

	/**
	 * ?
	 */
	public int seedcapital;
	
	/**
	 * The number of agents taking part in each team.
	 */
	public double interest;
	
	/**
	 * Minimum longitude, to define usable area of the map.
	 */
	public double minLon;
	
	/**
	 * Minimum latitude, to define usable area of the map.
	 */
	public double minLat;
	
	/**
	 * Maximum longitude, to define usable area of the map.
	 */
	public double maxLon;
	
	
	/**
	 * Maximum latitude, to define usable area of the map.
	 */
	public double maxLat;
	
	/**
	 * defines how close two locations need to be from each other to be considered the same.
	 */
	public double proximity;
	
	/**
	 * used to define the number of cell between nodes of the map.
	 */
	public double cellSize;
	
	
	//////// Configuration maps
	/*/**
	 * A map from action names to their configurations.
	 */
	// public HashMap<String, ActionConfiguration> actionsConfMap;
	
	
	/**
	 * A set of action names.
	 */
	public Vector<String> actionsNames;
	
	/**
	 * A map from role names to their configurations.
	 */
	public HashMap<String, RoleConfiguration> rolesConfMap;
	
	/**
	 * A map from product names to their configurations.
	 */
	public Vector<ProductConfiguration> productsConf; 
	
	/**
	 * A map from jobs names to their configurations.
	 */
	public Vector<JobConfiguration> jobsConf;
	
	/**
	 * A map from jobs names to their configurations.
	 */
	public Vector<FacilityConfiguration> facilitiesConf; 
	
	
	/**
	 * The seed that will be used for the random graph generator
	 */
	public long randomSeed;


	/* (non-Javadoc)
	 * @see massim.server.ServerSimulationConfiguration#setSimulationName(java.lang.String)
	 */
	public void setSimulationName(String name) {
		simulationName = name;
	}

	/* (non-Javadoc)
	 * @see massim.server.ServerSimulationConfiguration#setTournamentName(java.lang.String)
	 */
	public void setTournamentName(String name) {
		tournamentName = name;
	}

	@Override
	public void setTeamName(int n, String name) {
		if (teamNames == null){
			teamNames = new Vector<String>();
		}
		if (n>= teamNames.size()){
			teamNames.setSize(n+1);
		}
		teamNames.set(n,name);
	}
	
	public Vector<String> getTeamNames() {
		return teamNames;
	}
	
	/*/**
	 * Returns the configuration object of the action whose name is given as a parameter.
	 * @param name The name of the action.
	 * @return an <code>ActionConfiguration</code> object.
	 */
	/* public ActionConfiguration getActionConf(String name){
		return actionsConfMap.get(name);
	} */
	
	/**
	 * Returns the configuration object of the role whose name is given as a parameter.
	 * @param name The name of the role.
	 * @return a <code>RoleConfiguration</code> object.
	 */
	public RoleConfiguration getRoleConf(String name){
		return rolesConfMap.get(name);
	}
	



	/**
	 * Populates this object from the contents of an XML subtree with its root in <code>source</code> (taken
	 * from the configuration file).
	 * @param source
	 */
	@Override
 	public void decodeFromXML(Element source) {
		try{
			maxNumberOfSteps = Integer.decode(source.getAttribute("maxNumberOfSteps"));		
			numberOfAgents  =  Integer.decode(source.getAttribute("numberOfAgents")); 
			numberOfTeams  =   Integer.decode(source.getAttribute("numberOfTeams"));
//			agentsPerTeam  =   Integer.decode(source.getAttribute("agentsPerTeam"));
			seedcapital  =  Integer.decode(source.getAttribute("seedcapital")); 
			interest  =   Double.parseDouble(source.getAttribute("interest"));
			minLon    =   Double.parseDouble(source.getAttribute("minLon"));
			minLat    =   Double.parseDouble(source.getAttribute("minLat"));
			maxLon    =   Double.parseDouble(source.getAttribute("maxLon"));
			maxLat    =   Double.parseDouble(source.getAttribute("maxLat"));
			proximity    =   Double.parseDouble(source.getAttribute("proximity"));
			cellSize    =   Double.parseDouble(source.getAttribute("cellSize"));

		}catch(NumberFormatException e){
			log(LOGLEVEL_ERROR,"Error in config.");
		}
		
		try{
			randomSeed =   Long.decode(source.getAttribute("randomSeed"));
		}catch(NumberFormatException nfe){
			log(LOGLEVEL_NORMAL,"No random seed specified - taking system time.");
			randomSeed = System.currentTimeMillis();
		}
		log(LOGLEVEL_NORMAL,"Seed for map generation: "+randomSeed);
		
		mapName  =  source.getAttribute("map"); 
		
		// actionsConfMap = new HashMap<>();
		rolesConfMap = new HashMap<>();
		productsConf = new Vector<>();
		jobsConf = new Vector<>();
		facilitiesConf = new Vector<>();
		actionsNames = new Vector<>();
		
		
		NodeList nl = source.getChildNodes();
		for (int i=0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if ("actions".equals(n.getNodeName())){
				
				NodeList actionNodes = n.getChildNodes();
				
				for (int j=0; j < actionNodes.getLength(); j++) {
					Node actionNode = actionNodes.item(j);
					if ("action".equals(actionNode.getNodeName())){
						Element actionElement = (Element)actionNode;
						String name = actionElement.getAttribute("name");
						actionsNames.add(name);
						// ActionConfiguration ac = new ActionConfiguration();
						// ac.name = name;
						// actionsConfMap.put(ac.name, ac);
						
					}
				}
			} else if ("roles".equals(n.getNodeName())){
				
				try {
					NodeList roleNodes = n.getChildNodes();
					
					for (int j=0; j < roleNodes.getLength(); j++) {
						Node roleNode = roleNodes.item(j);
						if ("role".equals(roleNode.getNodeName())){
							Element roleElement = (Element)roleNode;
							RoleConfiguration rc = new RoleConfiguration();
							rc.name = roleElement.getAttribute("name");
							rc.speed =           Integer.decode(roleElement.getAttribute("speed"));
							rc.loadCapacity =    Integer.decode(roleElement.getAttribute("loadCapacity"));
							rc.batteryCapacity = Integer.decode(roleElement.getAttribute("batteryCapacity"));
							
							NodeList nl2 = roleElement.getChildNodes();
							for (int k=0; k < nl2.getLength(); k++) {
								Node classNode = nl2.item(k);
								if ("actions".equals(classNode.getNodeName())){
									NodeList actionNodes = classNode.getChildNodes();								
									for (int l=0; l < actionNodes.getLength(); l++) {
										Node actionNode = actionNodes.item(l);
										if ("action".equals(actionNode.getNodeName())){
											Element actionElement = (Element)actionNode;
											rc.actions.add(actionElement.getAttribute("name"));
										}
									}
								} else if ("roads".equals(classNode.getNodeName())){
									NodeList roadNodes = classNode.getChildNodes();								
									for (int l=0; l < roadNodes.getLength(); l++) {
										Node roadNode = roadNodes.item(l);
										if ("road".equals(roadNode.getNodeName())){
											Element roadElement = (Element)roadNode;
											rc.roads.add(roadElement.getAttribute("name"));
										}
									}
								}  else if ("tools".equals(classNode.getNodeName())){
									NodeList toolNodes = classNode.getChildNodes();								
									for (int l=0; l < toolNodes.getLength(); l++) {
										Node toolNode = toolNodes.item(l);
										if ("tool".equals(toolNode.getNodeName())){
											Element toolElement = (Element)toolNode;
											rc.tools.add(toolElement.getAttribute("id"));
										}
									}
								}						
							}
							
							rolesConfMap.put(rc.name, rc);						
						}
					}
				} catch (NumberFormatException e) {
					log(LOGLEVEL_ERROR,"Error in roles config.");
				}
			} else if ("products".equals(n.getNodeName())){
				try {
					NodeList nodes = n.getChildNodes();
					
					for (int j=0; j < nodes.getLength(); j++) {
						Node node = nodes.item(j);
						if ("product".equals(node.getNodeName())){
							
							Element element = (Element)node;
							ProductConfiguration pc = new ProductConfiguration();
							pc.id = element.getAttribute("id");
							pc.volume = Integer.decode(element.getAttribute("volume"));
							pc.userAssembled = Boolean.parseBoolean(element.getAttribute("userAssembled"));
							NodeList nl2 = element.getChildNodes();
							for (int k=0; k < nl2.getLength(); k++) {
								Node reqNode = nl2.item(k);
								if ("requirements".equals(reqNode.getNodeName())){
									NodeList reqNodes = reqNode.getChildNodes();								
									for (int l=0; l < reqNodes.getLength(); l++) {
										Node prodNode = reqNodes.item(l);
										if ("product".equals(prodNode.getNodeName())){
											Element prodElement = (Element)prodNode;
											if (Boolean.parseBoolean(prodElement.getAttribute("consumed"))) {
												pc.itemsConsumed.put(prodElement.getAttribute("id"), 
														Integer.decode(prodElement.getAttribute("amount")));
											} else {
												pc.toolsNeeded.put(prodElement.getAttribute("id"), 
														Integer.decode(prodElement.getAttribute("amount")));
											}
											
										}
									}
								} else if ("location".equals(reqNode.getNodeName())){
									
								}
							}
							productsConf.add(pc);
						}
					}
				} catch (NumberFormatException e) {
					log(LOGLEVEL_ERROR,"Error in products config.");
				}
			} else if ("facilities".equals(n.getNodeName())){
				
				try {
					NodeList nodes = n.getChildNodes();
					for (int j=0; j < nodes.getLength(); j++) {
						Node node = nodes.item(j);
						if ("facility".equals(node.getNodeName())){
							
							Element element = (Element)node;
							FacilityConfiguration fac = new FacilityConfiguration();
							fac.id = element.getAttribute("id");
							fac.type = element.getAttribute("type");
							if("workshop".equals(fac.type)){
								fac.cost = Integer.decode(element.getAttribute("cost"));
							} else if("storage".equals(fac.type)){
								fac.cost = Integer.decode(element.getAttribute("cost"));
								fac.capacity = Integer.decode(element.getAttribute("capacity"));
							} else if("dump".equals(fac.type)){
								fac.cost = Integer.decode(element.getAttribute("cost"));
							} else if("charging".equals(fac.type)){
								fac.cost = Integer.decode(element.getAttribute("cost"));
								fac.rate = Integer.decode(element.getAttribute("rate"));
								fac.concurrent = Integer.decode(element.getAttribute("concurrent"));
							} else if("shop".equals(fac.type)){
								fac.stock = new ArrayList<>();
							}
							
							NodeList nl2 = element.getChildNodes();
							for (int k=0; k < nl2.getLength(); k++) {
								Node reqNode = nl2.item(k);
								if ("products".equals(reqNode.getNodeName()) && "shop".equals(fac.type)){
									NodeList reqNodes = reqNode.getChildNodes();								
									for (int l=0; l < reqNodes.getLength(); l++) {
										Node prodNode = reqNodes.item(l);
										if ("product".equals(prodNode.getNodeName())){
											Element prodElement = (Element)prodNode;
											FacilityStock fs = new FacilityStock();
											fs.id = prodElement.getAttribute("id");
											fs.cost = Integer.decode(prodElement.getAttribute("cost"));
											fs.amount = Integer.decode(prodElement.getAttribute("amount"));
											fs.restock = Integer.decode(prodElement.getAttribute("restock"));
											fac.stock.add(fs);
										}
									}
								} else if ("location".equals(reqNode.getNodeName())){
									Element prodElement = (Element)reqNode;
									fac.lon = Double.parseDouble(prodElement.getAttribute("lon"));
									fac.lat = Double.parseDouble(prodElement.getAttribute("lat"));
								}
							}
							
							facilitiesConf.add(fac);
						}
					}
				} catch (NumberFormatException e) {
					log(LOGLEVEL_ERROR,"Error in facilities config.");
				}

			} else if ("jobs".equals(n.getNodeName())){
				
				try {
					NodeList nodes = n.getChildNodes();
					
					for (int j=0; j < nodes.getLength(); j++) {
						Node node = nodes.item(j);
						if ("job".equals(node.getNodeName())){
							
							Element element = (Element)node;
							JobConfiguration job = new JobConfiguration();
							job.id = element.getAttribute("id");
							job.type = element.getAttribute("type");
							job.storageId = element.getAttribute("storageId");
							job.firstStepActive = Integer.decode(element.getAttribute("firstStepActive"));
							job.lastStepActive = Integer.decode(element.getAttribute("lastStepActive"));
							if("priced".equals(job.type)){
								job.reward = Integer.decode(element.getAttribute("reward"));
							} else if("auction".equals(job.type)){
								job.firstStepAuction = Integer.decode(element.getAttribute("firstStepAuction"));
								job.maxReward = Integer.decode(element.getAttribute("maxReward"));
								job.fine = Integer.decode(element.getAttribute("fine"));
							}
							job.products = new HashMap<>();
							NodeList nl2 = element.getChildNodes();
							for (int k=0; k < nl2.getLength(); k++) {
								Node reqNode = nl2.item(k);
								if ("products".equals(reqNode.getNodeName())){
									NodeList reqNodes = reqNode.getChildNodes();								
									for (int l=0; l < reqNodes.getLength(); l++) {
										Node prodNode = reqNodes.item(l);
										if ("product".equals(prodNode.getNodeName())){
											Element prodElement = (Element)prodNode;
											job.products.put(prodElement.getAttribute("id"),
													Integer.decode(prodElement.getAttribute("amount")));
										}
									}
								}
							}
							
							jobsConf.add(job);
						}
					}
				} catch (NumberFormatException e) {
					log(LOGLEVEL_ERROR,"Error in Jobs config.");
				}
			}
		}
		
	}
	
	
}
