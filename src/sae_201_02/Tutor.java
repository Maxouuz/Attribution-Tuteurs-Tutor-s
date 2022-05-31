package sae_201_02;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Tutor extends Student {
	
	private Map<Tutoring, Set<Tutee>> association;
	private Map<Tutoring, Set<Tutee>> forcedAssignment;
	private Map<Tutoring, Set<Tutee>> doNotAssign;
	
	protected Tutor(String FORENAME, String NAME, int PROMO, int nbAbsences, Map<Subject, Double> moyennes) throws ExceptionPromo {
		super(FORENAME, NAME, PROMO, nbAbsences, moyennes);
	}

	@Override
	public boolean isTutor() {
		return true;
	}

	@Override
	public boolean isTutee() {
		return false;
	}
	
	public void removeTutoring(Tutoring tutoring) {
		if (forcedAssignment.containsKey(tutoring)) forcedAssignment.remove(tutoring);
		if (doNotAssign.containsKey(tutoring)) doNotAssign.remove(tutoring);
	}
	
	public void clearAssignment(Tutoring tutoring) {
		if (association.containsKey(tutoring)) association.get(tutoring).clear();
		if (forcedAssignment.containsKey(tutoring)) association.put(tutoring, forcedAssignment.get(tutoring));
	}

	@Override
	public void forceAssignment(Tutoring tutoring, Student other) throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		if (other.isTutee()) {
			throw new ExceptionPromo();
		} else if (!forcedAssignment.containsKey(tutoring) || forcedAssignment.get(tutoring).size() == tutoring.getMaxTuteesForTutor()) {
			throw new ExceptionTooManyAssignments();
		}
		forcedAssignment.get(tutoring).add((Tutee) other);
	}

	@Override
	public void removeForcedAssignment(Tutoring tutoring) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Student> getAssignments(Tutoring tutoring) {
		Set<Student> res = new HashSet<>();
		if (association.containsKey(tutoring)) {
			res.addAll(association.get(tutoring));
		}
		return res;
	}

	@Override
	public Set<Student> getStudentsToNotAssign(Tutoring tutoring) {
		Set<Student> res = new HashSet<>();
		if (doNotAssign.containsKey(tutoring)) {
			res.addAll(doNotAssign.get(tutoring));
		}
		return res;
	}
	
	public void addAssignment(Tutoring tutoring, Student other) {
		// TODO: PAS FINI
		association.get(tutoring).add((Tutee) other);
	}

	@Override
	public void doNotAssign(Tutoring tutoring, Student other) {
		doNotAssign.get(tutoring).add((Tutee) other);		
	}
}
