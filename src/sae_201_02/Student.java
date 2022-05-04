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
	private int absence;
	
	/**
	 * Constructeur de la classe Student
	 * @param FORENAME
	 * @param NAME
	 * @param moyenne
	 * @param PROMO
	 * @param absence
	 */
	public Student(String FORENAME, String NAME, double moyenne, int PROMO, int absence) {
		super(FORENAME, NAME);
		this.moyenne = moyenne;
		this.absence = absence;
		if (PROMO < 1 || PROMO > 3) {
			throw new ArithmeticException("La promo de l'étudiant doit être compris entre 1 et 3!");
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
		return super.toString() + " (moyenne: " + this.moyenne + ", promo: " + this.PROMO + ")";
	}
	
	/**
	 * Retourne le nombre d'absence d'un etudiant
	 * @return
	 */
	public int getAbsence() {
		return absence;
	}

	/**
	 * Change le nombre d'absence d'un étudiant
	 * @param absence
	 */
	public void setAbsence(int absence) {
		this.absence = absence;
	}
	
	/**
	 * Compte le nombre d'absence d'un étudiant
	 * @param student
	 * @return
	 */
	public int numberAbsences(Student student) {
		return student.getAbsence();
	}

	/**
	 * Retourne vrai si le tutoré/tuteur a au moins une absence 
	 * @param student
	 * @return
	 */
	public boolean haveAbsences(Student student) {
		if(student.numberAbsences(student) > 0) {
			return true;
		}else {
			return false;
		}
	}
	
}
