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
	/** Variables pour filtrer les tutorés qui ont une moyenne trop élevée */
	private Double moyenneMaxTutee;
	/** Variable pour filtrer les tuteurs qui ont une moyenne trop faible */
	private Double moyenneMinTutor;
	/** Variable pour filtrer les étudiants qui ont trop d'absences */
	private Integer nbAbsencesMax;
	/** Map qui donne le tuteur associé au tutoré */
	private final Map<Student, Student> tuteeToTutor;
	/** Map qui donne le/les tutoré(s) associé au tuteur */
	private final Map<Student, Set<Student>> tutorToTutees;
	/** Map qui enregistre toutes les associations forcées par les professeurs */
	private final Map<Student, Student> forcedAssignment;
	/** Variable qui donne le poids maximal que peut avoir une arête */
	public final static double POIDS_MAXIMAL;
	/** Variable qui définit combien de tutorés un tuteur peut gérer */
	public final static int MAX_TUTEES_FOR_TUTOR = 2;
	
	/**
	 * Initialise la variable POIDS_MAXIMAL
	 */
	static {
		double poidsMaximal;
		try {
			Tutoring tutoring = new Tutoring();
			Student st1 = new Student("Best", "Tutor", 20.0, 3, 0);
			Student st2 = new Student("Best", "Tutee", 20.0, 1, 0);
			poidsMaximal = tutoring.getWidthArete(st1, st2);
		} catch (ExceptionPromo e) {
			poidsMaximal = Double.MAX_VALUE;
			e.printStackTrace();
		}
		POIDS_MAXIMAL = poidsMaximal;
	}

	public Tutoring() {
		this.tuteeToTutor = new HashMap<>();
		this.tutorToTutees = new HashMap<>();
		this.forcedAssignment = new HashMap<>();
		this.motivations = new HashMap<>();
		this.tutees = new ArrayList<>();
		this.tutors = new ArrayList<>();
	}
	
	public List<Student> getTutees() { return new ArrayList<>(tutees); }
	
	public List<Student> getTutors() { return new ArrayList<>(tutors); }
	
	public Double getMoyenneMaxTutee() { return moyenneMaxTutee; }

	public void setMoyenneMaxTutee(Double moyenneMinTutee) { this.moyenneMaxTutee = moyenneMinTutee; }
	
	public Double getMoyenneMinTutor() { return moyenneMinTutor; }
	
	public void setMoyenneMinTutor(Double moyenneMinTutor) { this.moyenneMinTutor = moyenneMinTutor; }
	
	public Integer getNbAbsencesMax() { return nbAbsencesMax; }

	public void setNbAbsencesMax(Integer nbAbsencesMax) { this.nbAbsencesMax = nbAbsencesMax; }
	
	/**
	 * Retourne le tuteur affecté au tutoré donné en entrée
	 * @param tutee
	 * @return
	 */
	public Student getTuteeAssignment(Student tutee) {
		return tuteeToTutor.get(tutee);
	}
	
	/**
	 * Retourne la liste des affectations pour un tuteur
	 * Renvoie une liste vide si il y a pas de tutorés associés
	 * @param tutor
	 * @return
	 */
	public Set<Student> getTutorAssignment(Student tutor) {
		return tutorToTutees.containsKey(tutor) ? tutorToTutees.get(tutor) : new LinkedHashSet<>();
	}
	
	/**
	 * Méthode pour avoir les points bonus de motivation d'un étudiant.
	 * @param student
	 * @return
	 */
	public double getBonusPoints(Student student) {
		// Retourne la valeur de NEUTRAL si aucune appréciation n'a été donnée
		return motivations.containsKey(student) ? motivations.get(student).getBonusPoints() : Motivation.NEUTRAL.getBonusPoints();
	}
	
	/**
	 * Méthode qui permet de forcer une association entre un tuteur et un tutoré
	 * @param tutee
	 * @param tutor
	 * @throws ExceptionPromo
	 * @throws ExceptionNotInTutoring
	 * @throws ExceptionTooManyTutees 
	 */
	public void forceAssignment(Student tutee, Student tutor) throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyTutees {
		// Retourne une erreur si le tutoré n'est pas en première année
		if (!tutee.canBeTutee()) {
			throw new ExceptionPromo("Le tutoré doit être en première année!");
		// Retourne une erreur si le tuteur est en première année
		} else if (!tutor.canBeTutor()) {
			throw new ExceptionPromo("Le tuteur doit être en deuxième ou troisième année!");
		// Retourne une erreur si le tutoré ou le tuteur n'est pas dans la liste des candidats
		} else if (!tutees.contains(tutee) || !tutors.contains(tutor)) {
			throw new ExceptionNotInTutoring();
		} else if (Collections.frequency(forcedAssignment.values(), tutor) == MAX_TUTEES_FOR_TUTOR) {
			throw new ExceptionTooManyTutees();
		} else {
			// Ajoute l'affectation forcée si tout est valide
			forcedAssignment.put(tutee, tutor);
		}
	}
	
	/**
	 * Méthode qui permet d'annuler une association entre un tuteur et un tutoré qui était forcée
	 * @param tutee
	 */
	public void removeForcedAssignment(Student tutee) {
		if (forcedAssignment.containsKey(tutee)) {
			forcedAssignment.remove(tutee);
		}
	}
	
	/**
	 * Réinitialise les deux maps d'affectations tout en préservant les affectations forcées
	 */
	public void clearAssignments() {
		tuteeToTutor.clear();
		tutorToTutees.clear();
		
		// Remet la lisee des affectations forcées
		for (Student tutee: forcedAssignment.keySet()) {
			addAssignment(tutee, forcedAssignment.get(tutee));
		}
	}
	
	/**
	 * Méthode pour informer la motivation d'un étudiant pour ce tutorat
	 * @param student
	 * @param motivation
	 */
	public void addStudentMotivation(Student student, Motivation motivation) {
		this.motivations.put(student, motivation);
	}

	/**
	 * Inscrit un étudiant pour ce tutorat
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
	 * Inscrit plusieurs étudiants pour ce tutorat
	 */
	public void addAllStudents(Student... students) {
		for (Student std : students) {
			addStudent(std);
		}
	}
	
	/**
	 * Désinscrit l'étudiant du tutorat
	 * @param student
	 * @return
	 */
	public void removeStudent(Student student) {
		if (student.canBeTutee()) {
			// Retire de la liste le tutoré
			tutees.remove(student);
			// Retire éventuellement l'affectation concernant le tutoré
			forcedAssignment.remove(student);
		} else {
			// Retire de la liste le tuteur
			tutors.remove(student);
			// Retire éventuellement toutes les affectations concernant le tuteur
			for (Student tutee : forcedAssignment.keySet()) {
				if (forcedAssignment.get(tutee) == student) {
					forcedAssignment.remove(tutee);
				}
			}
		}
	}
	
	/**
	 * Retourne true si l'étudiant a une moyenne suffisante pour être tuteur/tutoré
	 * @param student
	 * @return
	 */
	public boolean canParticipate(Student student) {
		boolean res;
		// Retourne true si il n'y a pas de filtre ou si l'étudiant a une bonne moyenne
		// et qu'il n'a pas trop d'absences
		if (student.canBeTutee()) {
			res = moyenneMaxTutee == null || student.getMoyenne() <= moyenneMaxTutee;
		} else {
			res = moyenneMinTutor == null || student.getMoyenne() >= moyenneMinTutor;
		}
		res = res && (nbAbsencesMax == null || student.getNbAbsences() <= nbAbsencesMax);
		return res;
	}
	
	/**
	 * Retourne la liste de tous les tuteurs qui peuvent encore être affectés
	 * @return
	 */
	private List<Student> getEligibleTutors() {
		List<Student> eligibleTutors = new ArrayList<>();
		for (Student tutor: tutors) {
			if (this.canParticipate(tutor)) {
				// Un étudiant de 2ème année ne peut aider qu'un seul tutoré
				if (tutor.getPROMO() == 2 && getTutorAssignment(tutor).size() != 1) {
					eligibleTutors.add(tutor);
				// Même vérification pour les élèves de 3ème année
				} else if (tutor.getPROMO() == 3 && getTutorAssignment(tutor).size() != MAX_TUTEES_FOR_TUTOR) {
					eligibleTutors.add(tutor);
				}
			}
		}
		return eligibleTutors;
	}
	
	/**
	 * Retourne la liste de tous les tutorés qui peuvent encore être affectés
	 * @return
	 */
	private List<Student> getEligibleTutees() {
		List<Student> eligibleTutees = new ArrayList<>();
		for (Student tutee: tutees) {
			if (this.canParticipate(tutee) && !tuteeToTutor.containsKey(tutee))
				eligibleTutees.add(tutee);
		}
		return eligibleTutees;
	}
	
	/**
	 * Retourne le poids d'une arête pour un tuteur et un tutoré
	 */
	public double getWidthArete(Student tutee, Student tutor) {
		return (tutor.getScore() + getBonusPoints(tutor)) * (tutee.getScore() - getBonusPoints(tutee));
	}
	
	public void addFakeStudents(List<Student> tuteesList, List<Student> tutorsList, GrapheNonOrienteValue<Student> graphe) throws ExceptionPromo {
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
	}
	
	/**
	 * Méthode qui résout le problème d'affectation (ne crée que des couples)
	 * @return
	 * @throws ExceptionPromo 
	 */
	private GrapheNonOrienteValue<Student> getGrapheTutorTutee(List<Student> tuteesList, List<Student> tutorsList) throws ExceptionPromo {
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
		
		addFakeStudents(tuteesList, tutorsList, graphe);
		
		return graphe;
	}
	
	/**
	 * Méthode qui ajoute une association dans les deux maps
	 */
	void addAssignment(Student tutee, Student tutor) {
		tuteeToTutor.put(tutee, tutor);
		if (!tutorToTutees.containsKey(tutor)) {
			tutorToTutees.put(tutor, new LinkedHashSet<>());
		}
		tutorToTutees.get(tutor).add(tutee);
	}
	
	/**
	 * Méthode qui crée les affectations
	 * (fait des itérations tant qu'il y a encore des tuteurs ou des tutorés)
	 * @throws ExceptionPromo 
	 */
	public void createAssignments() throws ExceptionPromo {
		clearAssignments();
		
		List<Student> eligibleTutors, eligibleTutees;
		
		// Fait des affectations tant qu'il y a toujours des tutorés à affecter
		// et tant qu'il n'y a pas eu trop de répétitions
		do {
			eligibleTutors = getEligibleTutors();
			eligibleTutees = getEligibleTutees();
			
			// Variable qui vérifie si les faux étudiants ajoutés se trouveront dans les tuteurs ou tutorés
			boolean fakeStudentsAreTutees = eligibleTutors.size() > eligibleTutees.size();
			
			GrapheNonOrienteValue<Student> graphe = getGrapheTutorTutee(eligibleTutees, eligibleTutors);
			CalculAffectation<Student> calcul = new CalculAffectation<>(graphe, eligibleTutees, eligibleTutors);
			
			for (Arete<Student> arete : calcul.getAffectation()) {
				Student tutee = arete.getExtremite1();
				Student tutor = arete.getExtremite2();
				
				// Vérifie si l'affectation n'est pas avec un faux étudiant
				if (graphe.getPoids(tutee, tutor) <= POIDS_MAXIMAL) {
					addAssignment(tutee, tutor);
				} else {
					// Enlève ce faux étudiant pour la prochaine affectation
					if (fakeStudentsAreTutees) eligibleTutees.remove(tutee);
					else eligibleTutors.remove(tutor);
				}
			}
		} while (!eligibleTutees.isEmpty() && !eligibleTutors.isEmpty());
	}
	
	/**
	 * Retourne une chaîne de caractères listant tous les éléments d'une map de Student
	 * @param map
	 * @return
	 */
	private String toStringMap(Map<Student, ?> map) {
		StringBuilder res = new StringBuilder();
		List<Student> students = new ArrayList<>();
		students.addAll(map.keySet());
		Collections.sort(students, new ScoreComparator());
		
		for (Student student : students) {
			if (map.containsKey(student)) {
				res.append(student + " --> " + map.get(student) + "\n");
			}
		}
		return res.toString();
	}
	
	/**
	 * Chaîne de caractère donnant pour chaque tutoré le tuteur associé
	 * @return
	 */
	public String toStringTutees() {return toStringMap(tuteeToTutor);}
	
	/**
	 * Chaîne de caractère donnant pour chaque tuteur le/les tutoré(s) associé(s)
	 * @return
	 */
	public String toStringTutors() {return toStringMap(tutorToTutees);}
	
	/**
	 * Par défaut le toString renvoie vers toStringTutees()
	 * @return
	 */
	public String toString() {return toStringTutees();}
}