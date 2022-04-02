package sae_101_02;

public class Tutor extends Student {
	private Subject subjectTeached;
	
	public Tutor(String FORENAME, String NAME, double moyenne, int PROMO, Subject subjectTeached) {
		// TODO: Prévoir le fait que la variable promo soit inférieur à 2 ou supérieur à 3
		super(FORENAME, NAME, moyenne, PROMO);
		this.subjectTeached = subjectTeached;
	}

	public Subject getSubjectTeached() {
		return subjectTeached;
	}

	public void setSubjectTeached(Subject subjectTeached) {
		this.subjectTeached = subjectTeached;
	}
}
