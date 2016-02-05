package massim.webclient;
/**
 * This class is represents a team-object where information about 
 * each team is saved and provided to other classes. 
 * This class is used by CreateResultsPage and WebClientTournamentServlet. 
 * 
 * @author Dominik Steinborn
 *
 */
public class TeamData {

	private String teamname;
	private double goldCollected = 0; //total number of gold-items collected by the team 
	private double goldCollectedEnemy = 0; //total number of gold-items collected by the enemy-teams in simulation against team
	private int simulationPoints = 0; //total points of team
	private int simulationPointsEnemy = 0; //total points of enemy-team received in simulations against team
	private int points = 0; //the same as simulationPoints
	private int id; //team id
	
	private int matchCounter = 0;
	
	public void increaseMatchCounter() {
		this.matchCounter++;
	}
	
	public int getMatchCounter() {
		return this.matchCounter;
	}
	
	
	public TeamData(String teamname, int id)
	{
		this.teamname = teamname;
		this.id = id;
	}
	
	public String getTeamname()
	{
		return teamname;
	}
	
	public double getGoldCollected()
	{
		return goldCollected;
	}
	
	public double getGoldCollectedEnemy()
	{
		return goldCollectedEnemy;
	}
	
	public int getsimulationPoints()
	{
		return simulationPoints;
	}
	
	public int getsimulationPointsEnemy()
	{
		return simulationPointsEnemy;
	}
	
	public int getPoints()
	{
		return points;
	}
	
	public int getID()
	{
		return id;
	}
	
	public void setGoldCollected(double goldCollected)
	{
		this.goldCollected = goldCollected;
	}
	
	public void setGoldCollectedEnemy(double goldCollectedEnemy)
	{
		this.goldCollectedEnemy = goldCollectedEnemy;
	}
	
	public void setsimulationPoints(int simulationPoints)
	{
		this.simulationPoints = simulationPoints;
		points = this.simulationPoints;
	}
	
	public void setsimulationPointsEnemy(int simulationPointsEnemy)
	{
		this.simulationPointsEnemy = simulationPointsEnemy;
	}
}
