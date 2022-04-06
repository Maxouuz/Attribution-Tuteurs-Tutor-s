package sae_101_02;

public abstract class Person {
	private final String LOGIN;
	private final String FORENAME;
	private final String NAME;
	private static int id = 0;
	
	public Person(String FORENAME, String NAME, String extension) {
		this.FORENAME = FORENAME;
		this.NAME = NAME;
		
		
		String tmpLogin = FORENAME + "." + NAME + id;
		id++;
		
		// On peut ajouter une extension à la fin d'un login (comme .etu)
		if (extension != null && !extension.equals(""))
			tmpLogin += "." + extension;
		
		this.LOGIN = tmpLogin;
	}
	
	public Person(String FORENAME, String NAME) {
		this(FORENAME, NAME, null);
	}

	public String getLogin() {
		return LOGIN;
	}

	public String getForename() {
		return FORENAME;
	}

	public String getName() {
		return NAME;
	}
}