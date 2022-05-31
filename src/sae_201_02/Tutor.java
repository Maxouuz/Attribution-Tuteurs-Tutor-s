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

	@Override
	public void forceAssignment(Student tutor) throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		
	}
	
	public void removeTutoring(Tutoring tutoring) {
		if (forcedAssignment.containsKey(tutoring)) forcedAssignment.remove(tutoring);
		if (doNotAssign.containsKey(tutoring)) doNotAssign.remove(tutoring);
	}

	@Override
	public void forceAssignment(Tutoring tutoring, Student tutor)
			throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		// TODO Auto-generated method stub
		
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
}
