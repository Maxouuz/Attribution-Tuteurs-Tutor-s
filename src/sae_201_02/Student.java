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
	 * Constructeur de la classe Student
	 * @param FORENAME
	 * @param NAME
	 * @param moyenne
	 * @param PROMO
	 * @param absence
	 * @throws Exception_Promo 
	 */
	public Student(String FORENAME, String NAME, double moyenne, int PROMO, int nbAbsences) throws Exception_Promo {
		super(FORENAME, NAME);
		this.moyenne = moyenne;
		this.nbAbsences = nbAbsences;
		if (PROMO < 1 || PROMO > 3) {
			throw new Exception_Promo("La promo de l'étudiant doit être compris entre 1 et 3!");
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
		return PROMO * 2 + moyenne - (nbAbsences / 5.0);
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
	public boolean setNbAbsences(int nbAbsences) {
		boolean res = false;
		if (nbAbsences >= 0) {
			this.nbAbsences = nbAbsences;
			res = true;
		}
		return res;
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
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (PROMO != other.PROMO)
			return false;
		if (Double.doubleToLongBits(moyenne) != Double.doubleToLongBits(other.moyenne))
			return false;
		if (nbAbsences != other.nbAbsences)
			return false;
		return true;
	}
}
