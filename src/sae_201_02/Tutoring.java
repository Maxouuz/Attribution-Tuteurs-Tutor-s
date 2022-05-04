package sae_201_02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.ulille.but.sae2_02.graphes.Arete;
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
	/** Variables pour filtrer les étudiants qui n'ont pas une moyenne suffisante */
	private Double moyenneMaxTutee, moyenneMinTutor;
	/** Map qui donne le tuteur associé au tutoré */
	private final Map<Student, Student> tuteeToTutor;
	/** Map qui donne le/les tutoré(s) associé au tuteur */
	private final Map<Student, Set<Student>> tutorToTutees;
	/** Variable qui donne le poids maximal que peut avoir une arête */
	public final static double POIDS_MAXIMAL;
	/** Variable qui définit combien de tutorés un tuteur peut gérer */
	public final static int MAX_TUTEES_FOR_TUTOR = 2;
	
	static {
		Student st1 = new Student("Best", "Tutor", 20.0, 3, 0);
		Student st2 = new Student("Best", "Tutee", 20.0, 1, 0);
		POIDS_MAXIMAL = Tutoring.getWidthArete(st1, st2);
	}

	/**
	 * Constructeur de la classe Tutoring
	 */
	public Tutoring() {
		this.tuteeToTutor = new HashMap<>();
		this.tutorToTutees = new HashMap<>();
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
	 * Retourne une copie de la liaison des tuteurs/tutorés
	 * @return
	 */
	public Map<Student, Student> getAssignment() {
		Map<Student, Student> copy = new HashMap<>();
		copy.putAll(tuteeToTutor);
		return copy;
	}

	/**
	 * Retourne le filtre de la moyenne minimum pour les tutorés
	 * @return
	 */
	public Double getMoyenneMaxTutee() {
		return moyenneMaxTutee;
	}
	
	/**
	 * Méthode pour changer la moyenne minimum pour les tutorés
	 * @return
	 */
	public void setMoyenneMaxTutee(Double moyenneMinTutee) {
		this.moyenneMaxTutee = moyenneMinTutee;
	}
	
	/**
	 * Retourne le filtre de la moyenne minimum pour les tuteurs
	 * @return
	 */
	public Double getMoyenneMinTutor() {
		return moyenneMinTutor;
	}
	
	/**
	 * Méthode pour changer la moyenne minimum pour les tutorés
	 * @return
	 */
	public void setMoyenneMinTutor(Double moyenneMinTutor) {
		this.moyenneMinTutor = moyenneMinTutor;
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
	 * Retourne true si l'étudiant a une moyenne suffisante pour être tuteur/tutoré
	 * @param student
	 * @return
	 */
	public boolean canParticipate(Student student) {
		boolean res;
		// Retourne true si il n'y a pas de filtre ou si l'étudiant a une bonne moyenne
		if (student.canBeTutee()) {
			res = moyenneMaxTutee == null || student.getMoyenne() <= moyenneMaxTutee;
		} else {
			res = moyenneMinTutor == null || student.getMoyenne() >= moyenneMinTutor;
		}
		return res;
	}
	
	
	/**
	 * Retourne la liste de tous les tuteurs qui ont une moyenne suffisante
	 * @return
	 */
	public List<Student> getEligibleTutors() {
		List<Student> eligibleTutors = new ArrayList<>();
		for (Student tutor: tutors) {
			if (this.canParticipate(tutor)) eligibleTutors.add(tutor);
		}
		return eligibleTutors;
	}
	
	/**
	 * Retourne la liste de tous les tutorés qui ont une moyenne suffisante
	 * @return
	 */
	public List<Student> getEligibleTutees() {
		List<Student> eligibleTutees = new ArrayList<>();
		for (Student tutee: tutees) {
			if (this.canParticipate(tutee)) eligibleTutees.add(tutee);
		}
		return eligibleTutees;
	}
	
	/**
	 * Retourne le poids d'une arête pour un tuteur et un tutoré
	 */
	public static double getWidthArete(Student tutor, Student tutee) {
		return tutor.getScore() * tutee.getScore();
	}
	
	/**
	 * Méthode qui résout le problème d'affectation (ne crée que des couples)
	 * @return
	 */
	private CalculAffectation<Student> computeCoupleTutorTutee() {
		GrapheNonOrienteValue<Student> graphe = new GrapheNonOrienteValue<>();
		
		List<Student> eligibleTutors = getEligibleTutors();
		Collections.sort(eligibleTutors);
		List<Student> eligibleTutees = getEligibleTutees();
		
		// Ajout de tous les sommets
		for (Student student: eligibleTutees) graphe.ajouterSommet(student);
		for (Student student: eligibleTutors) graphe.ajouterSommet(student);
		
		// Ajout des arêtes
		for (Student tutee: eligibleTutors) {
			for (Student tutor: eligibleTutees) {
				graphe.ajouterArete(tutee, tutor, Tutoring.getWidthArete(tutor, tutee));
			}
		}
		
		// Ajoute les tuteurs manquants
		int tutorsMissing = tutees.size() - tutors.size();
		for (int i = 0; i < tutorsMissing; i++) {
			Student fakeStudent = new Student("", "", 0, 2, 0);
			this.addStudent(fakeStudent);
			graphe.ajouterSommet(fakeStudent);
			for (Student tutee: tutees) {
				graphe.ajouterArete(tutee, fakeStudent, POIDS_MAXIMAL + 1);
			}
		}
		
		return new CalculAffectation<>(graphe, tutees, tutors);
	}
	
	/**
	 * Méthode qui associe plusieurs tutorés aux tuteurs de troisième année
	 */
	private void addMultipleTuteeToTutors() {
		int nbRepetitions = 0;
		int nbAssociations = 0;
		List<Student> eligibleTutors = getEligibleTutors();
		List<Student> eligibleTutees = getEligibleTuteesWithNoTutors();
		/* Répète tant que tous les tutorés n'ont pas été associé
		   ou que le nombre maximal d'associations a été atteint */
		while (nbAssociations < eligibleTutees.size() && nbRepetitions < 2) {
			int i = 0;
			while (nbAssociations < eligibleTutees.size() && i < eligibleTutors.size()) {
				addAssignment(eligibleTutees.get(i), eligibleTutors.get(i));
				i++;
				nbAssociations++;
			}
			nbRepetitions++;
		}
	}
	
	/**
	 * Retourne tous les tutorés qui ne sont pas associé à un tuteur
	 * @return
	 */
	private List<Student> getEligibleTuteesWithNoTutors() {
		List<Student> res = new ArrayList<>();
		for (Student tutee: getEligibleTutees()) {
			if (!tutors.contains(tuteeToTutor.get(tutee))) {
				res.add(tutee);
			}
		}
		return res;
	}
	
	/**
	 * Méthode qui ajoute une association dans les deux maps
	 */
	public void addAssignment(Student tutee, Student tutor) {
		tuteeToTutor.put(tutee, tutor);
		if (!tutorToTutees.containsKey(tutor)) {
			tutorToTutees.put(tutor, new LinkedHashSet<>());
		}
		tutorToTutees.get(tutor).add(tutee);
	}
	
	/**
	 * Méthode qui actualise les deux maps d'affectations
	 */
	public void createAssignments() {
		for (Arete<Student> arete : computeCoupleTutorTutee().getAffectation()) {
			Student tutor = arete.getExtremite1();
			Student tutee = arete.getExtremite2();
			addAssignment(tutee, tutor);
		}
		addMultipleTuteeToTutors();
	}
	
	/**
	 * Chaîne de caractère donnant pour chaque tutoré le tuteur associé
	 * @return
	 */
	public String toStringTutees() {
		StringBuilder res = new StringBuilder();
		List<Student> sortedTutees = tutees;
		Collections.sort(sortedTutees, new ScoreComparator());
		
		for (Student tutee: sortedTutees) {
			if (tuteeToTutor.containsKey(tutee)) {
				res.append(tutee + " --> " + tuteeToTutor.get(tutee) + "\n");
			}
		}
		return res.toString();
	}
	
	/**
	 * Chaîne de caractère donnant pour chaque tuteur le/les tutoré(s) associé(s)
	 * @return
	 */
	public String toStringTutors() {
		StringBuilder res = new StringBuilder();
		List<Student> sortedTutors = tutors;
		Collections.sort(sortedTutors, new ScoreComparator());
		
		for (Student tutor: sortedTutors) {
			if (tutorToTutees.containsKey(tutor)) {
				res.append(tutor + " --> " + tutorToTutees.get(tutor) + "\n");
			}
		}
		return res.toString();
	}
}
