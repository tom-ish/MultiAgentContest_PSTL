package agent;

import apltk.interpreter.data.LogicBelief;
import apltk.interpreter.data.LogicGoal;
import apltk.interpreter.data.Message;
import massim.javaagents.Agent;
import massim.javaagents.agents.CityUtil;
import eis.iilang.Action;
import eis.iilang.Percept;
import eis.iilang.Parameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import massim.competition2015.scenario.Location;
import massim.competition2015.scenario.Route;


public class MyAgent extends Agent {

	public MyAgent(String name, String team) {
		super(name, team);
		// TODO Auto-generated constructor stub
	}

	private boolean startRole = false;
	public Role role = Role.JobSelector;
	boolean charging = false;
	public double lon=0;
	public double lat=0;
	public int currentBattery = 0;
	String type = "";

	@Override
	public void handlePercept(Percept p) {
		// TODO Auto-generated method stub

	}

	private void findShop(Collection<Percept> percepts){
		// checkTeamGoals
		LogicGoal purchaseGoal = null;
		for(LogicGoal goal : getGoalBase()){
			// L'agent a été chargé d'acheter une quantité X d'un itemY par le teamManager		
			if(goal.getPredicate().equalsIgnoreCase("buy")){purchaseGoal = goal;}
		}

		// On va parser dans un tableau les items nécessaires à la réalisation du pricedJob
		String reqItemId = null;
		String reqItemQty = null;
		if (purchaseGoal != null){
			reqItemId = purchaseGoal.getParameters().firstElement();
			reqItemQty = purchaseGoal.getParameters().lastElement();
		}else{
			this.addGoal(new LogicGoal("moveto", ""));
		}
		// On veut filtrer les shops qui possèdent les items recherchés
		for(Percept shopPercept : percepts){
			if(shopPercept.getName().equalsIgnoreCase("shop")){
				// items disponibles dans le shop => 'item(itemId)'
				Parameter itemsInShop = shopPercept.getParameters().getLast();
				String[] items = itemsInShop.toString().split(",");
				for(int i = 0; i < items.length; i++){
					// items[i] contient l'id de l'item <=> le nom de l'item
					//items[i] = items[i].substring(items[i].indexOf("(")+1,items[i].length()-1);
					String shopItemId = items[i].substring(items[i].indexOf("(")+1,items[i].length()-1);

					if(shopItemId.equalsIgnoreCase(reqItemId)){
						// Si le shop contient des items nécessaires, on retient son id
						String shopId = shopPercept.getParameters().getFirst().toString();

						// On considère que dès qu'un item est trouvé dans le shop, on va l'acheter
						// on supprime donc l'item à trouver dans la liste des goals
						// goal.getParameters().remove(0);

						// il faut notify aux autres agents qu'on va aller acheter l'item

						// retourner une action
						this.addGoal(new LogicGoal("moveto", shopId));
					}
				}
			}
		}
	}
	
	private void findWorkshop(Collection<Percept> percepts){
		LogicGoal assembleGoal = null;
		for(LogicGoal goal : getGoalBase()){
			// L'agent a été chargé d'acheter une quantité X d'un itemY par le teamManager		
			if(goal.getPredicate().equalsIgnoreCase("assemble")){assembleGoal = goal;}
		}
		
		
		double posLat;
		double posLon;
		Location from = null;
		for(Percept fromPercept : percepts){
			if (fromPercept.getName() == "entity" && fromPercept.getParameters().getFirst().toString() == this.getName()){
				posLat = Double.parseDouble(fromPercept.getParameters().get(2).toString());
				posLon = Double.parseDouble(fromPercept.getParameters().get(3).toString());
				from = new Location(posLon, posLat);
			}
		}
		
		double distanceToWorkshop = Double.MAX_VALUE;
		String workShopId = null;
		for(Percept workshopPercept : percepts){
			if(workshopPercept.getName().equalsIgnoreCase("workshop")){
				String wname = workshopPercept.getParameters().getFirst().toString();
				double wlat = Double.parseDouble(workshopPercept.getParameters().get(2).toString());
				double wlon = Double.parseDouble(workshopPercept.getParameters().get(3).toString());
				
				Location to = new Location(wlon, wlat);
//				CityMap city = new CityMap("london", 0.001, 0.0002);
				
				//				Route r = city.getNewRoute(from, to, permissions);
				Route r = new Route();
				r.addPoint(from);
				r.addPoint(to);

				if(distanceToWorkshop > r.getRouteLength()){
					workShopId = wname;
					distanceToWorkshop = r.getRouteLength();
				}
			}
		}
		
		this.addGoal(new LogicGoal("moveTo", workShopId));
		
	}
	

