package sae_201_02;

import java.util.Map;

public class Tutee extends Student {
	
	private Map<Tutoring, Tutor> association;
	
	protected Tutee(String FORENAME, String NAME, int PROMO, int nbAbsences, Map<Subject, Double> moyennes) throws ExceptionPromo {
		super(FORENAME, NAME, PROMO, nbAbsences, moyennes);
		if (PROMO != 1) {
			throw new ExceptionPromo("Vous ne pouvez pas créer un tutoré qui n'est pas en première année");
		}
	}
	
	
}
