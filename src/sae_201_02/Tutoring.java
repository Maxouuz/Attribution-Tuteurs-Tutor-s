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
	/** Map de motivation des élèves */
	private final Map<Student, Motivation> motivations;
	/** Variables pour filtrer les étudiants qui n'ont pas une moyenne suffisante */
	private Double moyenneMaxTutee, moyenneMinTutor;
	/** Variable pour filtrer les étudiants qui ont trop d'absences */
	private Integer nbAbsencesMax;
	/** Map qui donne le tuteur associé au tutoré */
	private final Map<Student, Student> tuteeToTutor;
	/** Map qui donne le/les tutoré(s) associé au tuteur */
	private final Map<Student, Set<Student>> tutorToTutees;
	/** Variable qui donne le poids maximal que peut avoir une arête */
	public final static double POIDS_MAXIMAL;
	/** Variable qui définit combien de tutorés un tuteur peut gérer */
	public final static int MAX_TUTEES_FOR_TUTOR = 2;
	
	static {
		Student st1;
		double poids_maximal;
		try {
			st1 = new Student("Best", "Tutor", 20.0, 3, 0);
			Student st2 = new Student("Best", "Tutee", 20.0, 1, 0);
			poids_maximal = Tutoring.getWidthArete(st1, st2);
		} catch (Exception_Promo e) {
			poids_maximal = Double.MAX_VALUE;
			e.printStackTrace();
		}
		POIDS_MAXIMAL = poids_maximal;
	}

	/**
	 * Constructeur de la classe Tutoring
	 */
	public Tutoring() {
		this.tuteeToTutor = new HashMap<>();
		this.tutorToTutees = new HashMap<>();
		this.motivations = new HashMap<>();
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
	 * Retourne le filtre du nombre d'absences
	 * @return
	 */
	public Integer getNbAbsencesMax() {
		return nbAbsencesMax;
	}

	public void setNbAbsencesMax(Integer nbAbsencesMax) {
		this.nbAbsencesMax = nbAbsencesMax;
	}
	
	public void addStudentMotivation(Student student, Motivation motivation) {
		this.motivations.put(student, motivation);
	}
	
	public double getBonusPoints(Student student) {
		Motivation motivation = this.motivations.get(student);
		double bonusPoints = Motivation.NEUTRAL.getBonusPoints();
		if (motivation != null) {
			bonusPoints = motivation.getBonusPoints();
		}
		return bonusPoints;
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
			res = (moyenneMaxTutee == null || student.getMoyenne() <= moyenneMaxTutee);
		} else {
			res = moyenneMinTutor == null || student.getMoyenne() >= moyenneMinTutor;
		}
		res = res && (nbAbsencesMax == null || student.getNbAbsences() <= nbAbsencesMax);
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
	
	public List<Student> getEligibleTutorsThirdYear() {
		List<Student> eligibleTutors = getEligibleTutors();
		List<Student> eligibleTutors3y = new ArrayList<>();
		
		for (Student tutor : eligibleTutors) {
			if (tutor.getPROMO() == 3) {
				eligibleTutors3y.add(tutor);
			}
		}
		
		return eligibleTutors3y;
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
	public double getWidthArete(Student tutee, Student tutor) {
		return (tutor.getScore() + getBonusPoints(tutor)) * (tutee.getScore() - getBonusPoints(tutee));
	}
	
	/**
	 * Méthode qui résout le problème d'affectation (ne crée que des couples)
	 * @return
	 * @throws Exception_Promo 
	 */
	private GrapheNonOrienteValue<Student> getGrapheTutorTutee(List<Student> tuteesList, List<Student> tutorsList) throws Exception_Promo {
		GrapheNonOrienteValue<Student> graphe = new GrapheNonOrienteValue<>();
		
		// Ajout de tous les sommets
		for (Student student: tuteesList) graphe.ajouterSommet(student);
		for (Student student: tutorsList) graphe.ajouterSommet(student);
		
		// Ajout des arêtes
		for (Student tutee: tuteesList) {
			for (Student tutor: tutorsList) {
				graphe.ajouterArete(tutee, tutor, this.getWidthArete(tutee, tutor));
			}
		}
		
		// Ajoute les tuteurs manquants
		while (tuteesList.size() > tutorsList.size()) {
			Student fakeStudent = new Student("", "", 0, 2, 0);
			tutorsList.add(fakeStudent);
			graphe.ajouterSommet(fakeStudent);
			for (Student tutee: tuteesList) {
				graphe.ajouterArete(tutee, fakeStudent, POIDS_MAXIMAL + 1);
			}
		}
		
		// Ajoute les tutorés manquants
		while (tutorsList.size() > tuteesList.size()) {
			Student fakeStudent = new Student("", "", 0, 1, 0);
			tuteesList.add(fakeStudent);
			graphe.ajouterSommet(fakeStudent);
			for (Student tutor: tutorsList) {
				graphe.ajouterArete(fakeStudent, tutor, POIDS_MAXIMAL + 1);
			}
		}
		
		return graphe;
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
	 * @throws Exception_Promo 
	 */
	public void createAssignments() throws Exception_Promo {
		List<Student> eligibleTutees = getEligibleTutees();
		List<Student> eligibleTutors = getEligibleTutors();
		int nbRepetitions = 0;
		while (eligibleTutees.size() != 0 && nbRepetitions < MAX_TUTEES_FOR_TUTOR) {
			boolean fakeStudentsAreTutees = false;
			if (eligibleTutors.size() > eligibleTutees.size()) fakeStudentsAreTutees = true;
			
			GrapheNonOrienteValue<Student> graphe = getGrapheTutorTutee(eligibleTutees, eligibleTutors);
			CalculAffectation<Student> calcul = new CalculAffectation<>(graphe, eligibleTutees, eligibleTutors);
			for (Arete<Student> arete : calcul.getAffectation()) {
				Student tutee = arete.getExtremite1();
				Student tutor = arete.getExtremite2();
				
				if (graphe.getPoids(tutee, tutor) <= POIDS_MAXIMAL) {
					addAssignment(tutee, tutor);
					eligibleTutees.remove(tutee);
				} else {
					if (fakeStudentsAreTutees) eligibleTutees.remove(tutee);
					else eligibleTutors.remove(tutor);
				}
				eligibleTutors = getEligibleTutorsThirdYear();
			}
			nbRepetitions++;
		}
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
