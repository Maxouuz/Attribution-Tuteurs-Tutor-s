package sae_201_02;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Classe qui représente un professeur
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 *
 */
public class Teacher extends Person {
	/** Liste des matières que le professeur enseigne */
	private final Set<Subject> subjectsTeached;
	/** Hash du mot de passe du professeur pour se connecter au tutorat */
	private final int PASSWORD;
	
	/**
	 * Constructeur de la classe Teacher
	 * @param FORENAME
	 * @param NAME
	 * @param subjectsTeached
	 */
	public Teacher(String FORENAME, String NAME, Collection<? extends Subject> subjectsTeached, String password) {
		super(Person.getNonUsedINE(), FORENAME, NAME);
		this.subjectsTeached = new LinkedHashSet<>(subjectsTeached);
		this.PASSWORD = Objects.hash(password);
	}
	
	/**
	 * Constructeur surchargé de la classe Teacher
	 */
	public Teacher(String FORENAME, String NAME, String password) {
		this(FORENAME, NAME, new LinkedHashSet<>(), password);
	}
	
	/**
	 * Ajoute une matière enseigné par le professeur
	 * @param subject
	 */
	public void addSubjectTeached(Subject subject) {
		this.subjectsTeached.add(subject);
	}
	
	/**
	 * Retire une matière enseigné par le professeur
	 * @param subject
	 */
	public boolean removeSubjectTeached(Subject subject) {
		return this.subjectsTeached.remove(subject);
	}

	/**
	 * Retourne une copie des matières enseignées par le professeur
	 * @return
	 */
	public Set<Subject> getSubjectsTeached() {
		return new LinkedHashSet<Subject>(subjectsTeached);
	}

	/**
	 * Renvoie true si la chaîne donné en paramètre est égal au mot de passe du professeur
	 * @param entry
	 * @return
	 */
	public boolean checkPassword(String entry) {
		return PASSWORD == Objects.hash(entry);
	}
}

