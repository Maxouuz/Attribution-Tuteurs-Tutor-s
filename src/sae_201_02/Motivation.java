package sae_201_02;

public enum Motivation {
	NOT_MOTIVATED(-2), NEUTRAL(0), MOTIVATED(2);
	
	private final double bonusPoints;
	
	private Motivation(double bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public double getBonusPoints() {
		return bonusPoints;
	}
}
