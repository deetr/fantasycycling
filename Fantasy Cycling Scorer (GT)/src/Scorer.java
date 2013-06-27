import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;

/*
 * 
 */
public class Scorer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String path = "D:/Fantasy/tour2012/";

		HashMap<Integer, Rider> riders = new HashMap<Integer, Rider>(); 

		try {
			BufferedReader reader = new BufferedReader(new FileReader(path+"riders.txt"));
			String line = reader.readLine();
			while(line != null) {
				if(!line.isEmpty() && !line.contains("Manager") && !line.contains(",,")) {
					String[] lineElems = line.split(",");
					riders.put(Integer.parseInt(lineElems[0].trim()), new Rider(Integer.parseInt(lineElems[0]), lineElems[1].trim()));
				}
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Rider rider : riders.values()) {
			System.out.println(rider);
			rider.init();
		}

		ArrayList<Team> teams = new ArrayList<Team>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(path+"teams.txt"));
			String line = reader.readLine();
			while(line != null) {
				if(!line.isEmpty()) {
					String[] firstSplit = line.split("=");
					Team tempTeam = new Team(firstSplit[1], firstSplit[0]);
					for (String rNumber : firstSplit[2].split(",")) {
						tempTeam.addRider(riders.get(Integer.parseInt(rNumber)));
					}
					teams.add(tempTeam);

				}
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//process substitutions
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path+"subs.txt"));
			String line = reader.readLine();
			while(line != null) {
				if(!line.isEmpty()) {
					String[] firstSplit = line.split("=");
					Team tempTeam = null;
					for(Team team : teams)
						if(team.getManager().equals(firstSplit[0]))
							tempTeam = team;
					String[] individualSub = firstSplit[1].split(",");
					for (String sub : individualSub) {
						String[] stageSplit = sub.split(":");
						String[] subSplit = stageSplit[0].split("-");
						int stage = Integer.parseInt(stageSplit[1]);
						int subOut = -1;
						if (!subSplit[0].equals(""))
							subOut = Integer.parseInt(subSplit[0]);
						int subIn = Integer.parseInt(subSplit[1]);
						for (Rider rider : tempTeam.getRiders()) {
							if(rider.getNumber() == subOut)
								rider.setWithdrawnStage(stage);
							else if(rider.getNumber() == subIn)
								rider.setSubstitutedStage(stage);
						}
					}
				}					
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Team team : teams) {
			System.out.println("Team: " + team.getTeamName());
			System.out.println("Manager: " + team.getManager());
			for (Rider rider : team.getRiders()) {
				System.out.println(rider);
			}
		}
		//get the scores
		int stages = 0;
		File file = new File(path);
		for (File looker : file.listFiles()) {

			if(!looker.isHidden() & looker.isDirectory()) {
				System.out.println(looker.getAbsolutePath());
				for (File data : looker.listFiles()) {
					if(!data.isHidden()) {
						System.out.println(data.getName());
						try {
							BufferedReader reader = new BufferedReader(new FileReader(data.getAbsolutePath()));
							String line = reader.readLine();

							while(line != null) {
								if(!data.getName().contains("html")) {//line.contains("pts") || line.contains("GC") || data.getName().contains("mod")) {
									String[] tmpStr = null;
									String[] tmpPoints = null;
									int number = -1;
									if(!line.isEmpty() && !line.contains("Km") && !(line.contains("RANK,RIDER")
											|| line.contains("Pos,Dor.") || line.contains("POSN.,,") || line.contains("Pos.,Dor.")) && !line.contains(",,,,")) {

										if(data.getName().contains("mod")) {
											tmpStr = line.split("=");
											tmpPoints = tmpStr;
											number = Integer.parseInt(tmpStr[0]);
										} else {
											tmpStr = line.split(",");
											tmpPoints = tmpStr[4].split(" ");
											number = Integer.parseInt(tmpStr[2].substring(0));
										}
										Rider pointOwner = riders.get(number);
										String stageNumberStr = looker.getName().substring(looker.getName().indexOf("e")+1);
										int stageNumber = Integer.parseInt(stageNumberStr);
										if(stageNumber > stages)
											stages = stageNumber;
										if((pointOwner.isSubstitute() && pointOwner.getSubstitutedStage() < stageNumber+1)
												|| !pointOwner.isSubstitute()) {
											int prevPoints = pointOwner.getScores()[stageNumber].getPoints();
											int prevClimbing = pointOwner.getScores()[stageNumber].getClimbing();
											boolean prevGC = pointOwner.getScores()[stageNumber].getGC();
											int prevModifier = pointOwner.getScores()[stageNumber].getModifier();
											if(data.getName().contains("gc")) {
												pointOwner.setScore(new Points(prevPoints, prevClimbing, true), stageNumber);
												pointOwner.getScores()[stageNumber].setModifier(prevModifier);
											} else if(data.getName().contains("point")) {
												pointOwner.setScore(new Points(Integer.parseInt(tmpPoints[0].trim())+prevPoints, prevClimbing, prevGC), stageNumber);
												pointOwner.getScores()[stageNumber].setModifier(prevModifier);
											} else if(data.getName().contains("climb")) {
												pointOwner.setScore(new Points(prevPoints, Integer.parseInt(tmpPoints[0].trim())+prevClimbing, prevGC), stageNumber);
												pointOwner.getScores()[stageNumber].setModifier(prevModifier);
											} else if(data.getName().contains("mod")) {
												pointOwner.getScores()[stageNumber].setModifier(Integer.parseInt(tmpPoints[1]));

											}

										}
									}
								}
								line = reader.readLine();
							}
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			}

		}

		for (Team team : teams) {
			System.out.println("Team: " + team.getTeamName());
			System.out.println("Manager: " + team.getManager());
			for (Rider rider : team.getRiders()) {
				System.out.print(rider + "\t");
				for(int i=0; i<rider.getScores().length; i++) {
					System.out.print(rider.getScores()[i] + "\t");
				}
				System.out.println();
			}
		}

		boolean prologue = true;
		//for prolog
		stages++;
		System.out.println("stages: " + stages);

		//generate some rudimentary html
		System.out.println();
		System.out.println("<html>");
		System.out.println("<title>Scores v2</title>");
		System.out.println("<body>");
		System.out.println("<h1>Current Scores</h1>");
		System.out.println("<table border=\"1\">");
		System.out.print("<tr><th>Rider</th>");
		{
			int i=1;
			if (prologue) {
				System.out.println("<td>Prologue</td>");
				i=2;
			}
			for(; i<stages; i++)
				System.out.print("<td>Stage " + (i-1) + "</td>");
		}
		
		System.out.println("<th>Totals:</th>");
		for (Team team : teams) {
			System.out.println("<tr><th>Team: " + team.getTeamName() + "</th></tr>");
			System.out.println("<tr><th>Manager: " + team.getManager() + "</th></tr>");
			int runningTotal = 0;
			for (Rider rider : team.getRiders()) {
				if(rider.isWithdrawn())
					System.out.print("<tr><td><del>"+rider.getName());
				else
					System.out.print("<tr><td>"+rider.getName());
				if(rider.isSubstitute())
					System.out.print(", substituted Stage: "+(rider.getSubstitutedStage()-1));
				if(rider.isWithdrawn())
					System.out.print(",</del> withdrew Stage: "+(rider.getWithdrawnStage()-1));
				System.out.print("</td>");
				int total = 0;
				for (int i=1; i<stages; i++) {

					//					if(rider.isSubstitute())
					//						if(i+1 > rider.getSubstitutedStage())
					//							System.out.print("<td>" + rider.getScores()[i] + "</td>");
					//						else
					//							System.out.print("<td></td>");
					//					if(rider.isWithdrawn())
					//						if(i+1 <= rider.getWithdrawnStage())
					//							System.out.print("<td>" + rider.getScores()[i] + "</td>");
					//						else
					//							System.out.print("<td></td>");

					if((!rider.isSubstitute() && !rider.isWithdrawn())
							|| (rider.isWithdrawn() && (i+1 <= rider.getWithdrawnStage()) && i+1 > rider.getSubstitutedStage())
							|| (rider.isSubstitute() && (i+1 > rider.getSubstitutedStage()) && i+1 <= rider.getWithdrawnStage()))
						System.out.print("<td>" + rider.getScores()[i] + "</td>");
					else
						System.out.print("<td></td>");
					Points temp = rider.getScores()[i];
					total += temp.getPoints();
					total += temp.getClimbing();
					total += (temp.getGC() ? 10 : 0);
					total += (temp.getModifier() > 0 ? temp.getModifier() : 0);
				}
				runningTotal += total;
				System.out.println("<th>"+total+"</th></tr>");
			}
			System.out.print("<th>Team Total:</th>");

			//				System.out.print("<td colspan=\""+stages+"\"></td>");
			for(int i=1; i<stages; i++) {
				int tempscore=0;
				for (Rider rider : team.getRiders()) {
					tempscore += rider.getScores()[i].getClimbing();
					tempscore += rider.getScores()[i].getPoints();
					tempscore += (rider.getScores()[i].getGC()) ? 10 : 0;
					tempscore += (rider.getScores()[i].getModifier() == -1) ? 0 : rider.getScores()[i].getModifier();
				}
				System.out.print("<td>"+tempscore+"</td>");
			}
			System.out.println("<th>"+runningTotal+"</th>");
		}
		System.out.println("</table>");
		System.out.println("</body>");
		System.out.println("</html>");

	}

}
