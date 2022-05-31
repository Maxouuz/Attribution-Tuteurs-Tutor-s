package sae_201_02;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe représentant un candidat pour être tutoré de première année
 * @author nathan.hallez.etu
 *
 */
public class Tutee extends Student {
	/** Map qui associe pour chaque tutorat un tuteur */
	private final Map<Tutoring, Tutor> assignments;
	/** Map qui enregistre toutes les associations forcées par les professeurs */
	private final Map<Tutoring, Tutor> forcedAssignment;
	
	/**
	 * Constructeur permettant de créer un tutoré
	 * @param FORENAME
	 * @param NAME
	 * @param PROMO
	 * @param nbAbsences
	 * @param moyennes
	 * @throws ExceptionPromo
	 */
	protected Tutee(String FORENAME, String NAME, int PROMO, int nbAbsences, Map<Subject, Double> moyennes) throws ExceptionPromo {
		super(FORENAME, NAME, PROMO, nbAbsences, moyennes);
		if (PROMO != 1) {
			throw new ExceptionPromo("Vous ne pouvez pas créer un tutoré qui n'est pas en première année");
		}
		this.assignments = new HashMap<>();
		this.forcedAssignment = new HashMap<>();
	}

	@Override
	public boolean isTutor() {
		return false;
	}

	@Override
	public boolean isTutee() {
		return true;
	}

	@Override
	public void forceAssignment(Tutoring tutoring, Student other) throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		if (other.isTutee()) {
			throw new ExceptionPromo();
		} else if (!tutoring.getTutors().contains(other)) {
			throw new ExceptionNotInTutoring();
		} else if (getPROMO() == 1 && getForcedAssignments(tutoring).size() == 1) {
			throw new ExceptionTooManyAssignments();
		}
		
		forcedAssignment.put(tutoring, (Tutor) other);
		
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
			res.add(assignments.get(tutoring));
		}
		return res;
	}
	
	@Override
	public Set<Student> getForcedAssignments(Tutoring tutoring) {
		Set<Student> res = new HashSet<>();
		if (forcedAssignment.containsKey(tutoring)) {
			res.add(forcedAssignment.get(tutoring));
		}
		return res;
	}

	@Override
	public Set<Student> getStudentsToNotAssign(Tutoring tutoring) {
		Set<Student> res = new HashSet<>();
		if (studentsToNotAssign.containsKey(tutoring)) {
			res.addAll(studentsToNotAssign.get(tutoring));
		}
		return res;
	}
	
	@Override
	public void addAssignment(Tutoring tutoring, Student other) {
		assignments.put(tutoring, (Tutor) other);
	}
}
