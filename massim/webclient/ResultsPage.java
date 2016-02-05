/**
 * 
 */
package massim.webclient;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class generates the result page, both for the tomcat and for the static version created by createResultsPage.java 
 */
public class ResultsPage {
	public static void calculateResults(Document xmlDocument) {
		Element docroot = xmlDocument.getDocumentElement();

		if ( docroot == null ) 
		{
			//throw new ServletException("Error parsing configuration: Missing root element.");
		}
		else
		{

			NodeList allTeams = docroot.getElementsByTagName("team"); //NodeList with all teams

			// generates a teamlist
			TeamData[] teamList = new TeamData[allTeams.getLength()];

			String red, blue; //string for red and blue teamname
			int redTeamNb = 0, blueTeamNb = 0; //variable for red and blue teamnumber

			int i = 0;

			while (i < allTeams.getLength())// for each team from NodeList
			{
				Element team = (Element) allTeams.item(i);

				teamList[i] = new TeamData(team.getAttribute("name"),i); //set teamname and id of each team in teamlist
				i++;
			}

			NodeList allMatches = docroot.getElementsByTagName("match"); // NodeList containing all matches
			int j = 0;
			while (j < allMatches.getLength()) // for all matches ...
			{
				Element match = (Element) allMatches.item(j);

				red = match.getAttribute("red"); // get red team
				for (int k = 0; k < allTeams.getLength(); k++) {
					if (teamList[k].getTeamname().equals(red))
						redTeamNb = teamList[k].getID(); // and red teams number
				}

				blue = match.getAttribute("blue"); // get blue team
				for (int k = 0; k < allTeams.getLength(); k++) {
					if (teamList[k].getTeamname().equals(blue))
						blueTeamNb = teamList[k].getID(); // and blues team number
				}

				// count matches for blue and for red
				teamList[redTeamNb].increaseMatchCounter();
				teamList[blueTeamNb].increaseMatchCounter();

				NodeList allSimulations = match.getElementsByTagName("simulation"); //get all simulations belonging to "match"

				//set initial score and points of red and blue team
				double scoreRed = 0;
				double scoreBlue = 0;
				int pointsRed = 0, pointsBlue = 0;

				int sim = 0; // for all simulations ...
				while (sim < allSimulations.getLength()) {
					double simScoreRed = 0, simScoreBlue = 0;//set initial score of the simulation for each team
					Element simulation = (Element) allSimulations.item(sim);

					NodeList results = simulation.getElementsByTagName("result"); // get simulation result

					Element r = (Element) results.item(0);

					simScoreRed = Double.parseDouble(r.getAttribute(red)); // get score of red team
					scoreRed += simScoreRed;//add reds score from current simulation to reds score for this match

					simScoreBlue = Double.parseDouble(r.getAttribute(blue)); // get score of blue team
					scoreBlue += simScoreBlue; //add blues score from current simulation to blues score for this match

					//find out who has won the simulation and set the points
					if (simScoreRed == simScoreBlue) // simulation ended draw 
					{
						pointsRed += 1;
						pointsBlue += 1;
					} else if (simScoreRed >= simScoreBlue) // red has won simulation
					{
						pointsRed += 3;
					} else { //blue has won simulation
						pointsBlue += 3;
					}
					sim++;
				}

				//                	change number of collected items and points in the teamlist for the appropriate team
				teamList[redTeamNb].setGoldCollected(teamList[redTeamNb].getGoldCollected()+scoreRed);
				teamList[redTeamNb].setGoldCollectedEnemy(teamList[redTeamNb].getGoldCollectedEnemy()+scoreBlue);
				teamList[redTeamNb].setsimulationPoints(teamList[redTeamNb].getsimulationPoints()+pointsRed);
				teamList[redTeamNb].setsimulationPointsEnemy(teamList[redTeamNb].getsimulationPointsEnemy()+pointsBlue);


				teamList[blueTeamNb].setGoldCollected(teamList[blueTeamNb].getGoldCollected()+scoreBlue);
				teamList[blueTeamNb].setGoldCollectedEnemy(teamList[blueTeamNb].getGoldCollectedEnemy()+scoreRed);
				teamList[blueTeamNb].setsimulationPoints(teamList[blueTeamNb].getsimulationPoints()+pointsBlue);
				teamList[blueTeamNb].setsimulationPointsEnemy(teamList[blueTeamNb].getsimulationPointsEnemy()+pointsRed);

				j++;
			}


			Element ranking = xmlDocument.createElement("ranking");//creates a new element for the xml-tournament-report-file
			for( int a=0; a<allTeams.getLength(); a++)//for every team create an element "rank" with attributes position, teamname, owngold, enemygold, diffgold, ownpoints and enemypoints
			{
				Element rank = xmlDocument.createElement("rank");
				rank.setAttribute("position",Integer.toString(position(teamList, a, allTeams.getLength() )));
				rank.setAttribute("teamname", teamList[a].getTeamname());
				rank.setAttribute("ownGold", Double.toString(teamList[a].getGoldCollected()));
				rank.setAttribute("enemyGold", Double.toString(teamList[a].getGoldCollectedEnemy()));
				rank.setAttribute("diffGold", Double.toString(teamList[a].getGoldCollected() - teamList[a].getGoldCollectedEnemy() ));
				rank.setAttribute("ownPoints", Integer.toString(teamList[a].getsimulationPoints()));
				rank.setAttribute("enemyPoints", Integer.toString(teamList[a].getsimulationPointsEnemy()));
				rank.setAttribute("played", Integer.toString(teamList[a].getMatchCounter()));
				ranking.appendChild(rank);
			}
			docroot.appendChild(ranking);
		}
	}
	
	/**
	 * This methods "calculates" the current position of each team in the
	 * ranking.
	 * 
	 * @param teamList
	 *            list of all teams
	 * @param teamnumber
	 *            number of team for which the position is "calculated"
	 * @param numberOfTeams
	 *            number of participating teams
	 * @return position of team in the ranking
	 */
	public static int position(TeamData[] teamList, int teamnumber,
			int numberOfTeams) {
		int position = 1;
		for (int i = 0; i < numberOfTeams; i++) {
			if (i != teamnumber) {
				if (teamList[teamnumber].getPoints() > teamList[i].getPoints()) {
					// place doesn't change. team with teamnumber has more
					// points than other team
				} else if (teamList[teamnumber].getPoints() < teamList[i]
						.getPoints()) {
					position++; // there is one team that has more points as
								// team with teamnumber
				} else if ((teamList[teamnumber].getPoints() == teamList[i]
						.getPoints())
						&& ((teamList[teamnumber].getGoldCollected() - teamList[teamnumber]
								.getGoldCollectedEnemy()) > (teamList[i]
								.getGoldCollected() - teamList[i]
								.getGoldCollectedEnemy()))) {
					// place doesn't change. team with teamnumber has collected
					// more gold than other team
				} else if ((teamList[teamnumber].getPoints() == teamList[i]
						.getPoints())
						&& ((teamList[teamnumber].getGoldCollected() - teamList[teamnumber]
								.getGoldCollectedEnemy()) < (teamList[i]
								.getGoldCollected() - teamList[i]
								.getGoldCollectedEnemy()))) {
					position++; // other team has collected more gold than team
								// with teamnumber
				}
			}
		}
		return position;
	}	
}
