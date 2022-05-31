package sae_201_02;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.json.JSONString;

/**
 * Classe abstraite qui représente un étudiant avec une moyenne et une promo
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 *
 */
public abstract class Student extends Person implements JSONString {
	/** Représente la moyenne de l'étudiant */
	private final Map<Subject, Double> moyennes;
	/** Année de promo de l'étudiant */
	private final int PROMO;
	/**Nombre abscences de l'etudiant */
	private int nbAbsences;
	/** Map de motivation des élèves */
	private final Map<Tutoring, Motivation> motivations;
	/** Variable qui définit combien de tutorés un tuteur peut gérer */
	public int maxTuteesForTutor;
	/** 
	 * Variable qui représente la limite du nombre d'absences
	 * pris en compte dans le calcul du score
	 */
	private final static double SCORE_MAX_ABSENCES = 365;
	
	/**
	 * Factory de la classe Student
	 * @param FORENAME
	 * @param NAME
	 * @param moyenne
	 * @param PROMO
	 * @param absence
	 * @throws ExceptionPromo 
	 */
	protected Student(String FORENAME, String NAME, int PROMO, int nbAbsences, Map<Subject, Double> moyennes) throws ExceptionPromo {
		super(FORENAME, NAME);
		this.moyennes = new HashMap<>(moyennes);
		this.nbAbsences = nbAbsences;
		if (PROMO < 1 || PROMO > 3) {
			throw new ExceptionPromo("La promo de l'étudiant doit être compris entre 1 et 3!");
		}
		this.PROMO = PROMO;
		this.motivations = new HashMap<>();
		setMaxTuteesForTutor(maxTuteesForTutor);
	}
	
	/**
	 * Constructeur de la classe Student
	 * @param FORENAME
	 * @param NAME
	 * @param moyenne
	 * @param PROMO
	 * @param absence
	 * @throws ExceptionPromo 
	 */
	public static Student createStudent(String FORENAME, String NAME, int PROMO, int nbAbsences, Map<Subject, Double> moyennes) throws ExceptionPromo {
		Student res;
		if (PROMO == 1) {
			res = new Tutee(FORENAME, NAME, PROMO, nbAbsences, moyennes);
		} else {
			res = new Tutor(FORENAME, NAME, PROMO, nbAbsences, moyennes);
		}
		return res;
	}
	
	/**
	 * Second constructeur de Student sans donner les moyennes
	 * @param FORENAME
	 * @param NAME
	 * @param PROMO
	 * @param nbAbsences
	 * @throws ExceptionPromo
	 */
	public static Student createStudent(String FORENAME, String NAME, int PROMO, int nbAbsences) throws ExceptionPromo {
		return createStudent(FORENAME, NAME, PROMO, nbAbsences, new HashMap<>());
	}
	
	/**
	 * Second constructeur de Student sans donner les moyennes
	 * @param FORENAME
	 * @param NAME
	 * @param PROMO
	 * @param nbAbsences
	 * @throws ExceptionPromo
	 */
	public static Student createStudent(String FORENAME, String NAME, int PROMO, int nbAbsences, double... moyennes) throws ExceptionPromo {
		Student res = createStudent(FORENAME, NAME, PROMO, nbAbsences);
		for (int i = 0; i < moyennes.length && i < Subject.values().length; i++) {
			res.setMoyenne(Subject.values()[i], moyennes[i]);
		}
		return res;
	}
	
	public int getMaxTuteesForTutor() { return maxTuteesForTutor; }

	public void setMaxTuteesForTutor(int maxTuteesForTutor) { this.maxTuteesForTutor = maxTuteesForTutor; }
	
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
	public int getPROMO() {
		return PROMO;
	}
	
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

	@Override
	public String toJSONString() {
		JSONObject json = new JSONObject(super.toJSONString());
		json.put("moyennes", moyennes);
		json.put("promo", PROMO);
		json.put("nbAbsences", nbAbsences);
		return json.toString();
	}
	
	/**
	 * Méthode pour informer la motivation d'un étudiant pour ce tutorat
	 * @param student
	 * @param motivation
	 */
	public void addStudentMotivation(Tutoring tutoring, Motivation motivation) {
		this.motivations.put(tutoring, motivation);
	}
	
	/**
	 * Méthode pour avoir les points bonus de motivation d'un étudiant.
	 * @param student
	 * @return
	 */
	public double getBonusPoints(Tutoring tutoring) {
		return motivations.containsKey(tutoring) ? motivations.get(tutoring).getBonusPoints() : Motivation.NEUTRAL.getBonusPoints();
	}
	
	public abstract void forceAssignment(Tutoring tutoring, Student tutor) throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments;
	
	/**
	 * Retire l'affectation forcée d'un étudiant
	 * @param student
	 */
	public abstract void removeForcedAssignment(Tutoring tutoring);
	
	public abstract void removeTutoring(Tutoring tutoring);
	
	public abstract Set<Student> getAssignments(Tutoring tutoring);
}
