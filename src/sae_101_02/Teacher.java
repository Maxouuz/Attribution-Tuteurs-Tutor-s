package sae_101_02;

import java.util.ArrayList;
import java.util.Collection;

public class Teacher extends Person {
	private ArrayList<Subject> subjectsTeached;
	
	public Teacher(String FORENAME, String NAME, Collection<? extends Subject> subjectsTeached) {
		super(FORENAME, NAME);
		this.subjectsTeached = new ArrayList<Subject>();
		this.subjectsTeached.addAll(subjectsTeached);
	}
	
}

