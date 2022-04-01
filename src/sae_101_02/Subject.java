package sae_101_02;

public enum Subject {
	R101("Initiation au développement");
	
	public final String nom_mat;
	
	Subject(String nom_mat) {
		this.nom_mat = nom_mat;
	}
	
	public String toString() {
		return this + " - " + this.nom_mat;
	}
}
