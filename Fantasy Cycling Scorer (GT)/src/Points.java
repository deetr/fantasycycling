
public class Points {
	private boolean gc;
	private int points;
	private int climbing;
	private int modifier;
	
	public Points(int points, int climbing, boolean gc) {
		this.gc = gc;
		this.points = points;
		this.climbing = climbing;
		modifier = -1;
	}
	
	public boolean getGC() {
		return gc;
	}
	
	public int getPoints() {
		return points;
	}
	
	public int getClimbing() {
		return climbing;
	}
	
	public int getModifier() {
		return modifier;
	}
	
	public void setModifier(int mod ) {
		modifier = mod;
	}
	
	@Override
	public String toString() {
		return points + "+" + climbing + "+" + (gc ? 10 : 0) + (modifier == -1 ? "" : "+" + modifier);
	}
	
}
