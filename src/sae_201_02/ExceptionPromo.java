package sae_201_02;

/**
 * Exception pour la promo des étudiants
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 */
public class ExceptionPromo extends Exception {
	/** Exception sans message */
	public ExceptionPromo() { super(); }
	
	/** Exception avec message */
	public ExceptionPromo(String message) { super(message); }

	/* Numéro UID généré automatiquement */
	private static final long serialVersionUID = -5365097718542792867L;
}
