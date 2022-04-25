package sae_201_02;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe de test pour la classe Student
 * @author lezlon
 *
 */
class StudentTest {
	/** Instances de la classe Student pour faire les tests */
	private Student s1, s2, s3, s4, s5;
	
	@BeforeEach
	/**
	 * Utilise les données exemples de DonneesPourTester
	 */
	@SuppressWarnings("PMD.AvoidDuplicateLiterals")
	public void initialization() {
		s1 = new Student("Jean","Dupont",6.9,1);
		s2 = new Student("Jean","Dupont",6.9,1);
		s3 = new Student("Jean","Dupont",12.7,1);
		s4 = new Student("Jean","Dupont",6.9,2);
		s5 = new Student("Hugues","Bigot",6.9,1);
	}

	@Test
	void equalsTest() {
		// Même instance
		assertEquals(s1, s1);
		// Même attributs
		assertEquals(s1, s2);
		// Note différente
		assertFalse(s1.equals(s3));
		// Promo différente
		assertFalse(s1.equals(s4));
		// Nom différent
		assertFalse(s1.equals(s5));
	}
}
