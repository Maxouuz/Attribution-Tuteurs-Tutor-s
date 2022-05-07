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
	 */
	public static void main(String[] args) throws ExceptionPromo {
		Tutoring tutoringShort = new Tutoring();
		Tutoring tutoringLong = new Tutoring();
		
		/**
		 * Ajout de tous les élèves dans l'instance de Tutoring
		 */
		
		tutoringShort.addStudent(new Student("Sophie", "Vallee", 15.5, 2, 0));
		tutoringShort.addStudent(new Student("Nicolas", "Roche", 16.5, 3, 0));
		tutoringShort.addStudent(new Student("Maurice", "Fernandez", 14.5, 3, 3));
		tutoringShort.addStudent(new Student("William", "Letellier", 18.5, 2, 2));
		tutoringShort.addStudent(new Student("Paul", "Sanchez", 13, 3, 0));
		
		tutoringShort.addStudent(new Student("Charles", "Letellier", 8, 1, 0));
		tutoringShort.addStudent(new Student("Daniel", "Daniel", 9.5, 1, 1));
		tutoringShort.addStudent(new Student("François", "Bertin", 7, 1, 1));
		tutoringShort.addStudent(new Student("Sabine", "Leleu", 5.5, 1, 0));
		tutoringShort.addStudent(new Student("Gabriel", "Marin", 9, 1, 8));
		tutoringShort.addStudent(new Student("Juliette", "Traore", 12, 1, 0));
		tutoringShort.addStudent(new Student("Franck", "Hebert", 2.5, 1, 0));
		
		for (String[] student: DonneesPourTester.studentData) {
			Student std = new Student(student[0], student[1], Double.valueOf(student[2]), Integer.parseInt(student[3]), 0);
			tutoringLong.addStudent(std);
		}
		
		// tutoring.setMoyenneMaxTutee(13.0);
		// tutoring.setMoyenneMinTutor(13.0);
		
		tutoringLong.createAssignments();
		
		System.out.println(tutoringLong.toStringTutees());
	}
}
