package sae_201_02;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe représentant un candidat pour être tuteur de deuxième ou troisième année
 * @author nathan.hallez.etu
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
	protected Tutor(String FORENAME, String NAME, int PROMO, int nbAbsences, Map<Subject, Double> moyennes) throws ExceptionPromo {
		super(FORENAME, NAME, PROMO, nbAbsences, moyennes);
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
	public void forceAssignment(Tutoring tutoring, Student other) throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		if (other.isTutor()) {
			throw new ExceptionPromo();
		} else if (!tutoring.getTutees().contains(other)) {
			throw new ExceptionNotInTutoring();
		} else if (getPROMO() == 2 && getForcedAssignments(tutoring).size() == 1
				  || getForcedAssignments(tutoring).size() == tutoring.getMaxTuteesForTutor()) {
			throw new ExceptionTooManyAssignments();
		}
		
		if (!forcedAssignment.containsKey(tutoring)) {
			forcedAssignment.put(tutoring, new HashSet<>());
		}
		
		forcedAssignment.get(tutoring).add((Tutee) other);
		
		if (!other.getForcedAssignments(tutoring).contains(this))
			other.forceAssignment(tutoring, this);
	}

	@Override
	public void removeForcedAssignment(Tutoring tutoring) {
		if (forcedAssignment.containsKey(tutoring)) forcedAssignment.remove(tutoring);
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
		assignments.get(tutoring).add((Tutee) other);
	}
}