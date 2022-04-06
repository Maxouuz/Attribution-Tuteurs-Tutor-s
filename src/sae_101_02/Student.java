package sae_101_02;

public abstract class Student extends Person {
	private double moyenne;
	
	public Student(String FORENAME, String NAME, double moyenne) {
		super(FORENAME, NAME, "etu");
		this.moyenne = moyenne;
	}

	public double getMoyenne() {
		return moyenne;
	}

	public void setMoyenne(double moyenne) {
		this.moyenne = moyenne;
	}

}
