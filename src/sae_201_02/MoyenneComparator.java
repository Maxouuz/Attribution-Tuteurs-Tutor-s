package sae_201_02;

import java.util.Comparator;

/**
 * Comparateur supplémentaire pour la classe Student qui compare la moyenne
 * @author lezlon
 *
 */
public class MoyenneComparator implements Comparator<Student> {

	@Override
	public int compare(Student student1, Student student2) {
		return (int) Math.floor(student1.getMoyenne() - student2.getMoyenne());
	}
	
}
