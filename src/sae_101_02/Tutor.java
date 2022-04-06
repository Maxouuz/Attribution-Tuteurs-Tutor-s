package sae_101_02;

/**
 * Classe représentant un élève de 2ème ou 3ème année souhaitant être tuteur
 * @author nathan.hallez.etu
 *
 */
public class Tutor extends Student {
	/**
	 * Sujet que l'élève veut tutorer
	 */
	private Subject subjectTeached;
	/**
	 * Année de promo de l'étudiant (2e ou 3e année)
	 */
	private final int PROMO;
	
	/**
	 * Constructeur de la classe Tutor
	 * @param FORENAME
	 * @param NAME
	 * @param moyenne
	 * @param PROMO
	 * @param subjectTeached
	 */
	public Tutor(String FORENAME, String NAME, double moyenne, int PROMO, Subject subjectTeached) {
		// TODO: Prévoir le fait que la variable promo soit inférieur à 2 ou supérieur à 3
		super(FORENAME, NAME, moyenne);
		if (PROMO == 2 || PROMO == 3) {
			this.PROMO = PROMO;
		} else {
			this.PROMO = 2;
		}
		this.subjectTeached = subjectTeached;
	}
	
	/**
	 * Retourne la ressource que l'élève veut tutorer
	 * @return
	 */
	public Subject getSubjectTeached() {
		return subjectTeached;
	}
	
	/**
	 * Changer le sujet que l'élève veut tutorer
	 * @param subjectTeached
	 */
	public void setSubjectTeached(Subject subjectTeached) {
		this.subjectTeached = subjectTeached;
	}
	
	/**
	 * Retourne l'année de la promo de l'étudiant
	 */
	public int getPromo() {
		return this.PROMO;
	}
}
