package sae_201_02;

/**
 * Classe abstraite qui représente une personne avec un identifiant unique.
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 */
public abstract class Person implements Comparable<Person> {
	/** Prénom de la personne */
	private final String FORENAME;
	/** Nom de la personne */
	private final String NAME;
	/** Identifiant unique de la personne */
	private final int INE;
	/** Numéro automatique pour générer les identifiants */
	private static int loginCounter = 0;
	
	/**
	 * Constructeur de la classe personne
	 * @param FORENAME
	 * @param NAME
	 */
	public Person(String FORENAME, String NAME) {
		this.FORENAME = FORENAME;
		this.NAME = NAME;
		this.INE = loginCounter;
		this.loginCounterIncrement();
	}
	
	/**
	 * Méthode qui incrémente le compteur pour générer les logins
	 */
	private void loginCounterIncrement() {
		loginCounter++;
	}
	
	/**
	 * Retourne l'identifiant unique de l'étudiant
	 */
	public int getINE() {
		return INE;
	}
	
	/**
	 * Retourne le login de la personne
	 * @return
	 */
	public String getLogin() {
		return FORENAME + "." + NAME + INE;
	}

	/**
	 * Retourne le prénom de la personne
	 * @return
	 */
	public String getForename() {
		return FORENAME;
	}

	/**
	 * Retourne le nom de la personne
	 * @return
	 */
	public String getName() {
		return NAME;
	}
	
	@Override
	public String toString() {
		return this.INE + ": " + this.FORENAME + " " + this.NAME;
	}

	@Override
	public int compareTo(Person otherPerson) {
		int diff = this.FORENAME.compareTo(otherPerson.getForename());
		if (diff == 0) {
			diff = this.NAME.compareTo(otherPerson.getName());
		}
		return diff;
	}
	
}