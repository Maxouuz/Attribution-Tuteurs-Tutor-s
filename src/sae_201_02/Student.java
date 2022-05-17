package sae_201_02;

/**
 * Classe abstraite qui représente un étudiant avec une moyenne et une promo
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 *
 */
public class Student extends Person {
	/** Représente la moyenne de l'étudiant */
	private double moyenne;
	/** Année de promo de l'étudiant */
	private final int PROMO;
	/**Nombre abscences de l'etudiant */
	private int nbAbsences;
	/** 
	 * Variable qui représente le nombre d'absences
	 * qu'il faut pour perdre un point de score
	 */
	private final static double ABSENCE_SCORE_DIVISOR = 3;
	/** Variable qui représente la limite du nombre d'absences
	 * pris en compte dans le calcul du score
	 */
	private final static double SCORE_MAX_ABSENCES = 365;
	/** Variable qui représente l'écart des scores entre les différentes promos */
	private final static double PROMO_SCORE_GAP = (SCORE_MAX_ABSENCES / ABSENCE_SCORE_DIVISOR) + 21;
	
	/**
	 * Constructeur de la classe Student
	 * @param FORENAME
	 * @param NAME
	 * @param moyenne
	 * @param PROMO
	 * @param absence
	 * @throws ExceptionPromo 
	 */
	public Student(String FORENAME, String NAME, double moyenne, int PROMO, int nbAbsences) throws ExceptionPromo {
		super(FORENAME, NAME);
		this.moyenne = moyenne;
		this.nbAbsences = nbAbsences;
		if (PROMO < 1 || PROMO > 3) {
			throw new ExceptionPromo("La promo de l'étudiant doit être compris entre 1 et 3!");
		}
		this.PROMO = PROMO;
	}
	
	/**
	 * Retourne le login de la personne avec un .etu à la fin du login
	 * @return
	 */
	public String getLogin() {
		return super.getLogin() + ".etu";
	}

	/**
	 * Retourne la moyenne de l'étudiant
	 * @return
	 */
	public double getMoyenne() {
		return moyenne;
	}
	
	/**
	 * Changer la moyenne d'un étudiant
	 * @param moyenne
	 */
	public void setMoyenne(double moyenne) {
		this.moyenne = moyenne;
	}

	/**
	 * Retourne l'année de la promo d'un étudiant
	 * @return
	 */
	public int getPROMO() {
		return PROMO;
	}
	
	/**
	 * Retourne true si l'étudiant peut dispenser le tutorat sur une ressource
	 * @return
	 */
	public boolean canBeTutor() {
		return PROMO >= 2;
	}
	
	/**
	 * Retourne true si l'étudiant peut bénéficier du tutorat pour une ressource
	 * @return
	 */
	public boolean canBeTutee() {
		return ! this.canBeTutor();
	}
	
	@Override
	public String toString() {
		return super.toString() + " (moyenne: " + this.moyenne + ", promo: " + this.PROMO + ", absences: " + this.nbAbsences + ")";
	}
	
	/**
	 * Méthode qui calcule un score pour l'étudiant
	 * Plus le score est grand, plus l'étudiant est considéré comme meilleur
	 * Les critères pris en compte sont:
	 * - La promo
	 * - La moyenne
	 * - Le nombre d'absences
	 * @return
	 */
	public double getScore() {
		double nbAbsencesCopy = nbAbsences;
		if (nbAbsencesCopy > SCORE_MAX_ABSENCES) {
			nbAbsencesCopy = SCORE_MAX_ABSENCES;
		}
		return PROMO * PROMO_SCORE_GAP + moyenne - (nbAbsencesCopy / ABSENCE_SCORE_DIVISOR);
	}

	/**
	 * Retourne le nombre d'absence d'un etudiant
	 * @return
	 */
	public int getNbAbsences() {
		return nbAbsences;
	}

	/**
	 * Change le nombre d'absence d'un étudiant
	 * @param absence
	 */
	public void setNbAbsences(int nbAbsences) {
		if (nbAbsences <= 0)
			throw new IllegalArgumentException("Le nombre d'absences doit être un nombre positif!");
		this.nbAbsences = nbAbsences;
	}
}
