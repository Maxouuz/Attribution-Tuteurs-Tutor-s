package sae_101_02;

/**
 * Classe abstraite qui représente une personne avec un login d'université.
 * @author nathan.hallez.etu
 */
public abstract class Person {
	/**
	 * Login de la personne sous la forme prenom.nom
	 */
	private final String LOGIN;
	/**
	 * Prénom de la personne
	 */
	private final String FORENAME;
	/**
	 * Nom de la personne
	 */
	private final String NAME;
	/**
	 * Numéro automatique pour générer les identifiants
	 */
	private static int loginCounter = 0;
	
	/**
	 * Constructeur de la classe personne
	 * avec la possibilité d'ajouter une extension au login
	 * @param FORENAME
	 * @param NAME 
	 * @param extension Ajout d'une extension à la fin d'un login (comme .etu)
	 */
	public Person(String FORENAME, String NAME, String extension) {
		this.FORENAME = FORENAME;
		this.NAME = NAME;
		
		
		String tmpLogin = FORENAME + "." + NAME + loginCounter;
		this.loginCounterIncrement();
		
		// On peut ajouter une extension à la fin d'un login (comme .etu)
		if (extension != null && !extension.equals(""))
			tmpLogin += "." + extension;
		
		this.LOGIN = tmpLogin;
	}
	
	/**
	 * Méthode qui incrémente le compteur pour générer les logins
	 */
	private void loginCounterIncrement() {
		loginCounter++;
	}
	
	/**
	 * Constructeur de la classe personne sans extension pour le login
	 * @param FORENAME
	 * @param NAME
	 */
	public Person(String FORENAME, String NAME) {
		this(FORENAME, NAME, null);
	}
	
	/**
	 * Retourne le login de la personne
	 * @return
	 */
	public String getLogin() {
		return LOGIN;
	}

	/**
	 * Retourne le prénom de la personne
	 * @return
	 */
	public String getForename() {
		return FORENAME;
	}

	/**
	 * Retourne le nom de la personne
	 * @return
	 */
	public String getName() {
		return NAME;
	}
}