package sae_101_02;

/**
 * Enumération des différentes ressources à tutorer
 * @author nathan.hallez.etu
 *
 */
public enum Subject {
	R101("Initiation au développement");
	
	/**
	 * Attribut qui représente le nom associé à la ressource
	 */
	public final String NOM_MAT;
	
	Subject(String NOM_MAT) {
		this.NOM_MAT = NOM_MAT;
	}
	
	/**
	 * Retourne une chaîne de caractère avec l'identifiant et le nom de la ressource
	 */
	public String toString() {
		return this + " - " + this.NOM_MAT;
	}
}
