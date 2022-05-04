package sae_201_02;

import java.util.Comparator;

/**
 * Comparateur pour la classe Student qui compare le score (poids des arêtes)
 * @author lezlon
 *
 */
public class ScoreComparator implements Comparator<Student> {

	@Override
	public int compare(Student student1, Student student2) {
		return (int) Math.floor(student1.getScore() - student2.getScore());
	}
	
}
