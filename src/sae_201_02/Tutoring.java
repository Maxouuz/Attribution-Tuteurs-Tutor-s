package sae_201_02;

import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae2_02.graphes.CalculAffectation;
import fr.ulille.but.sae2_02.graphes.GrapheNonOrienteValue;

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
	
	/**
	 * Méthode qui résout le problème d'affectation
	 * @return
	 */
	public CalculAffectation<Student> computeAssignment() {
		GrapheNonOrienteValue<Student> graphe = new GrapheNonOrienteValue<>();
		
		// Ajout de tous les sommets
		for (Student student: tutees) graphe.ajouterSommet(student);
		for (Student student: tutors) graphe.ajouterSommet(student);
		
		// Ajout des arêtes
		for (Student tutee: tutees) {
			for (Student tutor: tutors) {
				graphe.ajouterArete(tutor, tutee, (tutor.getPROMO() * 2 + tutor.getMoyenne()) * tutee.getMoyenne());
			}
		}
		
		// Ajoute les tuteurs manquants
		int tutorsMissing = tutees.size() - tutors.size();
		for (int i = 0; i < tutorsMissing; i++) {
			Student fakeStudent = new Student(null, null, 0, 2);
			this.addStudent(fakeStudent);
			graphe.ajouterSommet(fakeStudent);
			for (Student tutee: tutees) {
				graphe.ajouterArete(fakeStudent, tutee, 10000);
			}
		}
		
		return new CalculAffectation<>(graphe, tutees, tutors);
	}
}
