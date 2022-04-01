package sae_101_02;

import java.util.ArrayList;
import java.util.Collection;

public class Tutee extends Student {
	private ArrayList<Subject> hardSubjects;
	
	public Tutee(String forename, String name, double moyenne, Collection<? extends Subject> hardSubjects) {
		super(forename, name, moyenne, 1);
		this.hardSubjects = new ArrayList<Subject>();
		this.hardSubjects.addAll(hardSubjects);
	}
}
