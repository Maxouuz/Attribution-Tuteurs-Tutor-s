package sae_201_02;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;

/**
 * Classe d'exemple pour le projet
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 *
 */
public class Example {
	
	private Example() {};
	
	/** Instance de Tutoring qui contient des exemples de graphes */
	private static Tutoring tutoring;
	/** Variables de tuteurs */
	private static Student tutor1, tutor2, tutor3, tutor4, tutor5;
	/** Variables de tutorés */
	private static Student tutee1, tutee2, tutee3, tutee4, tutee5, tutee6, tutee7;
	
	/**
	 * Initialise le tutorat et les étudiants
	 * @throws ExceptionPromo
	 */
	private static void initializeDefault() throws ExceptionPromo {
		tutoring = new Tutoring(Subject.R101);

		tutor1 = Student.createStudent("Sophie", "Vallee", 2, 3, 15.5);
		tutor2 = Student.createStudent("Nicolas", "Roche", 3, 6, 16.5);
		tutor3 = Student.createStudent("Maurice", "Fernandez", 3, 4, 14.5);
		tutor4 = Student.createStudent("William", "Letellier", 2, 2, 18.5);
		tutor5 = Student.createStudent("Paul", "Sanchez", 3, 0, 13.7);
		
		tutee1 = Student.createStudent("Charles", "Letellier", 1, 0, 8);
		tutee2 = Student.createStudent("Daniel", "Daniel", 1, 3, 9);
		tutee3 = Student.createStudent("François", "Bertin", 1, 9, 7);
		tutee4 = Student.createStudent("Sabine", "Leleu", 1, 1, 5.5);
		tutee5 = Student.createStudent("Gabriel", "Marin", 1, 0, 9);
		tutee6 = Student.createStudent("Juliette", "Traore", 1, 30, 12);
		tutee7 = Student.createStudent("Franck", "Hebert", 1, 5, 2.5);
		
		tutoring.addAllStudents(tutor1, tutor2, tutor3, tutor4, tutor5,
				 tutee1, tutee2, tutee3, tutee4, tutee5, tutee6, tutee7);
	
		tutor4.setMotivation(tutoring, Motivation.MOTIVATED);
		tutee2.setMotivation(tutoring, Motivation.NOT_MOTIVATED);
		tutee3.setMotivation(tutoring, Motivation.MOTIVATED);
		tutee5.setMotivation(tutoring, Motivation.NOT_MOTIVATED);
		tutee6.setMotivation(tutoring, Motivation.MOTIVATED);
	}
	
	private static void loadTutoringArgs(String[] args) throws ExceptionPromo {
		if (args.length == 0) {
			initializeDefault();
		} else  {
			try {
				tutoring = TutoringSave.load(new File(args[0]));
			} catch (IOException e) {
				System.out.println("Chemin invalide");
			} catch (JSONException | ExceptionPromo | ExceptionNotInTutoring
					| ExceptionTooManyAssignments e) {
				System.out.println("Erreur de format:\n");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Démonstration de l'utilisation du programme
	 * @param args
	 * @throws ExceptionPromo
	 * @throws ExceptionNotInTutoring
	 * @throws ExceptionTooManyAssignments
	 */
	public static void main(String[] args) throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		loadTutoringArgs(args);
		
		System.out.println("--- AFFECTATION PAR DÉFAUT ---\n");
		tutoring.createAssignments();
		System.out.println(tutoring.toStringTutors());
		
		if (args.length == 0) {
			System.out.println("\n--- AFFECTATION AVEC AFFECTATION MANUELLE ---\n");
			tutee6.forceAssignment(tutoring, tutor5);
			tutoring.createAssignments();
			System.out.println(tutoring.toStringTutors());
			
			System.out.println("\n--- AFFECTATION AVEC AFFECTATION ANNULÉE ---\n");
			tutee4.doNotAssign(tutoring, tutor3);
			tutoring.createAssignments();
			System.out.println(tutoring.toStringTutors());
		}
		
		String filename = "tutoring_save.json";
		File save = new File(filename);
		try {
			TutoringSave.save(tutoring, save);
			System.out.println("Tutorat sauvegardé dans le fichier " + filename);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}
}
