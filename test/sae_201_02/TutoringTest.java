package sae_201_02;

import fr.ulille.but.sae2_02.donnees.DonneesPourTester;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
}