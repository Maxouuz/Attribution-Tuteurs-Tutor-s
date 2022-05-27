package sae_201_02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe de test pour la classe Tutoring
 * @author Maxence Stievenard, Nathan Hallez, Rémi Vautier
 *
 */
public class TutoringTest {
	
	/** Instance de Tutoring qui contient des exemples d'élèves de DonneesPourTester */
	private static Tutoring tutoring;
	/** Instance de Tutoring qui contient des exemples de graphes */
	private static Tutoring tutoringShort;
	/** Variables de tuteurs */
	private static Student tutor1, tutor2, tutor3, tutor4, tutor5;
	/** Variables de tutorés */
	private static Student tutee1, tutee2, tutee3, tutee4, tutee5, tutee6, tutee7;
	
	@BeforeEach
	/**
	 * Utilise les données exemples de DonneesPourTester + données supplémentaires
	 */
	public void initialization1() throws ExceptionPromo, IOException {
		tutoring = new Tutoring(Subject.R101);
		
		BufferedReader reader = new BufferedReader(new FileReader("./res/eleves.csv"));
		String line = reader.readLine();
		Scanner student = new Scanner("");
		
		while (line != null) {
			student = new Scanner(line);
			student.useDelimiter(";");
			Student tmp = new Student(student.next(), student.next(), student.nextInt(), student.nextInt(), Double.parseDouble(student.next()),
					Double.parseDouble(student.next()), Double.parseDouble(student.next()), Double.parseDouble(student.next()));
			tutoring.addStudent(tmp);
			tutoring.addStudentMotivation(tmp, Motivation.valueOf(student.next()));
			line = reader.readLine();
		}
		
		student.close();
		reader.close();
	}
	
	@BeforeEach
	/**
	 * 
	 * @throws ExceptionPromo
	 */
	public void initialization2() throws ExceptionPromo {
		tutoringShort = new Tutoring(Subject.R101);

		tutor1 = new Student("Sophie", "Vallee", 2, 3, 15.5);
		tutor2 = new Student("Nicolas", "Roche", 3, 6, 16.5);
		tutor3 = new Student("Maurice", "Fernandez", 3, 4, 14.5);
		tutor4 = new Student("William", "Letellier", 2, 2, 18.5);
		tutor5 = new Student("Paul", "Sanchez", 3, 0, 13.7);
		
		tutee1 = new Student("Charles", "Letellier", 1, 0, 8);
		tutee2 = new Student("Daniel", "Daniel", 1, 3, 9);
		tutee3 = new Student("François", "Bertin", 1, 9, 7);
		tutee4 = new Student("Sabine", "Leleu", 1, 1, 5.5);
		tutee5 = new Student("Gabriel", "Marin", 1, 0, 9);
		tutee6 = new Student("Juliette", "Traore", 1, 30, 12);
		tutee7 = new Student("Franck", "Hebert", 1, 5, 2.5);
	}
	
	@BeforeEach
	/**
	 * Utilise un groupe d'étudiant exemples de DonneesPourTester
	 */
	public void initializationStudentGroup() throws ExceptionPromo {
		// Création d'un groupe d'étudiant
		tutoringShort.addStudent(tutee1);
		tutoringShort.addStudent(tutee2);
		tutoringShort.addStudent(tutee3);
		tutoringShort.addStudent(tutor1);
		tutoringShort.addStudent(tutor2);
		tutoringShort.addStudent(tutor3);
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
		Tutoring tutoring2 = new Tutoring(Subject.R101);
		
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
			tutoring2.addStudent(new Student("", "", 1, 0, 10));
			tutoring2.createAssignments();
		} catch (ExceptionPromo e) {
			res = false;
		}
		
		assertTrue(res);
		
		tutoring2 = new Tutoring(Subject.R101);
		
		res = true;
		
		// Liste de tutorés vide uniquement
		try {
			tutoring2.addStudent(new Student("", "", 3, 0, 10));
			tutoring2.createAssignments();
		} catch (ExceptionPromo e) {
			res = false;
		}
		
