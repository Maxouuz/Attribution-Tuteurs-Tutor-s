package sae_201_02;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe abstraite qui représente un étudiant avec une moyenne et une promo
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 *
 */
public abstract class Student extends Person {
	/** Représente la moyenne de l'étudiant */
	private final Map<Subject, Double> moyennes;
	/** Année de promo de l'étudiant */
	private final int PROMO;
	/**Nombre abscences de l'etudiant */
	private int nbAbsences;
	/** Map de motivation des élèves */
	protected final Map<Tutoring, Motivation> motivations;
	/** Liste des étudiants à ne pas associer avec l'étudiant courant (pour chaque Tutorat) */
	protected Map<Tutoring, Set<Student>> studentsToNotAssign;
	
	/** 
	 * Variable qui représente la limite du nombre d'absences
	 * pris en compte dans le calcul du score
	 */
	private final static double SCORE_MAX_ABSENCES = 365;
	
	/**
	 * Constructeur de la classe Student
	 * @param FORENAME
	 * @param NAME
	 * @param moyenne
	 * @param PROMO
	 * @param absence
	 * @throws ExceptionPromo 
	 */
	protected Student(int INE, String FORENAME, String NAME, int PROMO, int nbAbsences, Map<Subject, Double> moyennes) throws ExceptionPromo {
		super(INE, FORENAME, NAME);
		this.moyennes = new HashMap<>(moyennes);
		this.nbAbsences = nbAbsences;
		this.PROMO = PROMO;
		
		this.motivations = new HashMap<>();
		this.studentsToNotAssign = new HashMap<>();
	}
	
	/**
	 * Factory de la classe Student avec l'INE donné par l'utilisateur
	 * @param FORENAME
	 * @param NAME
	 * @param moyenne
	 * @param PROMO
	 * @param absence
	 * @throws ExceptionPromo 
	 */
	public static Student createStudent(int INE, String FORENAME, String NAME, int PROMO, int nbAbsences, Map<Subject, Double> moyennes) throws ExceptionPromo {
		Student res;
		if (PROMO == 1) {
			res = new Tutee(INE, FORENAME, NAME, PROMO, nbAbsences, moyennes);
		} else {
			res = new Tutor(INE, FORENAME, NAME, PROMO, nbAbsences, moyennes);
		}
		return res;
	}
	
	/**
	 * Factory de la classe Student
	 * @param FORENAME
	 * @param NAME
	 * @param moyenne
	 * @param PROMO
	 * @param absence
	 * @throws ExceptionPromo 
	 */
	public static Student createStudent(String FORENAME, String NAME, int PROMO, int nbAbsences, Map<Subject, Double> moyennes) throws ExceptionPromo {
		return createStudent(Person.getNonUsedINE(), FORENAME, NAME, PROMO, nbAbsences, moyennes);
	}
	
	/**
	 * Factory de Student en donnant les moyennes sous forme de varargs avec l'INE donné par l'utilisateur
	 * @param FORENAME
	 * @param NAME
	 * @param PROMO
	 * @param nbAbsences
	 * @throws ExceptionPromo
	 */
	public static Student createStudent(int INE, String FORENAME, String NAME, int PROMO, int nbAbsences, double... moyennes) throws ExceptionPromo {
		Student res = createStudent(INE, FORENAME, NAME, PROMO, nbAbsences, new HashMap<>());
		for (int i = 0; i < moyennes.length && i < Subject.values().length; i++) {
			res.setMoyenne(Subject.values()[i], moyennes[i]);
		}
		return res;
	}
	
	/**
	 * Factory de Student en donnant les moyennes sous forme de varargs
	 * @param FORENAME
	 * @param NAME
	 * @param PROMO
	 * @param nbAbsences
	 * @throws ExceptionPromo
	 */
	public static Student createStudent(String FORENAME, String NAME, int PROMO, int nbAbsences, double... moyennes) throws ExceptionPromo {
		return createStudent(Person.getNonUsedINE(), FORENAME, NAME, PROMO, nbAbsences, moyennes);
	}
	
	/**
	 * Retourne le login de la personne avec un .etu à la fin du login
	 * @return
	 */
	public String getLogin() {
		return super.getLogin() + ".etu";
	}

	/**
	 * Retourne la moyenne de l'étudiant
	 * @return
	 */
	public double getMoyenne(Subject subject) {
		double moyenne;
		if (moyennes.containsKey(subject))
			moyenne = moyennes.get(subject);
		else
			moyenne = 0;
		return moyenne;
	}
	
	/**
	 * Changer la moyenne d'un étudiant
	 * @param moyenne
	 */
	public void setMoyenne(Subject subject, double moyenne) {
		this.moyennes.put(subject, moyenne);
	}

