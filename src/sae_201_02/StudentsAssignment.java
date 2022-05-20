package sae_201_02;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Méthode permettant d'associer les tutorés aux tuteurs.
 * On peut accéder aux affectations à partir du tuteur ou du tutoré
 * @author nathan.hallez.etu
 *
 */
public class StudentsAssignment {
	/** Map qui associe les tuteurs et les tutorés dans les deux sens */
	private final Map<Student, Set<Student>> assignments;
	
	/**
	 * Constructeur de la classe qui initialise la map
	 */
	public StudentsAssignment() {
		this.assignments = new HashMap<>();
	}
	
	/**
	 * Méthode qui ajoute une association dans les deux maps
	 * @throws ExceptionPromo 
	 */
	void add(Student tutee, Student tutor) {
		assignments.put(tutee, new LinkedHashSet<>());
		if (!assignments.containsKey(tutor)) {
			assignments.put(tutor, new LinkedHashSet<>());
		}
		assignments.get(tutee).add(tutor);
		assignments.get(tutor).add(tutee);
	}
	
	/**
	 * Méthode pour enlever une affectation entre un tuteur et un tutoré
	 * @param tutee
	 * @param tutor
	 */
	public void removeAssignment(Student tutee, Student tutor) {
		if (assignments.containsKey(tutee)) {
			assignments.remove(tutee);
			assignments.get(tutor).remove(tutee);
		}
	}
	
	/**
	 * Méthode pour enlever toutes les affectations d'un étudiant
	 * @param student
	 */
	public void removeAssignments(Student student) {
		for (Student associatedStudent: get(student))
			removeAssignment(student, associatedStudent);
	}
	
	/**
	 * Retourne la liste des affectations pour un étudiant
	 * Renvoie une liste vide si il y a pas de tutorés associés
	 * @param tutor
	 * @return
	 */
	public Set<Student> get(Student student) {
		return assignments.containsKey(student) ? assignments.get(student) : new LinkedHashSet<>();
	}
	
	/**
	 * Méthode déléguée de contains
	 * @param student
	 * @return
	 */
	public boolean contains(Student student) {
		return assignments.containsKey(student);
	}
	
	/**
	 * Méthode déléguée de clear
	 */
	public void clear() {
		assignments.clear();
	}
	
	/**
	 * Retourne la liste des tutorés affectés
	 * @return
	 */
	public Set<Student> getTutees() {
		Set<Student> res = new LinkedHashSet<>();
		for (Student student: assignments.keySet()) {
			if (student.canBeTutee()) {
				res.add(student);
			}
		}
		return res;
	}
	
	/**
	 * Retourne la liste des tuteurs affectés
	 * @return
	 */
	public Set<Student> getTutors() {
		Set<Student> res = new LinkedHashSet<>();
		for (Student student: assignments.keySet()) {
			if (student.canBeTutor()) {
				res.add(student);
			}
		}
		return res;
	}
	
	/**
	 * Retourne true si le couple tuteur-tutoré existe dans la map
	 * @param tutee
	 * @param tutor
	 * @return
	 */
	public boolean coupleExists(Student tutee, Student tutor) {
		boolean res = false;
		if (assignments.containsKey(tutee)) {
			res = assignments.get(tutee).contains(tutor);
		}
		return res;
	}
}