	@Override
	public Action step() {
		Collection<Percept> allPercepts = getAllPercepts();
		if (this.role == Role.Stockeur){
			// find an adequate Storage location

			// move to storage to drop item

			// send message to team manager
		}

		if (this.role == Role.Acheteur){
			Collection<Message> messageList = getMessages();
			// add corresponding messages to goals
			for (Message msg: messageList){
				LogicBelief msgBelief = (LogicBelief) msg.value;
				if (msgBelief.getPredicate() == "buy"){
					this.addGoal(new LogicGoal(msgBelief.getPredicate(), msgBelief.getParameters())); // set next purchagse objective
				}
				if (msgBelief.getPredicate() == "teamManager"){
					this.addBelief(new LogicBelief("teamManager", msg.sender)); // set the teamManager name
				}		
			}
			
			// calling this function should initialize a goal moveTo(shopId)
			// or moveTo("") if no purchase order was found
			findShop(allPercepts);
			String shopId = null;
			String itemId = null;
			String itemQty = null;
			for(LogicGoal goal : getGoalBase()){
				if (goal.getPredicate() == "moveTo"){
					shopId = goal.getParameters().firstElement();
				}
				if (goal.getPredicate() == "assemble"){
					itemId = goal.getParameters().firstElement();
					itemQty = goal.getParameters().lastElement();
				}
			}
			
			for(Percept percept : allPercepts){
				if(percept.getName().equalsIgnoreCase("inFacility")){
					String facilityName = percept.getParameters().get(0).toString();
					//	System.out.println(facilityName + " vs " + chargingName);
					if(facilityName.startsWith("assemble")){
						// tester la présence d'un assistant si besoin
						// si ok lancer assemble
						
					}
				}
			}
		}
		
		if(this.role == Role.Assembleur){
			// we suppose that from this point, our agent has all the raw materials
			// or that he will join forces so that he does
			
			Collection<Message> messageList = getMessages();
			// add corresponding messages to goals
			for (Message msg: messageList){
				LogicBelief msgBelief = (LogicBelief) msg.value;
				if (msgBelief.getPredicate() == "assemble"){
					this.addGoal(new LogicGoal(msgBelief.getPredicate(), msgBelief.getParameters())); // set next purchagse objective
				}

				if (msgBelief.getPredicate() == "teamManager"){
					this.addBelief(new LogicBelief("teamManager", msg.sender)); // set the teamManager name
				}		
			}
			
			// in the same manner as buyer
			// calling findWorkShop should set a goal "moveTo(workShopId)"
			findWorkshop(allPercepts);
			String workshopId = null;
			String itemId = null;
			String itemQty = null;
			for(LogicGoal goal : getGoalBase()){
				if (goal.getPredicate() == "moveTo"){
					workshopId = goal.getParameters().firstElement();
				}
				if (goal.getPredicate() == "buy"){
					itemId = goal.getParameters().firstElement();
					itemQty = goal.getParameters().lastElement();
				}
			}
			
			

			
			
		}

		if(this.role == Role.Rechargeur){

			if(charging){
				for (Percept p : allPercepts){
					if (p.getName().equalsIgnoreCase("charge")){
						System.out.println("BATTERY LEVEL " + p.toString());
						return CityUtil.action("continue");
					}
				}
			}else{
				ArrayList<Percept> chargingStationList = this.getAllChargingStations(allPercepts);
				Location from = new Location(lon, lat);

				String chargingName = this.getNearestFacility(allPercepts, from, chargingStationList);


				for(Percept percept : allPercepts){
					if(percept.getName().equalsIgnoreCase("inFacility")){
						String facilityName = percept.getParameters().get(0).toString();
						//	System.out.println(facilityName + " vs " + chargingName);
						if(facilityName.startsWith("charging")){
							//	this.role = Role.JobSelector;
							System.out.println("IN CORRECT CHARGING STATION");
							charging = true;
							return CityUtil.action("charge");
						}else{
							return CityUtil.action("goto", "facility="+chargingName);
						}
					}
				}

				//return CityUtil.action("goto", "facility="+chargingName);
			}

			return CityUtil.action("skip");
		}

		if(role == Role.JobSelector){
			// selectJob() termine en changeant le role de l'agent
			selectJob(allPercepts);
		}
		if(role == Role.TeamManager){
			sortNeededItems(allPercepts);
		}
		return null;
	}

	/********     JOBSELECTOR      ********/
	/********* FONCTIONS PRIVEES **********/

