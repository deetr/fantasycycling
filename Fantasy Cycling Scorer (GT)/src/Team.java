import java.util.ArrayList;
import java.util.Collection;


public class Team {

	private String misterManager;
	private String teamName;
	private ArrayList<Rider> riders;
	
	public Team(String manager, String teamName) {
		riders = new ArrayList<Rider>();
		misterManager = manager;
		this.teamName = teamName;
	}
	
	public void addRider(Rider toAdd) {
		riders.add(toAdd);
	}
	
	public Collection<Rider> getRiders() {
		return riders;
	}
	
	public String getManager() {
		return misterManager;
	}
	
	public String getTeamName() {
		return teamName;
	}
	
}
