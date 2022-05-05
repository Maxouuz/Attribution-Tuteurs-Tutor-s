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
	private Student st1, st2, st3, st4, st5;
	
	/**
	 * Utilise les données exemples de DonneesPourTester
	 */
	@BeforeEach
	@SuppressWarnings("PMD.AvoidDuplicateLiterals")
	public void initialization() {
		st1 = new Student("Jean","Dupont",6.9,1,0);
		st2 = new Student("Jean","Dupont",6.9,1,0);
		st3 = new Student("Jean","Dupont",12.7,1,0);
		st4 = new Student("Jean","Dupont",6.9,2,0);
		st5 = new Student("Hugues","Bigot",6.9,1,0);
	}

	@Test
	void equalsTest() {
		// Même instance
		assertEquals(st1, st1);
		// Même attributs
		assertEquals(st1, st2);
		// Note différente
		assertFalse(st1.equals(st3));
		// Promo différente
		assertFalse(st1.equals(st4));
		// Nom différent
		assertFalse(st1.equals(st5));
	}
}
