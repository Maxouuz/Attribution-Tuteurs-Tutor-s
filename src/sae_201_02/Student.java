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
	 * Variable qui représente la limite du nombre d'absences
	 * pris en compte dans le calcul du score
	 */
	private final static double SCORE_MAX_ABSENCES = 365;
	
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
	 * Méthode qui calcule un score pour l'étudiant pour un tutorat
	 * Plus le score est grand, plus l'étudiant est considéré comme meilleur
	 * Les critères pris en compte sont:
	 * - La promo
	 * - La moyenne
	 * - Le nombre d'absences
	 * @return
	 */
	public double getScore(Tutoring tutoring) {
		/** Variable qui représente l'écart des scores entre les différentes promos */
		double promoScoreGap = (SCORE_MAX_ABSENCES / tutoring.getAbsenceWidth()) + 21;
		
		double nbAbsencesCopy = nbAbsences;
		
		if (nbAbsencesCopy > SCORE_MAX_ABSENCES) {
			nbAbsencesCopy = SCORE_MAX_ABSENCES;
		}
		return PROMO * promoScoreGap + moyenne * tutoring.getMoyenneWidth()- (nbAbsencesCopy / tutoring.getAbsenceWidth());
	}
	
	/**
	 * Méthode qui calcule un score pour l'étudiant avec les poids par défaut
	 * @return
	 */
	public double getScore() {
		return getScore(new Tutoring());
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + PROMO;
		long temp;
		temp = Double.doubleToLongBits(moyenne);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + nbAbsences;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		boolean res = true;
		if (this == obj)
			res = true;
		else if (!super.equals(obj))
			res = false;
		else if (getClass() != obj.getClass())
			res = false;
		else {
			Student other = (Student) obj;
			if (PROMO != other.PROMO)
				res = false;
			if (Double.doubleToLongBits(moyenne) != Double.doubleToLongBits(other.moyenne))
				res = false;
			if (nbAbsences != other.nbAbsences)
				res = false;
		}
		return res;
	}
}
