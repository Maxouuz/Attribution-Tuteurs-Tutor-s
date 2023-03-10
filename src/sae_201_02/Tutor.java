package sae_201_02;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe représentant un candidat pour être tuteur de deuxième ou troisième année
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 *
 */
public class Tutor extends Student {
	/** Map qui associe pour chaque tutorat une liste de tutorés associés */
	private final Map<Tutoring, Set<Tutee>> assignments;
	/** Map qui associe pour chaque tutorat une liste de tutorés à associer absolument */
	private final Map<Tutoring, Set<Tutee>> forcedAssignment;
	
	/**
	 * Constructeur permettant de créer un tuteur
	 * @param FORENAME
	 * @param NAME
	 * @param PROMO
	 * @param nbAbsences
	 * @param moyennes
	 * @throws ExceptionPromo
	 */
	protected Tutor(int INE, String FORENAME, String NAME, int PROMO, int nbAbsences, Map<Subject, Double> moyennes) throws ExceptionPromo {
		super(INE, FORENAME, NAME, PROMO, nbAbsences, moyennes);
		if (PROMO < 2 || PROMO > 3) {
			throw new ExceptionPromo("Vous ne pouvez pas créer un tutoré qui n'est pas en deuxième ou troisième année!");
		}
		this.assignments = new HashMap<>();
		this.forcedAssignment = new HashMap<>();
	}

	@Override
	public boolean isTutor() {
		return true;
	}

	@Override
	public boolean isTutee() {
		return false;
	}
	
	@Override
	public boolean canAddMoreForcedAssignment(Tutoring tutoring) {
		return (getPromo() == 2 && getForcedAssignments(tutoring).isEmpty()) || (getPromo() == 3 && getForcedAssignments(tutoring).size() < tutoring.getMaxTuteesForTutor());
	}
	
	@Override
	public Set<Student> getAssignments(Tutoring tutoring) {
		Set<Student> res = new HashSet<>();
		if (assignments.containsKey(tutoring)) {
			res.addAll(assignments.get(tutoring));
		}
		return res;
	}
	
	@Override
	public Set<Student> getForcedAssignments(Tutoring tutoring) {
		Set<Student> res = new HashSet<>();
		if (forcedAssignment.containsKey(tutoring)) {
			res.addAll(forcedAssignment.get(tutoring));
		}
		return res;
	}
	
	@Override
	public void addAssignment(Tutoring tutoring, Student other) {
		if (!assignments.containsKey(tutoring)) {
			assignments.put(tutoring, new HashSet<>());
		}
		assignments.get(tutoring).add((Tutee) other);
		if (!other.getAssignments(tutoring).contains(this)) {
			other.addAssignment(tutoring, this);
		}
	}

	@Override
	public void clearAssignment(Tutoring tutoring) {
		if (assignments.containsKey(tutoring)) assignments.remove(tutoring);
		if (forcedAssignment.containsKey(tutoring)) assignments.put(tutoring, new HashSet<>(forcedAssignment.get(tutoring)));
	}

	@Override
	public void removeTutoring(Tutoring tutoring) {
		removeTutoring(assignments, forcedAssignment, tutoring);
	}

	@Override
	protected void forceAssignmentOneWay(Tutoring tutoring, Student other) {
		if (!forcedAssignment.containsKey(tutoring)) {
			forcedAssignment.put(tutoring, new HashSet<>());
		}
		
		forcedAssignment.get(tutoring).add((Tutee) other);
	}
	
	@Override
	public void removeForcedAssignment(Tutoring tutoring, Student student) {
		if (getForcedAssignments(tutoring).contains(student)) {
			forcedAssignment.get(tutoring).remove(student);
			if (student.getForcedAssignments(tutoring).contains(this)) {
				student.removeForcedAssignment(tutoring, this);
			}
		}
	}
}
