package sae_201_02;

import fr.ulille.but.sae2_02.donnees.DonneesPourTester;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe de test pour la classe Tutoring
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 *
 */
public class TutoringTest {
	
	/**
	 * Instance de Tutoring utilisé pour faire les tests
	 */
	private Tutoring tutoring;
	
	@BeforeEach
	/**
	 * Utilise les données exemples de DonneesPourTester
	 */
	public void initialization() throws ExceptionPromo {
		tutoring = new Tutoring();
		String[][] data = DonneesPourTester.studentData;
		for (String[] student: data) {
			Student tmp = new Student(student[0], student[1], Double.valueOf(student[2]), Integer.valueOf(student[3]), 0);
			tutoring.addStudent(tmp);
		}
	}
	
	@Test
	void onlyFirstYearInTuteesTest() {
		for (Student student: tutoring.getTutees()) {
			assertEquals(1, student.getPROMO());
		}
	}
	
	@Test
	void noFirstYearInTutorsTest() {
		for (Student student: tutoring.getTutors()) {
			assertNotEquals(1, student.getPROMO());
		}
	}
	
	@Test
	void emptyTutorOrTutee() {
		Tutoring tutoring2 = new Tutoring();
		
		boolean res = true;
		
		// Pas d'erreurs avec une liste vide
		try {
			tutoring2.createAssignments();
		} catch (ExceptionPromo e) {
			res = false;
		}
		
		assertTrue(res);
		
		res = true;
		
		// Liste de tuteurs vide uniquement
		try {
			tutoring2.addStudent(new Student("", "", 10, 1, 0));
			tutoring2.createAssignments();
		} catch (ExceptionPromo e) {
			res = false;
		}
		
		assertTrue(res);
		
		tutoring2 = new Tutoring();
		
		res = true;
		
		// Liste de tutorés vide uniquement
		try {
			tutoring2.addStudent(new Student("", "", 10, 3, 0));
			tutoring2.createAssignments();
		} catch (ExceptionPromo e) {
			res = false;
		}
		
		assertTrue(res);
	}
	
	@Test
	void showExemple() throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyTutees {
		Tutoring tutoringShort = new Tutoring();
		Student tutor1 = new Student("Sophie", "Vallee", 15.5, 2, 3);
		Student tutor2 = new Student("Nicolas", "Roche", 16.5, 3, 6);
		Student tutor3 = new Student("Maurice", "Fernandez", 14.5, 3, 4);
		Student tutor4 = new Student("William", "Letellier", 18.5, 2, 2);
		Student tutor5 = new Student("Paul", "Sanchez", 13.7, 3, 0);
		
		Student tutee1 = new Student("Charles", "Letellier", 8, 1, 0);
		Student tutee2 = new Student("Daniel", "Daniel", 9, 1, 3);
		Student tutee3 = new Student("François", "Bertin", 7, 1, 9);
		Student tutee4 = new Student("Sabine", "Leleu", 5.5, 1, 1);
		Student tutee5 = new Student("Gabriel", "Marin", 9, 1, 0);
		Student tutee6 = new Student("Juliette", "Traore", 12, 1, 30);
		Student tutee7 = new Student("Franck", "Hebert", 2.5, 1, 5);
		
		tutoringShort.addAllStudents(tutor1, tutor2, tutor3, tutor4, tutor5,
									 tutee1, tutee2, tutee3, tutee4, tutee5, tutee6, tutee7);
		
		tutoringShort.addStudentMotivation(tutor4, Motivation.MOTIVATED);
		tutoringShort.addStudentMotivation(tutee2, Motivation.NOT_MOTIVATED);
		tutoringShort.addStudentMotivation(tutee3, Motivation.MOTIVATED);
		tutoringShort.addStudentMotivation(tutee5, Motivation.NOT_MOTIVATED);
		tutoringShort.addStudentMotivation(tutee6, Motivation.MOTIVATED);
		
		System.out.println("--- AFFECTATION PAR DÉFAUT ---\n");
		tutoringShort.createAssignments();
		System.out.println(tutoringShort.toStringTutors());
		
		System.out.println("\n--- AFFECTATION AVEC AFFECTATION MANUELLE ---\n");
		tutoringShort.forceAssignment(tutee6, tutor5);
		tutoringShort.createAssignments();
		System.out.println(tutoringShort.toStringTutors());
	}
}
