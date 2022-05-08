package sae_201_02;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Classe qui représente un professeur
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 *
 */
public class Teacher extends Person {
	/** Liste des matières que le professeur enseigne */
	private final Set<Subject> subjectsTeached;
	
	/**
	 * Constructeur de la classe Teacher
	 * @param FORENAME
	 * @param NAME
	 * @param subjectsTeached
	 */
	public Teacher(String FORENAME, String NAME, Collection<? extends Subject> subjectsTeached) {
		super(FORENAME, NAME);
		this.subjectsTeached = new LinkedHashSet<>(subjectsTeached);
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
	public void removeSubjectTeached(Subject subject) {
		this.subjectsTeached.remove(subject);
	}

	/**
	 * Retourne une copie des matières enseignées par le professeur
	 * @return
	 */
	public Set<Subject> getSubjectsTeached() {
		return new LinkedHashSet<Subject>(subjectsTeached);
	}
}

