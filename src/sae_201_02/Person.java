package sae_201_02;

import java.util.Set;
import java.util.TreeSet;

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
	/** Liste qui contient tous les INE des personnes afin d'éviter les doublons */
	private final static Set<Integer> ineGenerated = new TreeSet<>();
	
	/**
	 * Constructeur de la classe personne
	 * @param FORENAME
	 * @param NAME
	 */
	public Person(int INE, String FORENAME, String NAME) {
		// Aucun doublon d'INE ne doit être crée
		if (ineGenerated.contains(INE)) {
			throw new IllegalArgumentException("L'INE existe déjà pour un autre étudiant");
		}
		this.FORENAME = FORENAME;
		this.NAME = NAME;		
		this.INE = INE;
		ineGenerated.add(INE);
		// On fait en sorte que le numéro automatique soit toujours supérieur à tous les autres INE
		if (INE >= loginCounter) {
			loginCounter = INE + 1;
		}
	}
	
	/**
	 * Retourne l'identifiant unique de l'étudiant
	 */
	public int getINE() {
		return INE;
	}
	
	/**
	 * Retourne le prochain INE qui sera généré automatiquement
	 * @return
	 */
	public static int getNonUsedINE() {
		return loginCounter;
	}
	
	/**
	 * Réinitialise la liste contenant tous les INE crées
	 * @return
	 */
	public static void resetUsedINE() {
		ineGenerated.clear();
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