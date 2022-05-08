package sae_201_02;

import fr.ulille.but.sae2_02.donnees.DonneesPourTester;

/**
 * Classe qui règle le problème d'affectation
 * @author nathan.hallez.etu
 *
 */
public abstract class Assignment {
	
	/**
	 * Méthode main pour observer le résultat de l'affectation
	 * @param args
	 * @throws ExceptionPromo
	 * @throws ExceptionNotInTutoring 
	 * @throws ExceptionTooManyTutees 
	 */
	public static void main(String[] args) throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyTutees {
		Tutoring tutoringShort = new Tutoring();
		Tutoring tutoringLong = new Tutoring();
		
		/**
		 * Ajout de tous les élèves dans l'instance de Tutoring
		 */
		
		Student forcedTutee = new Student("Franck", "Hebert", 2.5, 1, 0);
		Student forcedTutee2 = new Student("Juliette", "Traore", 12, 1, 0);
		Student forcedTutee3 = new Student("Gabriel", "Marin", 9, 1, 8);
		Student forcedTutor = new Student("Paul", "Sanchez", 13, 3, 0);
		
		tutoringShort.addStudent(new Student("Sophie", "Vallee", 15.5, 2, 0));
		tutoringShort.addStudent(new Student("Nicolas", "Roche", 16.5, 3, 0));
		tutoringShort.addStudent(new Student("Maurice", "Fernandez", 14.5, 3, 3));
		tutoringShort.addStudent(new Student("William", "Letellier", 18.5, 2, 2));
		tutoringShort.addStudent(forcedTutor);
		
		tutoringShort.addStudent(new Student("Charles", "Letellier", 8, 1, 0));
		tutoringShort.addStudent(new Student("Daniel", "Daniel", 9.5, 1, 1));
		tutoringShort.addStudent(new Student("François", "Bertin", 7, 1, 1));
		tutoringShort.addStudent(new Student("Sabine", "Leleu", 5.5, 1, 0));
		tutoringShort.addStudent(forcedTutee3);
		tutoringShort.addStudent(forcedTutee2);
		tutoringShort.addStudent(forcedTutee);
		
		for (String[] student: DonneesPourTester.studentData) {
			Student std = new Student(student[0], student[1], Double.valueOf(student[2]), Integer.parseInt(student[3]), 0);
			tutoringLong.addStudent(std);
		}
		
		// tutoring.setMoyenneMaxTutee(13.0);
		// tutoring.setMoyenneMinTutor(13.0);
		
		tutoringShort.forceAssignment(forcedTutee, forcedTutor);
		tutoringShort.forceAssignment(forcedTutee2, forcedTutor);
		try {
			tutoringShort.forceAssignment(forcedTutee3, forcedTutor);
		} catch (ExceptionTooManyTutees e) {
			System.out.println("Impossible d'ajouter une affectation forcée supplémentaire pour ce tuteur");
		}
		tutoringShort.createAssignments();
		
		System.out.println(tutoringShort.toStringTutors());
	}
}
