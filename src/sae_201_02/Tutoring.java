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
	/** Matière dédiée du tutorat */
	private final Subject subject;
	/** Liste des tutorés */
	private final Set<Student> tutees;
	/** Liste des tuteurs */
	private final Set<Student> tutors;
	
	/** Map de motivation des élèves */
	private final Map<Student, Motivation> motivations;
	
	/** Variables pour filtrer les tutorés qui ont une moyenne trop élevée */
	private Double moyenneMaxTutee;
	/** Variable pour filtrer les tuteurs qui ont une moyenne trop faible */
	private Double moyenneMinTutor;
	/** Variable pour filtrer les étudiants qui ont trop d'absences */
	private Integer nbAbsencesMax;
	
	/** Map qui associe les tuteurs aux tutorés */
	private final StudentsAssignment studentAssignment;
	
	/** Map qui enregistre toutes les associations forcées par les professeurs */
	private final StudentsAssignment forcedAssignment;
	
	/** Variable qui définit combien de tutorés un tuteur peut gérer */
	public final static int MAX_TUTEES_FOR_TUTOR = 2;
	/** Variable qui représente le poids de la moyenne dans le calcul du score */
	private double moyenneWidth;
	/** Variable qui représente le nombre d'absences qu'il faut pour perdre un point de score */
	private double absenceWidth;
	
	/**
	 * Constructeur Tutoring avec le poids des moyennes et absences données
	 * en paramètre
	 * @param moyenneWidth
	 * @param absenceWidth
	 */
	public Tutoring(Subject subject, double moyenneWidth, double absenceWidth) {
		this.subject = subject;
		this.studentAssignment = new StudentsAssignment();
		this.forcedAssignment = new StudentsAssignment();
		this.motivations = new HashMap<>();
		this.tutees = new LinkedHashSet<>();
		this.tutors = new LinkedHashSet<>();
		
		setMoyenneWidth(moyenneWidth);
		setAbsenceWidth(absenceWidth);
	}
	
	/**
	 * Constructeur tutoring sans paramètres
	 */
	public Tutoring(Subject subject) {
		this(subject, 1.0, 3.0);
	}
	
	public Subject getSubject() { return this.subject; };
	
	public List<Student> getTutees() { return new ArrayList<>(tutees); }
	
	public List<Student> getTutors() { return new ArrayList<>(tutors); }
	
	public Double getMoyenneMaxTutee() { return moyenneMaxTutee; }

	public void setMoyenneMaxTutee(Double moyenneMinTutee) { this.moyenneMaxTutee = moyenneMinTutee; }
	
	public Double getMoyenneMinTutor() { return moyenneMinTutor; }
	
	public void setMoyenneMinTutor(Double moyenneMinTutor) { this.moyenneMinTutor = moyenneMinTutor; }
	
	public Integer getNbAbsencesMax() { return nbAbsencesMax; }

	public void setNbAbsencesMax(Integer nbAbsencesMax) { this.nbAbsencesMax = nbAbsencesMax; }
	
	public double getAbsenceWidth() { return absenceWidth; }

	public void setAbsenceWidth(double absenceWidth) { this.absenceWidth = absenceWidth; }

	public double getMoyenneWidth() { return moyenneWidth; }

	public void setMoyenneWidth(double moyenneWidth) { this.moyenneWidth = moyenneWidth; }
	
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
	 * @throws ExceptionTooManyAssignments  
	 */
	public void forceAssignment(Student tutee, Student tutor) throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		// Retourne une erreur si le tutoré n'est pas en première année
		if (!tutee.canBeTutee()) {
			throw new ExceptionPromo("Le tutoré doit être en première année!");
		// Retourne une erreur si le tuteur est en première année
		} else if (!tutor.canBeTutor()) {
			throw new ExceptionPromo("Le tuteur doit être en deuxième ou troisième année!");
		// Retourne une erreur si le tutoré ou le tuteur n'est pas dans la liste des candidats
		} else if (!tutees.contains(tutee) || !tutors.contains(tutor)) {
			throw new ExceptionNotInTutoring();
		} else {
			if ((tutor.getPROMO() == 2 && forcedAssignment.get(tutor).size() == 1) 
			   || forcedAssignment.get(tutor).size() == MAX_TUTEES_FOR_TUTOR - 1) {
				
				throw new ExceptionTooManyAssignments(tutor + " a déjà atteint son nombre maximal de tutoré.");
			}
			if (forcedAssignment.contains(tutee)) {
				throw new ExceptionTooManyAssignments("Vous ne pouvez pas associer deux fois " + tutee);
			}
			// Ajoute l'affectation forcée si tout est valide
			forcedAssignment.add(tutee, tutor);
		}
	}
	
	/**
	 * Retire l'affectation d'un tutoré
	 * @param student
	 */
	public void removeForcedAssignment(Student student) {
		forcedAssignment.removeAssignments(student);
	}
	
	/**
	 * Méthode qui permet de forcer une association entre un tuteur et un tutoré
	 * @param tutee
	 * @param tutor
	 * @throws ExceptionPromo
	 * @throws ExceptionNotInTutoring
	 * @throws ExceptionTooManyAssignments  
	 */
	public void removeForcedAssignment(Student tutee, Student tutor) {
		forcedAssignment.removeAssignment(tutee, tutor);
	}
	
	/**
	 * Réinitialise les deux maps d'affectations tout en préservant les affectations forcées
	 */
	public void clearAssignments() {
		studentAssignment.clear();
		
		// Remet la liste des affectations forcées
		for (Student student: forcedAssignment.getTutees()) {
			if (student.canBeTutee())
				studentAssignment.add(student, (Student) forcedAssignment.get(student).toArray()[0]);
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
			forcedAssignment.removeAssignments(student);
		} else {
			// Retire de la liste le tuteur
			tutors.remove(student);
			// Retire éventuellement toutes les affectations concernant le tuteur
			for (Student tutee : forcedAssignment.getTutees()) {
				if (forcedAssignment.get(tutee) == student) {
					forcedAssignment.removeAssignments(tutee);
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
			res = moyenneMaxTutee == null || student.getMoyenne(subject) <= moyenneMaxTutee;
		} else {
			res = moyenneMinTutor == null || student.getMoyenne(subject) >= moyenneMinTutor;
		}
		res = res && (nbAbsencesMax == null || student.getNbAbsences() <= nbAbsencesMax);
		return res;
	}
	
	/**
	 * Retourne la liste de tous les tuteurs qui peuvent encore être affectés
	 * @return
	 */
	private Set<Student> getEligibleTutors() {
		Set<Student> eligibleTutors = new LinkedHashSet<>();
		for (Student tutor: tutors) {
			if (this.canParticipate(tutor)) {
				// Un étudiant de 2ème année ne peut aider qu'un seul tutoré
				if (tutor.getPROMO() == 2 && studentAssignment.get(tutor).size() != 1) {
					eligibleTutors.add(tutor);
				// Même vérification pour les élèves de 3ème année
				} else if (tutor.getPROMO() == 3 && studentAssignment.get(tutor).size() != MAX_TUTEES_FOR_TUTOR) {
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
	private Set<Student> getEligibleTutees() {
		Set<Student> eligibleTutees = new LinkedHashSet<>();
		for (Student tutee: tutees) {
			if (this.canParticipate(tutee) && !studentAssignment.contains(tutee))
				eligibleTutees.add(tutee);
		}
		return eligibleTutees;
	}
	
	/**
	 * Retourne le poids d'une arête pour un tuteur et un tutoré
	 */
	public double getWidthArete(Student tutee, Student tutor) {
		return (tutor.getScore(this) + getBonusPoints(tutor)) * (tutee.getScore(this) - getBonusPoints(tutee));
	}
	
	/**
	 * Remplie une des deux listes de tuteurs ou des tutorés de faux étudiants
	 * pour avoir le même nombre de tuteurs et de tutorés
	 * @param tuteesList
	 * @param tutorsList
	 * @param graphe
	 * @throws ExceptionPromo
	 */
	void addFakeStudents(Set<Student> tuteesList, Set<Student> tutorsList) throws ExceptionPromo {
		
		// Ajoute les tuteurs manquants
		while (tuteesList.size() > tutorsList.size()) {
			Student fakeStudent = new Student("", "", 2, Integer.MAX_VALUE);
			tutorsList.add(fakeStudent);
		}
		
		// Ajoute les tutorés manquants
		while (tutorsList.size() > tuteesList.size()) {
			Student fakeStudent = new Student("", "", 1, Integer.MAX_VALUE);
			tuteesList.add(fakeStudent);
		}
	}
	
	/**
	 * Méthode qui résout le problème d'affectation (ne crée que des couples)
	 * @return
	 * @throws ExceptionPromo 
	 */
	private CalculAffectation<Student> createCoupleTuteeTutor(Set<Student> tuteesList, Set<Student> tutorsList) throws ExceptionPromo {
		GrapheNonOrienteValue<Student> graphe = new GrapheNonOrienteValue<>();
		
		Set<Student> tuteesListCopy = new LinkedHashSet<>(tuteesList);
		Set<Student> tutorsListCopy = new LinkedHashSet<>(tutorsList);

		addFakeStudents(tuteesListCopy, tutorsListCopy);

		// Ajout de tous les sommets
		for (Student student: tuteesListCopy) graphe.ajouterSommet(student);
		for (Student student: tutorsListCopy) graphe.ajouterSommet(student);

		// Ajout des arêtes
		for (Student tutee: tuteesListCopy) {
			for (Student tutor: tutorsListCopy) {
				graphe.ajouterArete(tutee, tutor, this.getWidthArete(tutee, tutor));
			}
		}
		
		return new CalculAffectation<>(graphe, new ArrayList<Student>(tuteesListCopy), new ArrayList<Student>(tutorsListCopy));
	}
	
	
	
	/**
	 * Méthode qui crée les affectations
	 * (fait des itérations tant qu'il y a encore des tuteurs ou des tutorés)
	 * @throws ExceptionPromo 
	 */
	public void createAssignments() throws ExceptionPromo {
		clearAssignments();
		
		Set<Student> eligibleTutors, eligibleTutees;
		
		// Fait des affectations tant qu'il y a toujours des tutorés à affecter
		// et tant qu'il n'y a pas eu trop de répétitions
		do {
			eligibleTutors = getEligibleTutors();
			eligibleTutees = getEligibleTutees();
			
			CalculAffectation<Student> calcul = createCoupleTuteeTutor(eligibleTutees, eligibleTutors);
			
			for (Arete<Student> arete : calcul.getAffectation()) {
				Student tutee = arete.getExtremite1();
				Student tutor = arete.getExtremite2();
				
				// Vérifie si l'affectation n'est pas avec un faux étudiant
				if (eligibleTutors.contains(tutor) && eligibleTutees.contains(tutee)) {
					studentAssignment.add(tutee, tutor);
				}
			}
		} while (!eligibleTutees.isEmpty() && !eligibleTutors.isEmpty());
	}
	
	/**
	 * Retourne une chaîne de caractères listant tous les éléments d'une map de Student
	 * @param map
	 * @return
	 */
	private String toStringStudentAssignment(Set<Student> students) {
		StringBuilder res = new StringBuilder();
		List<Student> studentsList = new ArrayList<>();
		studentsList.addAll(students);
		Collections.sort(studentsList, new ScoreComparator(this));
		
		for (Student student : studentsList) {
			if (studentAssignment.contains(student)) {
				res.append(student + " --> " + studentAssignment.get(student) + "\n");
			}
		}
		return res.toString();
	}
	
	/**
	 * Chaîne de caractère donnant pour chaque tutoré le tuteur associé
	 * @return
	 */
	public String toStringTutees() {return toStringStudentAssignment(tutees);}
	
	/**
	 * Chaîne de caractère donnant pour chaque tuteur le/les tutoré(s) associé(s)
	 * @return
	 */
	public String toStringTutors() {return toStringStudentAssignment(tutors);}
	
	/**
	 * Par défaut le toString renvoie vers toStringTutees()
	 * @return
	 */
	public String toString() {return toStringTutees();}
}