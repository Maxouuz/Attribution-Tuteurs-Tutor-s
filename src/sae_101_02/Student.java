package sae_101_02;

public abstract class Student extends Person {
	private double moyenne;
	private final int promo;
	
	public Student(String forename, String name, double moyenne, int promo) {
		super(forename, name, "etu");
		this.moyenne = moyenne;
		this.promo = promo;
	}

	public double getMoyenne() {
		return moyenne;
	}

	public void setMoyenne(double moyenne) {
		this.moyenne = moyenne;
	}

	public int getPromo() {
		return promo;
	}
}
