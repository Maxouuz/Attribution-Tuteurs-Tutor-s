package sae_201_02;

import fr.ulille.but.sae2_02.donnees.DonneesPourTester;
import fr.ulille.but.sae2_02.graphes.GrapheNonOrienteValue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

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
	void testAddFakeStudent() throws ExceptionPromo{
		/** Création des étudiants pour les ArrayLists*/
		Student st1 = new Student("Michel", "Blanc", 16, 1, 0);
		Student st2 = new Student("Laurent", "Blanc", 16, 1, 0);
		Student st3 = new Student("Patrick", "Bruel", 16, 1, 0);
		Student st4 = new Student("Jean", "Jardin", 16, 3, 2);
		Student st5 = new Student("Henri", "Voisin", 16, 3, 0);
		Student st6 = new Student("Maxence", "Stievenard", 16, 3, 2);
		
		/**Création des faux étudiants qui vont être ajoutés manuellement*/
		Student fakeTutee = new Student("", "", 0, 1, 0);
		Student fakeTutors = new Student("", "", 0, 2, 0);
		
		/** Création des ArrayList */
		/**Ici plus de tutorés que de tuteurs*/
		List<Student> exemple1Tutees = new ArrayList<Student>();
		exemple1Tutees.add(st1);
		exemple1Tutees.add(st2);
		exemple1Tutees.add(st3);
		
		List<Student> exemple1Tutors = new ArrayList<Student>();
		exemple1Tutors.add(st4);
		exemple1Tutors.add(st5);
		
		/*Et ici plus de tuteurs que de tutorés*/
		List<Student> exemple2Tutees = new ArrayList<Student>();
		exemple2Tutees.add(st1);
		exemple2Tutees.add(st2);
		
		List<Student> exemple2Tutors = new ArrayList<Student>();
		exemple2Tutors.add(st4);
		exemple2Tutors.add(st5);
		exemple2Tutors.add(st6);
		
		/**Ajout a la main des fakeStudent*/
		
		List<Student> exemple1TutorsFake = new ArrayList<Student>();
		exemple1Tutors.add(st4);
		exemple1Tutors.add(st5);
		exemple2Tutors.add(fakeTutors);
		
		/**Ici le fake est le tutoré*/
		List<Student> exemple2TuteesFake = new ArrayList<Student>();
		exemple1Tutees.add(st1);
		exemple1Tutees.add(st2);
		exemple1Tutees.add(fakeTutee);
		

		
		/** J'instancie le GrapheNonOrienteValué<Student>*/
		GrapheNonOrienteValue<Student> graphe = new GrapheNonOrienteValue<Student>();
		
		/**Passons au corps du test*/
		/**Ici dans le cas où il y a plus de tutorés que de tuteurs*/
		tutoring.addFakeStudents(exemple1Tutees, exemple2Tutors, graphe);
		tutoring.addFakeStudents(exemple2Tutees, exemple2Tutors, graphe);
		
		/**verifions l'égalité entre les etudiants ajoutés manuellement et ceux ajoutés par la méthode*/
		
		
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
		
		tutoringShort.addStudent(tutor1);
		tutoringShort.addStudent(tutor2);
		tutoringShort.addStudent(tutor3);
		tutoringShort.addStudent(tutor4);
		tutoringShort.addStudent(tutor5);
		
		tutoringShort.addStudent(tutee1);
		tutoringShort.addStudent(tutee2);
		tutoringShort.addStudent(tutee3);
		tutoringShort.addStudent(tutee4);
		tutoringShort.addStudent(tutee5);
		tutoringShort.addStudent(tutee6);
		tutoringShort.addStudent(tutee7);
		
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