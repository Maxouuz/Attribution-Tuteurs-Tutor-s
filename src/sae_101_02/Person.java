package sae_101_02;

public abstract class Person {
	private final String LOGIN;
	private final String FORENAME;
	private final String NAME;
	
	public Person(String FORENAME, String NAME, String extension) {
		this.FORENAME = FORENAME;
		this.NAME = NAME;
		
		// TODO: vérifier si le nom et prénom n'existe pas déjà!
		String tmpLogin = FORENAME + "." + NAME;
		
		// On peut ajouter une extension à la fin d'un login (comme .etu)
		if (extension != null && !extension.equals(""))
			tmpLogin += "." + extension;
		
		this.login = tmpLogin;
	}
	
	public Person(String FORENAME, String NAME) {
		this(FORENAME, NAME, null);
	}

	public String getLogin() {
		return login;
	}

	public String getForename() {
		return FORENAME;
	}

	public String getName() {
		return NAME;
	}
}
