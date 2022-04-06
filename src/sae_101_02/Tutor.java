package sae_101_02;

public class Tutor extends Student {
	private Subject subjectTeached;
	protected int promo;
	
	public Tutor(String FORENAME, String NAME, double moyenne, int PROMO, Subject subjectTeached) {
		// TODO: Prévoir le fait que la variable promo soit inférieur à 2 ou supérieur à 3
		
		super(FORENAME, NAME, moyenne);
		this.subjectTeached = subjectTeached; 
		if(this.promo < 2 || this.promo > 3) {
			this.promo = 2;
		}
	}
	
	public Subject getSubjectTeached() {
		return subjectTeached;
	}

	public void setSubjectTeached(Subject subjectTeached) {
		this.subjectTeached = subjectTeached;
	}

	public int getPromo() {
		return promo;
	}	
	
	
}
