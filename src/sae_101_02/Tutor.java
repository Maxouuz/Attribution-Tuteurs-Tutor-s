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
<<<<<<< HEAD
	/**
	 * Année de promo de l'étudiant (2e ou 3e année)
	 */
	private final int PROMO;
=======
	protected int promo;
>>>>>>> e73f8fc6a9c6253a39571e6c969b9f67d9740a32
	
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
<<<<<<< HEAD
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
=======
		
		super(FORENAME, NAME, moyenne);
		this.subjectTeached = subjectTeached; 
		if(this.promo < 2 || this.promo > 3) {
			this.promo = 2;
		}
	}
	
>>>>>>> e73f8fc6a9c6253a39571e6c969b9f67d9740a32
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
<<<<<<< HEAD
	
	/**
	 * Retourne l'année de promo de l'étudiant
	 * @return
	 */
	public int getPromo() {
		return PROMO;
	}
=======

	public int getPromo() {
		return promo;
	}	
	
	
>>>>>>> e73f8fc6a9c6253a39571e6c969b9f67d9740a32
}
