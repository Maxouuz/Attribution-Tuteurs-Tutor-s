package sae_101_02;

public abstract class Student extends Person {
	private double moyenne;
	private final int PROMO;
	
	public Student(String FORENAME, String NAME, double moyenne, int PROMO) {
		super(FORENAME, NAME, "etu");
		this.moyenne = moyenne;
		this.PROMO= PROMO;
	}

	public double getMoyenne() {
		return moyenne;
	}

	public void setMoyenne(double moyenne) {
		this.moyenne = moyenne;
	}

	public int getPromo() {
		return PROMO;
	}
}
