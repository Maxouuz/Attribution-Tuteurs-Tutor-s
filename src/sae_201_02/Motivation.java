package sae_201_02;

/**
 * Énumération du niveau de motivation d'un étudiant
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 */
public enum Motivation {
	NOT_MOTIVATED(-2), NEUTRAL(0), MOTIVATED(2);
	
	/** 
	 * Entier qui donne le nombre de points qui seront ajoutés dans le score
	 * de l'étudiant
	 */
	private final double bonusPoints;
	
	private Motivation(double bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public double getBonusPoints() {
		return bonusPoints;
	}
}
