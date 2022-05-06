package sae_201_02;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Classe de test pour la classe Student
 * @author lezlon
 *
 */
class StudentTest {
	/** Instances de la classe Student pour faire les tests */
	private Student st1, st2, st3, st4, st5, st6;
	
	/*@BeforeEach
	@SuppressWarnings("PMD.AvoidDuplicateLiterals")
	public void initialization() {
		st1 = new Student("Jean","Dupont",6.9,1,0);
		st2 = new Student("Jean","Dupont",6.9,1,0);
		st3 = new Student("Jean","Dupont",12.7,1,0);
		st4 = new Student("Jean","Dupont",6.9,2,0);
		st5 = new Student("Hugues","Bigot",6.9,1,0);
		st6 = new Student("Jean","Dupont",6.9,1,2);
	}*/

	@Test
	void equalsTest() throws Exception_Promo {
		st1 = new Student("Jean","Dupont",6.9,1,0);
		st2 = new Student("Jean","Dupont",6.9,1,0);
		st3 = new Student("Jean","Dupont",12.7,1,0);
		st4 = new Student("Jean","Dupont",6.9,2,0);
		st5 = new Student("Hugues","Bigot",6.9,1,0);
		st6 = new Student("Jean","Dupont",6.9,1,2);
		
		// Même instance
		assertEquals(st1, st1);
		// Même attributs mais INE différent
		assertFalse(st1.equals(st2));
		// Note différente
		assertFalse(st1.equals(st3));
		// Promo différente
		assertFalse(st1.equals(st4));
		// Nom différent
		assertFalse(st1.equals(st5));
		// Nombre d'absences différent
		assertFalse(st1.equals(st6));
	}
	
	@Test
	void scoreTest() throws Exception_Promo {
		/**
		 * Rappel: Plus le score est haut, plus l'étudiant est considéré comme meilleur
		 */
		
		st1 = new Student("Jean","Dupont",10,2,0);
		st2 = new Student("Hugues","Bigot",9.6,2,0);
		st3 = new Student("Franck","Hebert",8,3,0);
		st4 = new Student("Sabine", "Leleu", 10.5,2,0);
		st5 = new Student("Nicolas", "Roche", 10,2,3);
		// Note supérieure
		assertTrue(st1.getScore() > st2.getScore());
		// Même score pour une année d'écart avec deux points de différence
		assertTrue(st1.getScore() == st3.getScore());
		// Meilleur score pour un étudiant de deuxième année avec 2.5 points de plus
		assertTrue(st4.getScore() > st3.getScore());
		// Moins bon score pour un étudiant ayant des absences
		assertTrue(st5.getScore() < st1.getScore());
	}
	
	@Test
	void errorWithWrongPromoTest() {
		// Promo inférieur à 1
		boolean res = false;
		try {
			new Student("Jean", "Dupont", 10, 0, 0);
		} catch (Exception_Promo e) {
			res = true;
		}
		assertTrue(res);
		
		// Promo supérieur à 3
		res = false;
		try {
			new Student("Jean", "Dupont", 10, 4, 0);
		} catch (Exception_Promo e) {
			res = true;
		}
		assertTrue(res);
		
		// Promo entre 1 et 3
		res = false;
		try {
			new Student("Jean", "Dupont", 10, 1, 0);
			new Student("Hugues","Bigot", 10, 2, 0);
			new Student("Franck","Hebert", 10, 3, 0);
		} catch (Exception_Promo e) {
			res = true;
		}
		assertFalse(res);
	}
}
