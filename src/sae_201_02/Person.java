package sae_201_02;

import java.util.Objects;

/**
 * Classe abstraite qui représente une personne avec un identifiant unique.
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 */
public abstract class Person {
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
	public int hashCode() {
		return Objects.hash(INE);
	}

	@Override
	public boolean equals(Object obj) {
		boolean res;
		if (this == obj)
			res = true;
		else if (obj == null)
			res = false;
		else if (getClass() != obj.getClass())
			res =  false;
		else {
			Person other = (Person) obj;
			res = INE == other.INE;
		}
		return res;
	}
	
	@Override
	public String toString() {
		return this.INE + ": " + this.FORENAME + " " + this.NAME;
	}
}