	/**
	 * Retourne l'année de la promo d'un étudiant
	 * @return
	 */
	public int getPromo() {
		return PROMO;
	}
	
	@Override
	public String toString() {
		return super.toString() + " (moyenne: " + this.moyennes + ", promo: " + this.PROMO + ", absences: " + this.nbAbsences + ")";
	}
	
	/**
	 * Méthode qui calcule un score pour l'étudiant pour un tutorat
	 * Plus le score est grand, plus l'étudiant est considéré comme meilleur
	 * Les critères pris en compte sont:
	 * - La promo
	 * - La moyenne
	 * - Le nombre d'absences
	 * @return
	 */
	public double getScore(Tutoring tutoring) {
		/** Variable qui représente l'écart des scores entre les différentes promos */
		double promoScoreGap = (SCORE_MAX_ABSENCES / tutoring.getAbsenceWidth()) + 21;
		
		double nbAbsencesCopy = nbAbsences;
		
		if (nbAbsencesCopy > SCORE_MAX_ABSENCES) {
			nbAbsencesCopy = SCORE_MAX_ABSENCES;
		}
		return PROMO * promoScoreGap + getMoyenne(tutoring.getSubject()) * tutoring.getMoyenneWidth()- (nbAbsencesCopy / tutoring.getAbsenceWidth());
	}
	
	/**
	 * Méthode qui calcule un score pour l'étudiant avec les poids par défaut
	 * @return
	 */
	public double getScore() {
		return getScore(new Tutoring(Subject.R101));
	}

	/**
	 * Retourne le nombre d'absence d'un etudiant
	 * @return
	 */
	public int getNbAbsences() {
		return nbAbsences;
	}

	/**
	 * Change le nombre d'absence d'un étudiant
	 * @param absence
	 */
	public void setNbAbsences(int nbAbsences) {
		if (nbAbsences <= 0)
			throw new IllegalArgumentException("Le nombre d'absences doit être un nombre positif!");
		this.nbAbsences = nbAbsences;
	}
	
	/**
	 * Méthode pour informer la motivation d'un étudiant pour ce tutorat
	 * @param student
	 * @param motivation
	 */
	public void setMotivation(Tutoring tutoring, Motivation motivation) {
		this.motivations.put(tutoring, motivation);
	}
	
	/**
	 * Méthode pour récupérer la motivation d'un étudiant pour un tutorat
	 * @param tutoring
	 * @return
	 */
	public Motivation getMotivation(Tutoring tutoring) {
		return motivations.containsKey(tutoring) ? motivations.get(tutoring) : Motivation.NEUTRAL;
	}
	
	/**
	 * Méthode pour avoir les points bonus de motivation d'un étudiant.
	 * @param student
	 * @return
	 */
	public double getBonusPoints(Tutoring tutoring) {
		return motivations.containsKey(tutoring) ? motivations.get(tutoring).getBonusPoints() : Motivation.NEUTRAL.getBonusPoints();
	}
	
	/**
	 * Permet de retirer un étudiant du tutorat
	 * @param tutoring
	 */
	protected <T> void removeTutoring(Map<Tutoring, T> assignments, Map<Tutoring, T> forcedAssignment, Tutoring tutoring) {
		if (forcedAssignment.containsKey(tutoring)) removeForcedAssignments(tutoring);
		if (studentsToNotAssign.containsKey(tutoring)) removeStudentsToNotAssign(tutoring);
	}
	
	/**
	 * Méthode pour ne pas assigner un étudiant avec un autre
	 * @param tutoring
	 * @param other
	 * @throws ExceptionNotInTutoring 
	 * @throws ExceptionPromo 
	 */
	public void doNotAssign(Tutoring tutoring, Student other) throws ExceptionNotInTutoring, ExceptionPromo {
		boolean addedForOther = other.getStudentsToNotAssign(tutoring).contains(this);
		
		if (!addedForOther) {
			if (getClass() == other.getClass()) {
				throw new ExceptionPromo(); 
			} else if (!tutoring.isInTutoring(this) || !tutoring.isInTutoring(other)) {
				throw new ExceptionNotInTutoring();
			}
		}
		
		if (!studentsToNotAssign.containsKey(tutoring)) {
			studentsToNotAssign.put(tutoring, new HashSet<>());
		}
		studentsToNotAssign.get(tutoring).add(other);
		
		if (!addedForOther) {
			other.doNotAssign(tutoring, this);
		}
	}
	
	/**
	 * Méthode pour récupérer tous les étudiants à ne pas assigner avec lui pour un tutorat
	 * @param tutoring
	 * @return
	 */
	public Set<Student> getStudentsToNotAssign(Tutoring tutoring) {
		Set<Student> res = new HashSet<>();
		if (studentsToNotAssign.containsKey(tutoring)) {
			res.addAll(studentsToNotAssign.get(tutoring));
		}
		return res;
	}
	
