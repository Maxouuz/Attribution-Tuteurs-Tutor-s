package sae_201_02;

/**
 * Exception qui se déclenche lorsque qu'il y a trop d'affectations pour un tuteur
 * @author nathan.hallez.etu
 */
public class ExceptionTooManyAssignments extends Exception {
	/** Exception avec message */
	public ExceptionTooManyAssignments(String message) { super(message); }

	/* Numéro UID généré automatiquement */
	private static final long serialVersionUID = 6401090478524725950L;
}
