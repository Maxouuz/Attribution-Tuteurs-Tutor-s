package sae_101_02;

/**
 * Classe abstraite qui représente un étudiant avec une moyenne et une promo
 * @author nathan.hallez.etu
 *
 */
public abstract class Student extends Person {
	/**
	 * Représente la moyenne de l'étudiant
	 */
	private double moyenne;
	
	/**
	 * Constructeur de la classe Student
	 * @param FORENAME
	 * @param NAME
	 * @param moyenne
	 * @param PROMO
	 */
	public Student(String FORENAME, String NAME, double moyenne) {
		super(FORENAME, NAME, "etu");
		this.moyenne = moyenne;
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
}
