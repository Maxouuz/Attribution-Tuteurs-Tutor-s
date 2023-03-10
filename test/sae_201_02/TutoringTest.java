package sae_201_02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

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
	private static Student tutor1, tutor2, tutor3;
	/** Variables de tutorés */
	private static Student tutee1, tutee2, tutee3, tutee4;
	
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
			Student tmp = Student.createStudent(student.next(), student.next(), student.nextInt(), student.nextInt(), Double.parseDouble(student.next()),
					Double.parseDouble(student.next()), Double.parseDouble(student.next()), Double.parseDouble(student.next()));
			tutoring.addStudent(tmp);
			tmp.setMotivation(tutoring, Motivation.valueOf(student.next()));
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

		tutor1 = Student.createStudent("Sophie", "Vallee", 2, 3, 15.5);
		tutor2 = Student.createStudent("Nicolas", "Roche", 3, 6, 16.5);
		tutor3 = Student.createStudent("Maurice", "Fernandez", 3, 4, 14.5);
		
		tutee1 = Student.createStudent("Charles", "Letellier", 1, 0, 8);
		tutee2 = Student.createStudent("Daniel", "Daniel", 1, 3, 9);
		tutee3 = Student.createStudent("François", "Bertin", 1, 9, 7);
		tutee4 = Student.createStudent("Sabine", "Leleu", 1, 1, 5.5);
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
			assertEquals(1, student.getPromo());
		}
	}
	
	@Test
	void noFirstYearInTutorsTest() {
		for (Student student: tutoring.getTutors()) {
			assertNotEquals(1, student.getPromo());
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
			tutoring2.addStudent(Student.createStudent("", "", 1, 0, 10));
			tutoring2.createAssignments();
		} catch (ExceptionPromo e) {
			res = false;
		}
		
		assertTrue(res);
		
		tutoring2 = new Tutoring(Subject.R101);
		
		res = true;
		
		// Liste de tutorés vide uniquement
		try {
			tutoring2.addStudent(Student.createStudent("", "", 3, 0, 10));
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
			tutor1.forceAssignment(tutoringShort, tutor2);
		} catch (ExceptionPromo e) {
			res = true;
		}
		assertTrue(res);
	}

	@Test
	void testforceAssignmentTooManyTutees() throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {

		boolean res = false;
		try {
			// On utilise un tuteur de deuxieme année, qui ne peut avoir qu'un seul tutoré
			tutee1.forceAssignment(tutoringShort, tutor1);
			tutee2.forceAssignment(tutoringShort, tutor1);
		} catch (ExceptionTooManyAssignments e) {
			res = true;
		}
		assertTrue(res);
		// je retire l'assignation pour pouvoir réutiliser le tutoré et le tuteur dans
		// d'autres tests
		tutee1.removeForcedAssignments(tutoringShort);

		try {
			// On utilise un tuteur de troisième année, qui a pour nombre de tutoré limite
			// la valeur décider par le professeur.
			// ici, la valeur est 2.
			tutee1.forceAssignment(tutoringShort, tutor2);
			tutee2.forceAssignment(tutoringShort, tutor2);
			tutee3.forceAssignment(tutoringShort, tutor2);
		} catch (ExceptionTooManyAssignments e) {
			res = true;
		}
		assertTrue(res);
		tutee1.removeForcedAssignments(tutoringShort);
		tutee2.removeForcedAssignments(tutoringShort);
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
			tutee1.forceAssignment(tutoringShort, tutor1);
			tutee1.forceAssignment(tutoringShort, tutor2);
		} catch (ExceptionTooManyAssignments e) {
			res = true;
		}
		assertTrue(res);
		tutee1.removeForcedAssignments(tutoringShort);
	}

	@Test
	void testforceAssignmentStudentNotInList()
			throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {

		boolean res = false;
		try {
			// On essaye d'assigner un étudiant qui existe, mais qui n'est dans aucune liste,
			// à un tuteur présent dans la liste des tuteurs.
			tutee4.removeForcedAssignments(tutoring);
			tutee4.forceAssignment(tutoringShort, tutor1);
		} catch (ExceptionNotInTutoring e) {
			res = true;
		}
		assertTrue(res);
		res = false;

		try {
			// Même chose mais avec un tutoré existant et un tutoré pas présent dans les
			// listes
			tutee4.removeForcedAssignments(tutoringShort);
			tutor1.removeForcedAssignments(tutoringShort);
			tutee4.forceAssignment(tutoringShort, tutor1);
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
}