	private void selectJob(Collection<Percept> allPercepts) {
		int seuil = 0;
		this.clearGoals();
		for(Percept p : allPercepts) {
			String begin = "";
			String end = "";
			if(p.getName().equalsIgnoreCase("auctionJob")){
				for(int i = 0; i < p.getParameters().size(); i++) {
					switch(i){
					case 2: // begin
						begin = p.getParameters().get(i).toString();
						break;
					case 3: // end
						end = p.getParameters().get(i).toString();
						break;
					}
				}
				Integer duration = Integer.valueOf(end) - Integer.valueOf(begin);
				// Si la durée du job est supérieure au seuil défini, c'est que le job est réalisable donc on l'ajoute aux Goals
				if(duration > seuil) {
					// new LogicGoal(String predicate, Collection<String> parameters)
					this.addGoal(new LogicGoal(p.getName(), p.getParameters().toString()));
				}
				// sinon on skip le job car on considère qu'il ne pourra pas etre finalisé
				else {}
			}
			else if(p.getName().equalsIgnoreCase("pricedJob")){
				for(int i = 0; i < p.getParameters().size(); i++) {
					switch(i){
					case 2: // begin
						begin = p.getParameters().get(i).toString();
						break;
					case 3: // end
						end = p.getParameters().get(i).toString();
						break;
					}
				}
				Integer duration = Integer.valueOf(end) - Integer.valueOf(begin);
				// Si la durée du job est supérieure au seuil défini, c'est que le job est réalisable donc on l'ajoute aux Goals
				if(duration > seuil) {
					// new LogicGoal(String predicate, Collection<String> parameters)
					this.addGoal(new LogicGoal(p.getName(), p.getParameters().toString()));
				}
				// sinon on skip le job car on considère qu'il ne pourra pas etre finalisé
				else {}
			}
		}
		System.out.println(" -------------- ");
		for(LogicGoal goal : getGoalBase())
			System.out.println(goal.toString());
		System.out.println(" -------------- ");

		// Une fois tous les jobs ajoutés, le jobSelector devient un TeamManager
		this.role = Role.TeamManager;
	}

	private void sortNeededItems(Collection<Percept> allPercepts) {
		for(LogicGoal goal : getGoalBase()) {
			if(goal.getPredicate().equalsIgnoreCase("pricedJob")) {

			}
			else if(goal.getPredicate().equalsIgnoreCase("auctionJob")) {

			}
		}
	}

	/********     RECHARGEUR     ********/
	/******** FONCTIONS PRIVEES *********/
	private ArrayList<Percept> getAllChargingStations(Collection<Percept> percepts){
		ArrayList<Percept> chargingStations = new ArrayList<Percept>();
		for(Percept percept : percepts){
			if(percept.getName().equalsIgnoreCase("chargingStation")){
				chargingStations.add(percept);
			}
		}

		//		System.out.println("GET ALL CHARGIN STATIONS");
		//		for (Percept p : chargingStations){
		//			System.out.println("==== " + p.toString());
		//		}
		return chargingStations;
	}

	private String getNearestFacility(Collection<Percept> percepts, Location from, ArrayList<Percept> facilities){
		int dist = Integer.MAX_VALUE;
		double clon=0,clat=0;
		String chargeStationName = "";
		Set<String> permissions = new HashSet<String>();
		permissions.add("air");
		permissions.add("road");

		for(Percept p : percepts){
			//			System.out.println("YO getNearestFacility");
			if(p.getName().equalsIgnoreCase("chargingStation") ||
					p.getName().equalsIgnoreCase("shop")||
					p.getName().equalsIgnoreCase("dump")||
					p.getName().equalsIgnoreCase("storage")||
					p.getName().equalsIgnoreCase("workshop")){
				chargeStationName = p.getParameters().get(0).toString();
				clat = Double.parseDouble(p.getParameters().get(1).toString());
				clon = Double.parseDouble(p.getParameters().get(2).toString());
				Location to = new Location(clon, clat);

				//				CityMap city = new CityMap("london", 0.001, 0.0002);

				//				Route r = city.getNewRoute(from, to, permissions);
				Route r = new Route();
				r.addPoint(from);
				r.addPoint(to);

				if(dist>r.getRouteLength()){
					clat = Double.parseDouble(p.getParameters().get(1).toString());
					clon = Double.parseDouble(p.getParameters().get(2).toString());
					dist = r.getRouteLength();
					chargeStationName = p.getParameters().get(0).toString();
				}
			}
		}

		return chargeStationName;
	}

	private boolean checkBattery(Location from, Location to){

		int speed = 0;

		Route route = new Route();
		route.addPoint(to);
		route.addPoint(from);

		for(Percept percept : getAllPercepts()){


			//Recuperer le role et la position de l'agent
			if(percept.getName().equalsIgnoreCase("entity")){
				if(this.getName().compareTo(percept.getParameters().get(0).toString())==0){
					type = percept.getParameters().get(4).toString();
					lat = Integer.parseInt(percept.getParameters().get(2).toString());
					lon = Integer.parseInt(percept.getParameters().get(3).toString());

				}
			}

			//recuperer sa vitesse en focntion de son role
			if(percept.getName().equalsIgnoreCase("role")){
				if(type.compareTo(percept.getParameters().get(0).toString())==0){
					speed = Integer.parseInt(percept.getParameters().get(1).toString());
				}
			}

			//recuperer la battery courante de l'agent
			if(percept.getName().equalsIgnoreCase("charge")){
				currentBattery = Integer.parseInt(percept.getParameters().get(0).toString());
			}
		}

		//Si la battery de l'agent est inferieur à la longueur de la route plus un certain seuil
		//on change son role en Rechargeur
		if(currentBattery<= route.getRouteDuration(speed)){
			this.role = Role.Rechargeur;
			return false;
		}

		return true;
	}
}

