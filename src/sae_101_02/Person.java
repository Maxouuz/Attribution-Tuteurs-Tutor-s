package sae_101_02;

public abstract class Person {
	private final String login;
	private final String forename;
	private final String name;
	
	public Person(String forename, String name, String extension) {
		this.forename = forename;
		this.name = name;
		
		// TODO: vérifier si le nom et prénom n'existe pas déjà!
		String tmpLogin = forename + "." + name;
		
		// On peut ajouter une extension à la fin d'un login (comme .etu)
		if (extension != null && !extension.equals(""))
			tmpLogin += "." + extension;
		
		this.login = tmpLogin;
	}
	
	public Person(String forename, String name) {
		this(forename, name, null);
	}

	public String getLogin() {
		return login;
	}

	public String getForename() {
		return forename;
	}

	public String getName() {
		return name;
	}
}
