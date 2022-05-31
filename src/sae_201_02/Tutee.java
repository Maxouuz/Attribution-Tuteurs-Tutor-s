package sae_201_02;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Tutee extends Student {
	
	private Map<Tutoring, Tutor> association;
	/** Map qui enregistre toutes les associations forcées par les professeurs */
	private Map<Tutoring, Tutor> forcedAssignment;
	private Map<Tutoring, Tutor> doNotAssign;
	
	protected Tutee(String FORENAME, String NAME, int PROMO, int nbAbsences, Map<Subject, Double> moyennes) throws ExceptionPromo {
		super(FORENAME, NAME, PROMO, nbAbsences, moyennes);
		if (PROMO != 1) {
			throw new ExceptionPromo("Vous ne pouvez pas créer un tutoré qui n'est pas en première année");
		}
		forcedAssignment = new HashMap<>();
	}

	@Override
	public boolean isTutor() {
		return false;
	}

	@Override
	public boolean isTutee() {
		return true;
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
		/*if (!other.isTutor()) {
			throw new ExceptionPromo();
		}
		if ()
		
		if ((other.getPROMO() == 2 && forcedAssignment.get(other).size() == 1) 
			   || forcedAssignment.get(other).size() == maxTuteesForTutor - 1) {
				
			throw new ExceptionTooManyAssignments(other + " a déjà atteint son nombre maximal de tutoré.");
		} else if (forcedAssignment.contains(other)) {
			throw new ExceptionTooManyAssignments("Vous ne pouvez pas associer deux fois " + tutee);
		}
		// Ajoute l'affectation forcée si tout est valide
		forcedAssignment.add(tutee, tutor);*/
	}

	@Override
	public void removeForcedAssignment(Tutoring tutoring) {
		// TODO Auto-generated method stub
		
	}
	
	public void removeTutoring(Tutoring tutoring) {
		if (forcedAssignment.containsKey(tutoring)) forcedAssignment.remove(tutoring);
		if (doNotAssign.containsKey(tutoring)) doNotAssign.remove(tutoring);
	}

	@Override
	public Set<Student> getAssignments(Tutoring tutoring) {
		Set<Student> res = new HashSet<>();
		if (association.containsKey(tutoring)) {
			res.add(association.get(tutoring));
		}
		return res;
	}
}
