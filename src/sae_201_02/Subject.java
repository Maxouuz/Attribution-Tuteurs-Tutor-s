package sae_201_02;

/**
 * Liste des matières pour lesquelles le soutien est disponible.
 * 
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 */
public enum Subject {
	/** Sujet R101, Initiation au développement. */
	R101("Initiation au développement"),
	/** Sujet R102, Interface Web. */
	R102("Interface Web"),
	/** Sujet R104, Introduction aux systèmes d'exploitation et leur fonctionnement. */
	R104("Introduction aux systèmes d'exploitation et leur fonctionnement"),
	/** Sujet R105, Bases de données. */
	R105("Bases de données");

	/**
	 * Représente l'intitulé d'une matière
	 * 
	 * @param NOM_MAT
	 */
	public final String NOM_MAT;

	/** 
	 * Constructeur de l'enum comportant l'initialisation de l'intitulé d'une matière
	 * @param NOM_MAT
	 */
	private Subject(String NOM_MAT) {
		this.NOM_MAT = NOM_MAT;
	}

	/**
	 * Affiche l'identifiant de la matière suivi de son intitulé.
	 */
	public String toString() {
		return this + " - " + this.NOM_MAT;
	}
}