		assertTrue(res);
	}
	
	@Test
	void testAddFakeStudentMoreTutorThanTutees() throws ExceptionPromo {
		/** Création des ArrayList */
		/**Ici plus de tutorés que de tuteurs*/
		Set<Student> exemple1Tutees = new LinkedHashSet<>();
		exemple1Tutees.add(tutee1);
		exemple1Tutees.add(tutee2);
		exemple1Tutees.add(tutee3);
		
		Set<Student> exemple1Tutors = new LinkedHashSet<>();
		exemple1Tutors.add(tutor1);
		exemple1Tutors.add(tutor2);

		/**Passons au corps du test*/
		/**Ici dans le cas où il y a plus de tutorés que de tuteurs*/
		tutoring.addFakeStudents(exemple1Tutees, exemple1Tutors);
		
		assertEquals(exemple1Tutees.size(), exemple1Tutors.size());
	}
	
	@Test
	void testforceAssignmentTuteeTutor()
			throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		/**
		 * forceAssigment est une méthode qui permet de forcer une association entre un
		 * tuteur et un tutoré. Si l'association n'est pas possible, il retourne une
		 * exception avec la cause de l'impossibilité.
		 * 
		 * removeForceAssigment, est une méthode qui retire, à partir d'un étudiant,
		 * l'association forcée dans laquelle il se trouve, dans le cas où elle existe.
		 */

		boolean res = false;

		// test de forceAssigment et exemples de removeForceAssigment

		try {
			// On essaye de forcer un tutoré à un autre, ce qui est impossible
			tutoringShort.forceAssignment(tutee1, tutee2);
		} catch (ExceptionPromo e) {
			res = true;
		}
		assertTrue(res);
		res = false;

		try {
			// Même chose, mais avec des tuteurs
			tutoringShort.forceAssignment(tutor1, tutor2);
		} catch (ExceptionPromo e) {
			res = true;
		}
		assertTrue(res);
	}

	@Test
	void testforceAssignmentTooManyTutees()
			throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {

		boolean res = false;
		try {
			// On utilise un tuteur de deuxieme année, qui ne peut avoir qu'un seul tutoré
			tutoringShort.forceAssignment(tutee1, tutor1);
			tutoringShort.forceAssignment(tutee2, tutor1);
		} catch (ExceptionTooManyAssignments e) {
			res = true;
		}
		assertTrue(res);
		res = false;
		// je retire l'assignation pour pouvoir réutiliser le tutoré et le tuteur dans
		// d'autres tests
		tutoringShort.removeForcedAssignment(tutee1);

		try {
			// On utilise un tuteur de troisième année, qui a pour nombre de tutoré limite
			// la valeur décider par le professeur.
			// ici, la valeur est 2.
			tutoringShort.forceAssignment(tutee1, tutor2);
			tutoringShort.forceAssignment(tutee2, tutor2);
			tutoringShort.forceAssignment(tutee3, tutor2);
		} catch (ExceptionTooManyAssignments e) {
			res = true;
		}
		assertTrue(res);
		tutoringShort.removeForcedAssignment(tutee1);
		tutoringShort.removeForcedAssignment(tutee2);
	}

	@Test
	void testforceAssignmentAlreadyHaveATutor()
			throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {

		boolean res = false;

		try {
			// On essaye d'assigner un tutoré deux fois à deux tuteurs différends
			// Le tutee étant assigner une première fois ne sera plus dans la
			// liste de tutee en attente et donc ne pourra plus etre assigner dans un autre
			// groupe.
			tutoringShort.forceAssignment(tutee1, tutor1);
			tutoringShort.forceAssignment(tutee1, tutor2);
		} catch (ExceptionTooManyAssignments e) {
			res = true;
		}
		assertTrue(res);
		tutoringShort.removeForcedAssignment(tutee1);
	}

	@Test
	void testforceAssignmentStudentNotInList()
			throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {

		boolean res = false;
		try {
			// On essaye d'assigner un étudiant qui existe, mais qui n'est dans aucune liste,
			// à un tuteur présent dans la liste des tuteurs.
			tutoringShort.forceAssignment(tutee4, tutor1);
		} catch (ExceptionNotInTutoring e) {
			res = true;
		}
		assertTrue(res);
		res = false;

		try {
			// Même chose mais avec un tutoré existant et un tutoré pas présent dans les
			// listes
			tutoringShort.forceAssignment(tutee4, tutor1);
		} catch (ExceptionNotInTutoring e) {
			res = true;
		}
		assertTrue(res);
	}

	@Test
	void testAddFakeStudentLessTutorThanTutees() throws ExceptionPromo {
		/*Et ici plus de tuteurs que de tutorés*/
		Set<Student> exemple2Tutees = new LinkedHashSet<>();
		exemple2Tutees.add(tutee1);
		exemple2Tutees.add(tutee2);
		
		Set<Student> exemple2Tutors = new LinkedHashSet<>();
		exemple2Tutors.add(tutor1);
		exemple2Tutors.add(tutor2);
		exemple2Tutors.add(tutor3);

		tutoring.addFakeStudents(exemple2Tutees, exemple2Tutors);

		assertEquals(exemple2Tutees.size(), exemple2Tutors.size());
	}
	
	@AfterAll
	static void showExemple() throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
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
		
		System.out.println("\n--- AFFECTATION AVEC AFFECTATION ANNULÉE ---\n");
		tutoringShort.doNotAssign(tutee4, tutor3);
		tutoringShort.createAssignments();
		System.out.println(tutoringShort.toStringTutors());
		
		String path = System.getProperty("user.dir") + File.separator + "res" + File.separator;
		File save1 = new File(path + "tutoring_save.json");
		File save2 = new File(path + "tutoring_save2.json");
		try {
			tutoringShort.save(save1);
			Tutoring loaded = Tutoring.load(save1);
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
