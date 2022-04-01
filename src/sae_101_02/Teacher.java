package sae_101_02;

import java.util.ArrayList;
import java.util.Collection;

public class Teacher extends Person {
	private ArrayList<Subject> subjectsTeached;
	
	public Teacher(String forename, String name, Collection<? extends Subject> subjectsTeached) {
		super(forename, name);
		this.subjectsTeached = new ArrayList<Subject>();
		this.subjectsTeached.addAll(subjectsTeached);
	}
	
}
