
public class Rider {

	private String name;
	private int number;
	private int withdrawnStage;
	private int substitutedStage;
	private Points[] points;
	private boolean isWithdrawn;
	
	public Rider(int number, String name) {
		this.number = number;
		this.name = name;
		withdrawnStage = Integer.MAX_VALUE;
		substitutedStage = -1;
		points = new Points[22];
		isWithdrawn = false;
	}
	
	public void init() {
		for(int i=0; i<points.length; i++) {
			points[i] = new Points(0, 0, false);
		}
	}

	public String getName() {
		return name;
	}

	public int getNumber() {
		return number;
	}

	public int getWithdrawnStage() {
		return withdrawnStage;
	}

	public void setWithdrawnStage(int withdrawnStage) {
		isWithdrawn = true;
		this.withdrawnStage = withdrawnStage;
	}

	public int getSubstitutedStage() {
		return substitutedStage;
	}

	public void setSubstitutedStage(int substitutedStage) {
		this.substitutedStage = substitutedStage;
	}
	
	public boolean isSubstitute() {
		return substitutedStage != -1;
	}
	
	public boolean isWithdrawn() {
		return isWithdrawn;
	}
	
	@Override
	public String toString() {
		if(isSubstitute() && isWithdrawn())
			return number + " " + name + ": was substituted stage " + substitutedStage + ", withdrew stage " + withdrawnStage;
		if(isSubstitute())
			return number + " " + name + ": was substituted stage " + substitutedStage;
		if(isWithdrawn())
			return number + " " + name + ": withdrew stage " + withdrawnStage;
		return number + " " + name;
	}
	
	public void setScore(Points newPoints, int stage) {
		points[stage] = newPoints;
	}
	
	public Points[] getScores() {
		return points;
	}
	
}
