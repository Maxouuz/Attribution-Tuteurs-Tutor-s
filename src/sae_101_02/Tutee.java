package sae_101_02;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Classe représentant un élève de première année souhaitant être tutoré
 * @author nathan.hallez.etu
 *
 */
public class Tutee extends Student {
	/**
	 * Liste de tous les sujets difficiles pour l'élève
	 */
	private final Set<Subject> hardSubjects;
	
	/**
	 * Constructeur de la classe Tutor
	 * @param FORENAME
	 * @param NAME
	 * @param moyenne
	 * @param hardSubjects
	 */
	public Tutee(String FORENAME, String NAME, double moyenne, Collection<? extends Subject> hardSubjects) {
		super(FORENAME, NAME, moyenne);
		this.hardSubjects = new LinkedHashSet<>();
		this.hardSubjects.addAll(hardSubjects);
	}
	
	/**
	 * Méthode pour ajouter une nouvelle matière difficile pour l'élève
	 * @param s
	 */
	public void addHardSubject(Subject student) {
		hardSubjects.add(student);
	}
	
	/**
	 * Méthode pour enlever une matière difficile pour l'élève
	 * @param s
	 */
	public void removeHardSubject(Subject student) {
		hardSubjects.remove(student);
	}
}
