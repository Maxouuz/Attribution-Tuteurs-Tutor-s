package sae_201_02;

import java.util.Map;

public class Tutor extends Student {
	
	private Set<Tutee> association;
	
	protected Tutor(String FORENAME, String NAME, int PROMO, int nbAbsences, Map<Subject, Double> moyennes) throws ExceptionPromo {
		super(FORENAME, NAME, PROMO, nbAbsences, moyennes);
	}
	
}
