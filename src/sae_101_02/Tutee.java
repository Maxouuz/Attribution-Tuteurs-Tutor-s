package sae_101_02;

import java.util.ArrayList;
import java.util.Collection;

public class Tutee extends Student {
	private ArrayList<Subject> hardSubjects;
	
	public Tutee(String FORENAME, String NAME, double moyenne, Collection<? extends Subject> hardSubjects) {
		super(FORENAME, NAME, moyenne, 1);
		this.hardSubjects = new ArrayList<Subject>();
		this.hardSubjects.addAll(hardSubjects);
	}
}
