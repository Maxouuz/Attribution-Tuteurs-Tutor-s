package sae_201_02;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un tutorat pour une matière.
 * Contient tous les candidats pour les tuteurs et tutorés.
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 */
public class Tutoring {
	/** Liste des tutorés */
	private final List<Student> tutees;
	/** Liste des tuteurs */
	private final List<Student> tutors;

	/**
	 * Constructeur de la classe Tutoring
	 */
	public Tutoring() {
		this.tutees = new ArrayList<>();
		this.tutors = new ArrayList<>();
	}
	
	/**
	 * Retourne une copie de la liste des tutorés
	 * @return
	 */
	public List<Student> getTutees() {
		List<Student> copy = new ArrayList<>();
		copy.addAll(tutees);
		return copy;
	}
	
	/**
	 * Retourne une copie de la liste des tuteurs
	 * @return
	 */
	public List<Student> getTutors() {
		List<Student> copy = new ArrayList<>();
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
