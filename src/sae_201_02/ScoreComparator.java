package sae_201_02;

import java.util.Comparator;

/**
 * Comparateur pour la classe Student qui compare le score (poids des arêtes)
 * @author lezlon
 *
 */
public class ScoreComparator implements Comparator<Student> {
	
	/** Le score est comparé selon les poids donné par un tutorat */
	private final Tutoring tutoring;
	
	/**
	 * Le comparateur demande en paramètre un tutorat
	 * afin de comparer les scores avec les bons poids
	 * @param tutoring
	 */
	public ScoreComparator(Tutoring tutoring) {
		this.tutoring = tutoring;
	}
	
	/**
	 * On peut très bien ne pas donner de tutorat en paramètre,
	 * il prendra donc les poids par défaut de la classe
	 */
	public ScoreComparator() {
		this(new Tutoring(Subject.R101));
	}
	
	@Override
	public int compare(Student student1, Student student2) {
		return (int) Math.floor(student1.getScore(tutoring) - student2.getScore(tutoring));
	}
	
}
