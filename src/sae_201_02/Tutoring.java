package sae_201_02;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Classe représentant un tutorat pour une matière.
 * Contient tous les candidats pour les tuteurs et tutorés.
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 */
public class Tutoring {
	/** Liste des tutorés */
	private final Set<Student> tutees;
	/** Liste des tuteurs */
	private final Set<Student> tutors;

	/**
	 * Constructeur de la classe Tutoring
	 */
	public Tutoring() {
		this.tutees = new LinkedHashSet<>();
		this.tutors = new LinkedHashSet<>();
	}
	
	/**
	 * Retourne une copie de la liste des tutorés
	 * @return
	 */
	public Set<Student> getTutees() {
		Set<Student> copy = new LinkedHashSet<>();
		copy.addAll(tutees);
		return copy;
	}
	
	/**
	 * Retourne une copie de la liste des tuteurs
	 * @return
	 */
	public Set<Student> getTutors() {
		Set<Student> copy = new LinkedHashSet<>();
		copy.addAll(tutors);
		return copy;
	}
	
	/**
	 * Ajoute un étudiant dans la liste des tutorées ou des tuteurs
	 * en fonction de son année de promo.
	 * @param student
	 * @return
	 */
	public boolean addStudent(Student student) {
		boolean res;
		if (student.canBeTutee()) {
			res =  tutees.add(student);
		} else {
			res = tutors.add(student);
		}
		return res;
	}
}
