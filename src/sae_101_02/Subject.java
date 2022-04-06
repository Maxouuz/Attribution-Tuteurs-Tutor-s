package sae_101_02;

/**
 *  Colors that can be used
 *  {@link #R101}
 *  {@link #R102}
 *  {@link #R104}
 *  {@link #R105}
 */
public enum Subject {
	/**
     * R101 Subject, Initiation au développement
     */
	R101("Initiation au développement");
	/**
     * R102 Subject, Interface Web
     */
	R102("Interface Web");
	/**
     * R104 Subject, Introduction aux systèmes d'exploitation et leur fonctionnement.
     */
	R104("Introduction aux systèmes d'exploitation et leur fonctionnement");
	/**
     * R105 Subject, Bases de données
     */
	R105("Bases de données");
	
	public final String NOM_MAT;
	
	Subject(String NOM_MAT) {
		this.NOM_MAT = NOM_MAT;
	}
	
	public String toString() {
		return this + " - " + this.NOM_MAT;
	}
}