	/**
	 * Méthode qui permet de forcer une association entre un tuteur et un tutoré
	 * @param tutee
	 * @param tutor
	 * @throws ExceptionPromo
	 * @throws ExceptionNotInTutoring
	 * @throws ExceptionTooManyAssignments  
	 */
	public void forceAssignment(Tutoring tutoring, Student other) throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		boolean addedForOther = other.getForcedAssignments(tutoring).contains(this);

		if (!addedForOther) {
			if (getClass() == other.getClass()) {
				throw new ExceptionPromo(); 
			} else if (!tutoring.isInTutoring(this) || !tutoring.isInTutoring(other)) {
				throw new ExceptionNotInTutoring();
			} else if (!canAddMoreForcedAssignment(tutoring) || !other.canAddMoreForcedAssignment(tutoring)) {
				throw new ExceptionTooManyAssignments();
			}
		}
		
		forceAssignmentOneWay(tutoring, other);
		
		if (!addedForOther)
			other.forceAssignmentOneWay(tutoring, this);
	}
	
	public Map<Subject, Double> getMoyennes() {
		Map<Subject, Double> copy = new HashMap<>();
		for (Subject subject: moyennes.keySet()) {
			copy.put(subject, moyennes.get(subject));
		}
		return copy;
	}
	
	/**
	 * Méthode pour forcer une affectation entre deux étudiants
	 * @param tutoring
	 * @param other
	 *//**
	 * Méthode pour effacer la liste des étudiants à ne pas affecter
	 * @param tutoring
	 */
	protected abstract void forceAssignmentOneWay(Tutoring tutoring, Student other);

	/**
	 * Retourne true si l'étudiant peut dispenser le tutorat sur une ressource
	 * @return
	 */
	public abstract boolean isTutor();
	
	/**
	 * Retourne true si l'étudiant peut bénéficier du tutorat pour une ressource
	 * @return
	 */
	public abstract boolean isTutee();
	
	/**
	 * Méthode pour retirer un élève d'un tutorat
	 * @param tutoring
	 */
	public abstract void removeTutoring(Tutoring tutoring);
	
	/**
	 * Méthode pour supprimer les affectations calculées
	 * @param tutoring
	 */
	public abstract void clearAssignment(Tutoring tutoring);
	
	/**
	 * Retourne true si l'étudiant peut encore avoir une affectation forcée
	 * @param tutoring
	 * @return
	 */
	public abstract boolean canAddMoreForcedAssignment(Tutoring tutoring);
	
	/**
	 * Méthode pour récupérer toutes les affectations forcées d'un étudiant pour un tutorat
	 * @param tutoring
	 * @return
	 */
	public abstract Set<Student> getForcedAssignments(Tutoring tutoring);
	
	/**
	 * Méthode pour enlever toutes les affectations forcées d'un étudiant
	 * @param tutoring
	 */
	public void removeForcedAssignments(Tutoring tutoring) {
		for (Student other: getForcedAssignments(tutoring)) {
			removeForcedAssignment(tutoring, other);
		}
	}
	
	/**
	 * Méthode pour enlever une affectation forcée d'un étudiant
	 * @param tutoring
	 * @param student
	 */
	public abstract void removeForcedAssignment(Tutoring tutoring, Student student);
	
	/**
	 * Méthode pour effacer la liste des étudiants à ne pas affecter
	 * @param tutoring
	 */
	public void removeStudentsToNotAssign(Tutoring tutoring) {
		if (studentsToNotAssign.containsKey(tutoring)) {
			for (Student other: studentsToNotAssign.remove(tutoring)) {
				other.removeStudentToNotAssign(tutoring, this);
			}
		}
	}
	
	/**
	 * Méthode pour donner la possibilité de réassigner à nouveau une affectation annulée
	 * @param tutoring
	 * @param student
	 */
	public void removeStudentToNotAssign(Tutoring tutoring, Student student) {
		if (getStudentsToNotAssign(tutoring).contains(student)) {
			studentsToNotAssign.get(tutoring).remove(student);
			if (student.getStudentsToNotAssign(tutoring).contains(this)) {
				student.removeStudentToNotAssign(tutoring, this);
			}
		}
	}
	
	/**
	 * Méthodes pour récupérer toutes les affectations d'un étudiant
	 * @param tutoring
	 * @return
	 */
	public abstract Set<Student> getAssignments(Tutoring tutoring);
	
	/**
	 * Méthode pour ajouter une affectation à un étudiant
	 * @param tutoring
	 * @param other
	 */
	public abstract void addAssignment(Tutoring tutoring, Student other);
}
