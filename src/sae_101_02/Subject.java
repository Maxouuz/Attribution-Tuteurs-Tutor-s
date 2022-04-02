package sae_101_02;

public enum Subject {
	R101("Initiation au développement");
	
	public final String NOM_MAT;
	
	Subject(String NOM_MAT) {
		this.NOM_MAT = NOM_MAT;
	}
	
	public String toString() {
		return this + " - " + this.NOM_MAT;
	}
}
