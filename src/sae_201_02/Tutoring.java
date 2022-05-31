package sae_201_02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
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
	private final Set<Tutee> tutees;
	/** Liste des tuteurs */
	private final Set<Tutor> tutors;
	
	/** Variables pour filtrer les tutorés qui ont une moyenne trop élevée */
	private Double moyenneMaxTutee;
	/** Variable pour filtrer les tuteurs qui ont une moyenne trop faible */
	private Double moyenneMinTutor;
	/** Variable pour filtrer les étudiants qui ont trop d'absences */
	private Integer nbAbsencesMax;
	/** Variable qui définit combien de tutorés un tuteur peut gérer */
	public int maxTuteesForTutor;
	
	/** Map qui associe les tuteurs aux tutorés */
	// private final StudentsAssignment studentsAssignment;
	
	/** Map qui enregistre toutes les associations qui ne doit pas être faites */
	// private final StudentsAssignment studentsToNotAssign;

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
	public Tutoring(Subject subject, int maxTuteesForTutor, double moyenneWidth, double absenceWidth) {
		this.subject = subject;
		this.tutees = new LinkedHashSet<>();
		this.tutors = new LinkedHashSet<>();
		
		setMaxTuteesForTutor(maxTuteesForTutor);
		setMoyenneWidth(moyenneWidth);
		setAbsenceWidth(absenceWidth);
	}
	
	/**
	 * Constructeur tutoring sans paramètres
	 */
	public Tutoring(Subject subject) {
		this(subject, 2, 1.0, 3.0);
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
	
	public int getMaxTuteesForTutor() { return maxTuteesForTutor; }

	public void setMaxTuteesForTutor(int maxTuteesForTutor) { this.maxTuteesForTutor = maxTuteesForTutor; }
	
	/**
	 * Prend en paramètres un couple d'étudiant
	 * Renvoie une erreur si le tutoré n'est pas en première année et si le tuteur est en première année,
	 * et renvoie une erreur si un des deux n'est pas inscrit au tutorat
	 * @param tutee
	 * @param tutor
	 * @throws ExceptionPromo
	 * @throws ExceptionNotInTutoring
	 */
	public void checkTuteeTutorParameters(Student tutee, Student tutor) throws ExceptionPromo, ExceptionNotInTutoring {
		// Retourne une erreur si le tutoré n'est pas en première année
		if (!tutee.isTutee()) {
			throw new ExceptionPromo("Le tutoré doit être en première année!");
		// Retourne une erreur si le tuteur est en première année
		} else if (!tutor.isTutor()) {
			throw new ExceptionPromo("Le tuteur doit être en deuxième ou troisième année!");
		// Retourne une erreur si le tutoré ou le tuteur n'est pas dans la liste des candidats
		} else if (!tutees.contains(tutee) || !tutors.contains(tutor)) {
			throw new ExceptionNotInTutoring();
		}
	}
	
	/**
	 * Méthode permettant d'ajouter un étudiant au tutorat
	 * Il appelle soit addStudent(Tutor) soit addStudent(Tutee)
	 * @param student
	 * @return
	 */
	public boolean addStudent(Student student) {
		return student.isTutee() ? addStudent((Tutee) student) : addStudent((Tutor) student);
	}
	
	/**
	 * Inscrit un tutoré pour ce tutorat
	 * @param student
	 * @return
	 */
	public boolean addStudent(Tutee student) {
		return tutees.add(student);
	}
	
	/**
	 * Inscrit un tuteur pour ce tutorat
	 * @param student
	 * @return
	 */
	public boolean addStudent(Tutor student) {
		return tutors.add(student);
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
		if (student.isTutee()) {
			// Retire de la liste le tutoré
			tutees.remove(student);
		} else {
			// Retire de la liste le tuteur
			tutors.remove(student);
		}
		student.removeTutoring(this);
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
		if (student.isTutee()) {
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
	private Set<Tutor> getEligibleTutors() {
		Set<Tutor> eligibleTutors = new LinkedHashSet<>();
		for (Tutor tutor: tutors) {
			if (this.canParticipate(tutor)) {
				// Un étudiant de 2ème année ne peut aider qu'un seul tutoré
				if (tutor.getPROMO() == 2 && tutor.getAssignments(this).size() != 1) {
					eligibleTutors.add(tutor);
				// Même vérification pour les élèves de 3ème année
				} else if (tutor.getPROMO() == 3 && tutor.getAssignments(this).size() != maxTuteesForTutor) {
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
	private Set<Tutee> getEligibleTutees() {
		Set<Tutee> eligibleTutees = new LinkedHashSet<>();
		for (Tutee tutee: tutees) {
			if (this.canParticipate(tutee) && tutee.getAssignments(this).size() == 0)
				eligibleTutees.add(tutee);
		}
		return eligibleTutees;
	}
	
	/**
	 * Retourne le poids d'une arête pour un tuteur et un tutoré
	 */
	public double getWidthArete(Student tutee, Student tutor) {
		return (tutor.getScore(this) + tutor.getBonusPoints(this)) * (tutee.getScore(this) - tutee.getBonusPoints(this));
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
			Student fakeStudent = Student.createStudent("", "", 2, Integer.MAX_VALUE);
			tutorsList.add(fakeStudent);
		}
		
		// Ajoute les tutorés manquants
		while (tutorsList.size() > tuteesList.size()) {
			Student fakeStudent = Student.createStudent("", "", 1, Integer.MAX_VALUE);
			tuteesList.add(fakeStudent);
		}
	}
	
	/**
	 * Réinitialise les deux maps d'affectations tout en préservant les affectations forcées
	 */
	public void clearAssignments() {
		for (Student student: tutees) {
			student.clearAssignment(this);
		}
		for (Student student: tutors) {
			student.clearAssignment(this);
		}
	}
	
	/**
	 * Méthode qui résout le problème d'affectation (ne crée que des couples)
	 * @return
	 * @throws ExceptionPromo 
	 */
	private CalculAffectation<Student> createCoupleTuteeTutor(Set<Tutee> tuteesList, Set<Tutor> tutorsList) throws ExceptionPromo {
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
				if (!tutee.getStudentsToNotAssign(this).contains(tutor)) 
					graphe.ajouterArete(tutee, tutor, this.getWidthArete(tutee, tutor));
				else
					graphe.ajouterArete(tutee, tutor, Double.MAX_VALUE);
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
		
		Set<Tutor> eligibleTutors;
		Set<Tutee> eligibleTutees;
		
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
					tutee.addAssignment(this, tutor);
				}
			}
		} while (!eligibleTutees.isEmpty() && !eligibleTutors.isEmpty());
	}
	
	/**
	 * Retourne une chaîne de caractères listant tous les éléments d'une map de Student
	 * @param map
	 * @return
	 */
	private String toStringStudentAssignment(Set<? extends Student> students) {
		StringBuilder res = new StringBuilder();
		List<Student> studentsList = new ArrayList<>();
		studentsList.addAll(students);
		Collections.sort(studentsList, new ScoreComparator(this));
		
		for (Student student : studentsList) {
			if (!student.getAssignments(this).isEmpty()) {
				res.append(student + " --> " + student.getAssignments(this) + "\n");
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